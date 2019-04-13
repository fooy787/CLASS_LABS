#include "disk.h"
#include "superblock.h"
#include "dirEntry.h"
#include "File.h"
#include "kprintf.h"
#include "errno.h"

static char blockBuffer[4096];
struct Superblock superblock;
unsigned int Uint[1024];
struct Inode rootInode;

struct File{
	int in_use;
	struct Inode ino;
	int flags;
	int offset;
	int size;
};

struct File file_table[MAX_FILES];

int isBusy(){
    //return busy bit
    return *STATUS& (1<<24); 
}

void disk_init(){
	*POWER = 3;
	*CLOCK = 8;
	*CMD = (1 << 10);
	*CMD = 8 | (1<<10);
	*CMD = 55 | (1<<10) | (1<<6);
	*ARG = 0xffffffff;
	*CMD = 41 | (1<<10) | (1<<6);
	do
	{
		*CMD = 55 | (1<<10) | (1<<6);
		*ARG = 0xffffffff;
		*CMD = 41 | (1<<10) | (1<<6);
	} while(isBusy());

	*CMD = 2 | (1<<10) | (1<<6) | (1<<7);
	*CMD = 3 | (1<<10) | (1<<6); 
	unsigned relative_address = *RESPONSE;
	*ARG = relative_address; *CMD = 7 | (1<<10) | (1<<6);
	read_block(0, blockBuffer);
	kmemcopy(&superblock, blockBuffer + 1024, sizeof(superblock));
	
}

void disk_read_sector(unsigned sector_number, void* datablock)
{
	*DATA_LENGTH = 512;
	*DATA_TIMER = 100;
	*DATA_CONTROL = 1 | (1<<1) | (9<<4);
	*ARG = 512*sector_number;
	*CLEAR=0x3ff;
	*CMD = 17 | (1<<10) | (1<<6);
	unsigned* p = (unsigned*)datablock;
	int k;
	for(k=0;k<128;++k)
	{
		while( *STATUS & (1<<19) )
			;

		*CLEAR = 0x3ff;
		unsigned v = *DATA_FIFO;
		*p++ = v;
	}
}
void disk_write_sector(unsigned sector, void* datablock)
{
	*DATA_LENGTH = 512; 
	*DATA_TIMER = 100;
	*DATA_CONTROL = 1 | (9<<4);
	*ARG = 512*sector;     
	*CLEAR=0x3ff; //clear status flags
	//do the write
	*CMD = 24 | (1<<10) | (1<<6); 
	unsigned* p = (unsigned*)datablock;  //data to write
	int k;
	for(k=0;k<128;++k)
	{
		//wait until buffer is empty
		while( (*STATUS & (1<<20)) )
			;
		*CLEAR = 0x3ff;
		*DATA_FIFO = *p;
		p++;
	}
}
      
void read_block(unsigned sector, void* datablock){
	for(int i = 0;i<8;i++){
		disk_read_sector((sector*8)+i, datablock+(512*i));
	}
}

int kdiv(int numey, int denom)
{
	int quota = 0;
	int TUB = 0;
	for(int i = 0; i < 32; i++)
	{
		TUB <<= 1;
		if(numey &(0x80000000 >> i)) TUB |= 1;
		if(TUB >= denom)
		{
			TUB -= denom;
			quota <<= 1;
			quota |= 1;
		}
		else
		{
			quota <<= 1;
		}
	}
	return quota;
}

int kmod(int n, int d)
{
	int quota = 0;
	int TUB = 0;
	for(int i = 0; i < 32; i++)
	{
		TUB <<= 1;
		if(n &(0x80000000 >> i)) TUB |= 1;
		if(TUB >= d)
		{
			TUB -= d;
			quota <<= 1;
			quota |= 1;
		}
		else
		{
			quota <<= 1;
		}
	}
	return TUB;
}

void disk_read_inode(unsigned num, struct Inode* ino){
	//kprintf("%d", num);
	num = num - 1;
	//kprintf("Want inode %d ipg=%d\n",num,sb.inodes_per_group);
	int grp = kdiv(num, superblock.inodes_per_group);
	//kprintf("Group %d\n",grp);
	int blk = superblock.blocks_per_group * grp + 4;
	int inodes_to_skip = kmod(num, superblock.inodes_per_group);
	int bytes_to_skip = inodes_to_skip * sizeof(struct Inode);
	int blocks_to_skip = kdiv(bytes_to_skip,4096);
	//kprintf("blk=%d, its=%d, bts=%d blts=%d\n",blk,inodes_to_skip,bytes_to_skip,blocks_to_skip);

	blk += blocks_to_skip;
	//inodes_to_skip -= kmod(bytes_to_skip,(kdiv(4096,sizeof(struct Inode))));
	disk_read_block_partial(blk,ino,inodes_to_skip*sizeof(struct Inode), sizeof(struct Inode));
}

void disk_read_block_partial(unsigned block, void* vp, unsigned start, unsigned count){
	static char buffer[4096];
	for(int i = 0;i < 8; i++)
	{
		disk_read_sector(block*8+i, buffer+(512*i));
	}
	kmemcopy(vp, buffer+start, count);
}

void disk_read_block(unsigned bl, void* vp, int start, int count)
{
	static char buff[4096];
	for(int i = 0; i < 1; i++)
	{
		disk_read_sector(1, buff);
	}
	kmemcopy(vp, buff, count);
}

int kmemcmp(void* a, void* b, int n)
{
	char* ap = (char*)a;
	char* bp = (char*)b;
	while(n > 0)
	{
		if(*ap < *bp) {return -1;}
		else if(*ap > *bp) {return 1;}		
		ap++;
		bp++;		
		n--;
	}
	return 0;
}

int kstrlen(const char* s)
{
	int n = 0;
	while(*s)
	{
		n++;
		s++;
	}
	return n;
}

int disk_get_file_inode(unsigned dir_inode, const char* fname){
    int fname_length = kstrlen(fname);
    
    read_block( 4, blockBuffer );
    struct Inode* I = (struct Inode*) blockBuffer;
    struct Inode ino;
    kmemcopy(&ino, &(I[1]), sizeof(struct Inode));
    
    for(int i = 0;i<12 ;i++){
        read_block( ino.direct[i] , blockBuffer );
        char*p = blockBuffer;
        struct DirEntry* de = (struct DirEntry*) p;
        while(de->rec_len){
            de = (struct DirEntry*) p;
            if(de->name_len == fname_length){
                if(kmemcmp((void*)fname, de->name, fname_length) == 0){
                    //kprintf("< %d> %.*s\n", de->inode, de->name_len, de->name);
                    return de->inode;
                }
            }
            p += de->rec_len;
        }
    }
    return 0;
}

int file_open(const char *fname, int flags){
	
	for(int i=0;i<MAX_FILES;i++){
		
		if(file_table[i].in_use == 0){
			int inodeNo = disk_get_file_inode(2, fname);
			//kprintf("Inode number: %d", inodeNo);
			if(inodeNo == 0){
				return -ENDENT;
			}else{
				file_table[i].in_use = 1;
				file_table[i].flags = flags;

				disk_read_inode(inodeNo,&file_table[i].ino);
				return i;
			}
		}
		
	}return -EMFILE;
}

int file_close(int fd)
{
	if(file_table[fd].in_use == 1)
	{
		file_table[fd].in_use = 0;
		return(SUCCESS);
	}
	else
	{
		return(-ENDENT);
	}
}

int file_read(int fd, void* buf, int count){
    struct File* fp = &file_table[fd];
    if(fd < 0 || fd >= MAX_FILES)
        return -EINVAL;
    if((fp->in_use == 0) || (count <= 0) || (fp->offset >= fp->ino.size))
	{
        return 0;
	}
    int bi = kdiv(fp->offset, BYTES_PER_BLOCK);
    
    
    if(bi < 12){
        read_block(fp->ino.direct[bi], blockBuffer);
		kmemcopy(buf, blockBuffer, count);
        //return ;
    }else{
        bi-=12;
        if(bi<1024){
            read_block(fp->ino.indirect, Uint);
            read_block(Uint[bi], blockBuffer);
			kmemcopy(buf, blockBuffer, count);
            //return U[bi]; how much data is given
        }else{
            bi-=1024;
            if(bi<(1024*1024)){
                read_block(fp->ino.doubleindirect, Uint);
                read_block(Uint[bi>>10], Uint);
                read_block(Uint[bi&0x3ff], blockBuffer);
				kmemcopy(buf,blockBuffer,count);
                //return U[bi];
            }
            else{
                bi -= (1024*1024);
                read_block(fp->ino.tripleindirect, Uint);
                read_block(Uint[bi>>20], Uint);
                read_block(Uint[(bi>>10)&0x3ff], Uint);
                read_block(Uint[bi&0x3ff], buf);
				kmemcopy(buf,blockBuffer,count);
                //return U[bi];
            }
        }
    }
    int bo = kmod(fp->offset, BYTES_PER_BLOCK);
    unsigned remaining = BYTES_PER_BLOCK - bo;
    
    int min_fsize = (remaining < count? remaining : count);
    if(min_fsize > (fp->ino.size)-(fp->offset))
        min_fsize = (fp->ino.size) - (fp->offset);
    
    fp->offset += min_fsize;
    kmemcopy( buf, blockBuffer + bo ,min_fsize);
    fp->size = min_fsize;
    return min_fsize;
}

int file_write(int fd, const void* buffer, int count){
    return -ENOSYS; //no such system call
}

int file_seek(int fd, int offset, int whence){
	struct File* fp = &file_table[fd];
	if(fd < 0 || fd >= MAX_FILES){
		return -EINVAL;
	}
	if(fp->in_use == 0){
		return -ENDENT;
	}
	
	if(whence == SEEK_SET){
		if(offset < 0){
			return -EINVAL;
		}
		else{
			fp->offset = offset;
			return 0;
		}
	}
	
	else if(whence == SEEK_CUR){
		if((fp->offset + offset)<0){
			return -EINVAL;
		}
		else{
			fp->offset += offset;
			return 0;
		}
	}
	
	else if(whence == SEEK_END){
		if(offset + (int)(fp->ino.size)<0){
			return -EINVAL;
		}
		else{
			fp->offset = fp->ino.size + offset;
			
			return 0;
			
		}
	}
	return -EINVAL;
}