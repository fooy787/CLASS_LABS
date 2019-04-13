//kernelmain.cabs
#include "console.h"
#include "disk.h"
#include "kprintf.h"
#include "interrupt.h"
#include "exec.h"
extern unsigned sbss, ebss;

void kmain(){
	kmemset(&sbss, 0, &ebss-&sbss);
    console_init();
	disk_init();
	interrupt_init();

	execute("keytest.bin");
	
    while(1){
		
		
    }
	
}