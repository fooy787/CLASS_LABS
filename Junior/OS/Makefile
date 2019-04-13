
CC=arm-none-eabi-gcc
LD=arm-none-eabi-ld
OBJCOPY=arm-none-eabi-objcopy
QEMU=qemu-system-arm
TRUNCATE=truncate
MKE2FS=/usr/sbin/mke2fs 
DEBUGFS=/usr/sbin/debugfs

#CC=arm-linux-gnu-gcc
#LD=arm-linux-gnu-ld
#OBJCOPY=arm-linux-gnu-objcopy


AS=$(CC)

CC+= -Wall -c -mcpu=arm926ej-s -marm -Werror -O2
LDKERNEL=$(LD) -Map kernelmap.txt -T linkerscript.txt
LDPROGRAM=$(LD) -T linkerscript2.txt
AS+= -c -x assembler-with-cpp -mcpu=arm926ej-s
QEMUARGS=-machine integratorcp -kernel kernel.bin -serial stdio -sd sdcard.img
DISPLAY?=:0
export DISPLAY
SDL_STDIO_REDIRECT=no
export SDL_STDIO_REDIRECT

all: 
	$(AS) kernelasm.s
	$(AS) interruptasm.s
	$(CC) kernelmain.c
	$(CC) console.c
	$(CC) testsuite.c
	$(CC) kprintf.c
	$(CC) disk.c
	$(CC) interrupt.c
	$(CC) exec.c

	
	$(LDKERNEL) -o kernel.tmp kernelasm.o interruptasm.o kernelmain.o console.o testsuite.o kprintf.o disk.o interrupt.o exec.o
	$(OBJCOPY) -Obinary kernel.tmp kernel.bin
	$(TRUNCATE) -s 400000000 sdcard.img
	$(MKE2FS) -b 4096 -F -I 128 -q -t ext2 -r 0 -L moocow -g 32768 sdcard.img
	$(AS) crtasm.s
	$(CC) crtc.c
	$(CC) keytest.c
	$(LDPROGRAM) -o keytest.tmp keytest.o
	$(OBJCOPY) -Obinary keytest.tmp keytest.bin
	$(DEBUGFS) -w -f fscmd.txt sdcard.img
	"$(QEMU)" $(QEMUARGS)
	
	
clean:
	-/bin/rm *.o *.exe *.bin *.img *.tmp *.gch
