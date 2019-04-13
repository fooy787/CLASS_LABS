#include "interrupt.h"

static char linebuf[LINEBUF_SIZE];
static int linebuf_chars = 0;
static volatile int linebuf_ready=0;
extern void* itable_start;
extern void* itable_end;
int timer = 0;
int lastWasF0 = 0;
int shifted = 0;

void interrupt_init(){
    kmemcopy((void*)0, itable_start, itable_end-itable_start);
	*IRQ_ENABLE= (IRQ_KEYBOARD | IRQ_MOUSE | IRQ_TIMER );
	*TIMER_LOAD = 1000000;  
	*TIMER_RELOAD = 1000000;   
	*TIMER_ACK = 1;      //clear pending interrupts
	*TIMER_CTL = TIMER_ENABLE | TIMER_CYCLE_FOREVER | TIMER_DO_INTERRUPTS | TIMER_PRESCALE_1 | TIMER_32_BITS;

	*KEYBOARD_CTL = (1<<2) | (1<<4);
	asm volatile
	(
    "mrs r0,cpsr\n"
    "and r0,r0,#0xffffff7f\n"
    "msr cpsr,r0" 
    : : : "r0" 
	);
	
}

void keyboard_interrupt(){
    unsigned q = *KEYBOARD_DATA;
    //int press;
	
	char key = ' ';

    /*if(q&0x80)
	{
        press = 1;
	}
    else
	{
        press = 0;
	}
    q &= 0x7f;*/
	if(lastWasF0 == 0 && q != 0xf0)
	{
		switch(q)
		{
			case(0x0d):
				key = '\t';
				break;
			case(0x0e):
				key = '`';
				break;
			case(0x12):
				key = '*';
				shifted = 1;
				break;
			case(0x15):
				key = 'q';
				break;
			case(0x16):
				key = '1';
				break;
			case(0x1a):
				key = 'z';
				break;
			case(0x1b):
				key = 's';
				break;
			case(0x1c):
				key = 'a';
				break;
			case(0x1d):
				key = 'w';
				break;
			case(0x1e):
				key = '2';
				break;
			case(0x21):
				key = 'c';
				break;
			case(0x22):
				key = 'x';
				break;
			case(0x23):
				key = 'd';
				break;
			case(0x24):
				key = 'e';
				break;
			case(0x25):
				key = '4';
				break;
			case(0x26):
				key = '3';
				break;
			case(0x29):
				key = ' ';
				break;
			case(0x2a):
				key = 'v';
				break;
			case(0x2b):
				key = 'f';
				break;
			case(0x2c):
				key = 't';
				break;
			case(0x2d):
				key = 'r';
				break;
			case(0x2e):
				key = '5';
				break;
			case(0x31):
				key = 'n';
				break;
			case(0x32):
				key = 'b';
				break;
			case(0x33):
				key = 'h';
				break;
			case(0x34):
				key = 'g';
				break;
			case(0x35):
				key = 'y';
				break;
			case(0x36):
				key = '6';
				break;
			case(0x3a):
				key = 'm';
				break;
			case(0x3b):
				key = 'j';
				break;
			case(0x3c):
				key = 'u';
				break;
			case(0x3d):
				key = '7';
				break;
			case(0x3e):
				key = '8';
				break;
			case(0x42):
				key = 'k';
				break;
			case(0x43):
				key = 'i';
				break;
			case(0x44):
				key = 'o';
				break;
			case(0x45):
				key = '0';
				break;
			case(0x46):
				key = '9';
				break;
			case(0x49):
				key = '.';
				break;
			case(0x4a):
				key = '/';
				break;
			case(0x4b):
				key = 'l';
				break;
			case(0x4c):
				key = ';';
				break;
			case(0x4d):
				key = 'p';
				break;
			case(0x4e):
				key = '-';
				break;
			case(0x52):
				key = '\'';
				break;
			case(0x54):
				key = '[';
				break;
			case(0x55):
				key = '=';
				break;
			case(0x5a):
				key = '\n';
				break;
			case(0x5b):
				key = ']';
				break;
			case(0x5d):
				key = '\\';
				break;
			case(0x66):
				key = '\b';
				break;
			case(0x69):
				key = '1';
				break;
			case(0x6b):
				key = '4';
				break;
			case(0x6c):
				key = '7';
				break;
			case(0x70):
				key = '0';
				break;
			case(0x72):
				key = '2';
				break;
			case(0x73):
				key = '5';
				break;
			case(0x74):
				key = '6';
				break;
			case(0x75):
				key = '8';
				break;
			case(0x7a):
				key = '3';
				break;
			case(0x7d):
				key = '9';
				break;
			default:
				key = ' ';
				linebuf_chars++;
		}
		
		if( linebuf_ready )
		{
			kmemset(linebuf, 0, sizeof(linebuf));
			linebuf_chars = 0;
		}
		else if(key == '\b')
		{
			//remove character, if there is one
			if(linebuf_chars > 0)
			{
				linebuf[linebuf_chars] = '\0';
				linebuf_chars--;
				kprintf("\b");
			}
		}	
		else if(key == '\n')
		{
			linebuf_ready = 1;
			kprintf("\n");
		}
		else if( linebuf_chars >= LINEBUF_SIZE )
			;
		else if(key != '*')
		{
			if(shifted)
			{
				key-=32;
			}
			//ascii is printable
			linebuf[linebuf_chars] = key;
			
			kprintf("%c", key);
			linebuf_chars++;
			
		}
		else{;}
		lastWasF0 = 0;
		
		
		//kprintf("Got milk");
	}
	else if(q == 0x12 && lastWasF0 == 1)
	{
		shifted = 0;
	}
	else if(q != 0xf0 && lastWasF0 == 1)
	{
		lastWasF0 = 0;
	}
	
		
	else if(q == 0xf0)
	{
		lastWasF0 = 1;
		//kprintf("%x ", q);
	}
	
	
	
}

void sti(){
    asm volatile(
        "mrs r0,cpsr\n"
        "and r0,r0,#0xffffff7f\n"
        "msr cpsr,r0" : : : "r0" 
    );
}

void halt(){
    asm volatile(
        "mov r0,#0\n"
        "mcr p15,0,r0,c7,c0,4" 
        : : : "r0"
    );
}

int keyboard_getline(char* buffer,int num){
    while( !linebuf_ready ){
        sti();  //enable interrupts
        halt(); //halt until interrupt
    }
    int copied = (num < linebuf_chars ? num : linebuf_chars);
	kmemcopy((void*)buffer, (void*)linebuf, copied);
	kmemset(linebuf, 0, sizeof(linebuf));
	linebuf_chars = 0;
    linebuf_ready=0;
    return copied;
}

void handler_prefetchabort_c(){
    kprintf("handler_prefetchabort!");
}

void handler_undefinded_c(){
    kprintf("handler_undefinded!");
}

void handler_dataabort_c(){
    kprintf("handler_dataabort!");
}
void handler_reserved_c(){
    kprintf("handler_reserved!");
}

void handler_irq_c(){
    //see which interrupts are being asserted
	
    if( *IRQ_STATUS & IRQ_TIMER ){
        *TIMER_ACK = 1;    //acknowledge at timer chip
		timer+= 1000;
    }
	if(*IRQ_STATUS & IRQ_KEYBOARD)
	{

		keyboard_interrupt();
	}
	
}

void handler_fig_c(){
    kprintf("handler_fig!");
}
void handler_svc_c(unsigned* ptr){
	
    switch(ptr[0])
	{
        case SYSCALL_OPEN:
        {
            //r1 = filename
            //r2 = flags
            ptr[0] = file_open((char*)ptr[1], ptr[2]);
			file_seek(ptr[0], 0, SEEK_SET);
            break;
        }
        case SYSCALL_CLOSE:
        {
            //r1 = file descriptor
            ptr[0] = file_close(ptr[1]);
            break;
        }
		case SYSCALL_READ:
		{
			int fd = ptr[1];
			if( fd == 0 )
			{
				ptr[0] = keyboard_getline((char*)ptr[2],ptr[3]);
				break;
			}
			else if( fd < 3 )
			{
				ptr[0] = -ENOSYS;
				break;
			}
			else
			{
				ptr[0] = file_read(fd,(char*)ptr[2],ptr[3]);
				break;
			}
		}
		case SYSCALL_WRITE:
		{
			int fd = ptr[1];
			
			if(fd == 1 || fd == 2)
			{
				kprintf("%.*s", ptr[3], (char*)ptr[2]);
				break;
			}
			else
			{
				ptr[0] = -ENOSYS;
				break;
			}
			
		}

		case SYSCALL_HALT:
		{
			halt();	
			break;
		}
		case SYSCALL_UPTIME:
		{
			ptr[0] = timer;
			break;
		}

        default:
            ptr[0] = -ENOSYS;
    }
}
