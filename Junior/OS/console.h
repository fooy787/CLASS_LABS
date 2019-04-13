//console.h

#define WIDTH 800
#define HEIGHT 600
#define framebuffer ((volatile unsigned char*)((0x7ffffff - (WIDTH * HEIGHT * 2))&0xffffff0))
void console_init();
void set_pixel(int x, int y, short color1, short color2);
void set_color(int r, int g, int b);
void set_character(int x, int y, char c);
void set_string(int x, int y, char* string);
void console_putc(char x);
void kmemcopy(void*, void *, int);
void kmemset(void*, char, int);