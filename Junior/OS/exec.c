#include "exec.h"
int execute(const char* filename)
{
	unsigned int p = 0x400000;
	int f = file_open(filename, 0);
	if(f <0) return -EMFILE;
	int minSize;
	while((minSize = file_read(f, (void*)p, 10000)) != 0){
		p+=minSize;
	}
	
	int close = file_close(f);
	if(close<0) return -ENDENT;
	asm volatile
	(
    "mrs r0,cpsr\n"
    "and r0,#0xffffffe0\n"
    "orr r0,#0x10\n"
	"msr cpsr,r0\n"
	"mov sp,#0x800000\n"
	"mov pc,#0x400000\n"
	);

	return(0);
}