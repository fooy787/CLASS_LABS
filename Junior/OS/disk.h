#pragma once
#include "inode.h"
#include "console.h"
#define MMCP_START ((volatile unsigned*) 0x1c000000 ) 
#define POWER ( MMCP_START)
#define CLOCK (MMCP_START+1)
#define ARG (MMCP_START+2)
#define CMD (MMCP_START+3)
#define RESPONSE_CMD (MMCP_START+4)
#define RESPONSE (MMCP_START+5)
#define DATA_TIMER (MMCP_START+9)
#define DATA_LENGTH (MMCP_START+10)
#define DATA_CONTROL (MMCP_START+11)
#define DATA_COUNTER (MMCP_START+12)
#define STATUS (MMCP_START+13)
#define CLEAR (MMCP_START+14)
#define INTERRUPT0_MASK (MMCP_START+15)
#define INTERRUPT1_MASK (MMCP_START+16)
#define SELECT (MMCP_START+17)
#define FIFO_COUNT (MMCP_START+18)
#define DATA_FIFO (MMCP_START+32)
#define MAX_FILES 16
#define SEEK_SET 0
#define SEEK_CUR 1
#define SEEK_END 2
#define BYTES_PER_BLOCK 4096
int isBusy();

void disk_init();

int kdiv(int, int);
int kmemcmp(void*, void*, int);
int kstrlen(const char*);
int kmod(int, int);

void disk_read_sector(unsigned sector, void* datablock);
void disk_write_sector(unsigned sector, void* datablock);

void read_block(unsigned, void*);

int file_open(const char* fname, int flags);
int file_close(int fd);

unsigned get_file_inode(unsigned dir_inode, const char* filename);

void disk_read_inode(unsigned num, struct Inode* ino);
void disk_read_block(unsigned block, void* vp, int start, int count);
void disk_read_block_partial(unsigned block, void* vp, unsigned start, unsigned count);

int file_read(int fd, void* buf, int count);
int file_write(int fd, const void* buf, int count);
int file_seek(int fd, int offset, int whence);

void disk_print_info();