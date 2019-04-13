#define pl110 ( (volatile unsigned*)0xc0000000 )
#define haxis       (*(pl110+0))    //offset 0: horizontal axis                             
#define vaxis       (*(pl110+1))    //offset 4: vertical axis                               
#define polarity    (*(pl110+2))    //offset 8: clock+polarity 
#define lineend     (*(pl110+3))    //offset 12: line end
#define baseaddr1   (*(pl110+4))    //offset 16: upper panel base address                           
#define baseaddr2   (*(pl110+5))    //offset 20: lower panel base address
#define intmask     (*(pl110+6))    //offset 24: interrupt mask
#define params      (*(pl110+7))    //offset 28: panel parameters 



#include "console.h"
#include "RobotoFont.h"

int columns = WIDTH / CHAR_WIDTH;
int rows = HEIGHT / CHAR_HEIGHT;
int curCol = 0;
int curRow = 0;
volatile unsigned* serialport = (unsigned*) 0x16000000;
volatile unsigned* serialflags = (unsigned*) 0x16000018;

void console_init(){
	haxis = 0x3f1f3f00 | (WIDTH/4-4);
	vaxis = 0x80b6000 | (HEIGHT-1);
	polarity = 0x067f3800;
	params = 0x1829;
	baseaddr1 = (unsigned)framebuffer;
}

void set_pixel (int x, int y, short color1, short color2){
	
	framebuffer[WIDTH*2*y + x*2] = color1;
	framebuffer[WIDTH*2*y + x*2+1] = color2;
}

void set_character(int x, int y, char c)
{
	int i;
	int j;
	short character;
	short bitwiseChar;
	
	for(j = 0; j < CHAR_HEIGHT; j++)
	{
		character = font_data[(int)c][j];
		for(i = 0; i < CHAR_WIDTH; i++)
		{
			bitwiseChar = character & 1;
			character = character >> 1;
			if(bitwiseChar)
			{
				set_pixel(x - i, y + j, 0xff, 0x0f);
			}
			else set_pixel(x - i, y + j, 0, 0);
		}
	}
}

void set_string(int x, int y, char* string)
{
	int i = 0;
	while(*string != '\0')
	{
		set_character(x + (i * CHAR_WIDTH), y, *string);
		i++;
		string++;
	}
}

void console_putc(char x)
{	
	if(x == '\f')
	{
		while(curRow >=0) curRow--;
		{
			while(curCol >= 0) curCol--;
			{
				set_character(curCol * CHAR_WIDTH, curRow * CHAR_HEIGHT, ' ');
			}
		}
		curRow = 0;
		curCol = 0;
	}
	
	else if(x == '\t')
	{
		if ((curCol % 8) == 0) curCol += 8;
		while(curCol % 8 != 0) curCol++;
	}
	
	else if (x == '\n')
	{
		curRow++;
		curCol = 0;
	}
	
	else if(x == '\x7f')
	{
		if (curCol < 0)
		{
			curRow--;
			curCol = columns; 
		}
		set_character(curCol * CHAR_WIDTH, curRow * CHAR_HEIGHT, ' ');
		curCol--;
	}
	
	else if(x == '\r')
	{
		curCol = 0;
	}
	else if (x == '\b')
	{
		if (curCol < 0)
		{
			curRow--;
			curCol = columns; 
		}
		set_character(curCol * CHAR_WIDTH, curRow * CHAR_HEIGHT, ' ');
		curCol--;
	}
	
	else
	{
		curCol++;
		set_character(curCol * CHAR_WIDTH, curRow * CHAR_HEIGHT, x);
		
	
		if (curCol +1> columns)
		{
			curRow++;
			curCol = 0;
		}
		if (curCol < 0)
		{
			curRow--;
			curCol = columns; 
		}
		if(curRow * CHAR_HEIGHT+CHAR_HEIGHT>HEIGHT)
		{  
			kmemcopy((void*)framebuffer,(void*)framebuffer + (WIDTH * CHAR_HEIGHT * 2), (WIDTH * HEIGHT * 2));
			curRow--;
		}
	}	
	while(*serialflags & (1 << 5));
	*serialport = x;
	
	for(int i = 0; i < 100000; i++){}
	
}

void kmemset(void * p, char v, int n)
{
	while (n > 0)
	{
		*(char*)p = v;
		p++;
		n--;
	}
}

void kmemcopy(void * d, void * src, int num)
{
	while(num--) 
	{
		*(char*)d++ = *(char*)src++;
	}
}