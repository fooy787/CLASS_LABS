
#include "syscalls.h"

int syscall0(int p0){
    register unsigned r0 asm ("r0");
    r0=p0;
    asm volatile("svc #0" : "+r"(r0) : : "memory","cc");
    return r0;
}

int syscall1(int p0, int p1){
    register unsigned r0 asm  ("r0");
    register unsigned r1 asm  ("r1");
    r0=p0;
    r1=p1;
    asm volatile("svc #0" : "+r"(r0),"+r"(r1) : : "memory","cc");
    return r0;
}

int syscall2(int p0, int p1, int p2){
    register unsigned r0 asm  ("r0");
    register unsigned r1 asm  ("r1");
    register unsigned r2 asm  ("r2");
    r0=p0;
    r1=p1;
    r2=p2;
    asm volatile("svc #0" : "+r"(r0),"+r"(r1), "+r"(r2) : : "memory","cc");
    return r0;
}

int syscall3(int p0, int p1, int p2, int p3){
    register unsigned r0 asm  ("r0");
    register unsigned r1 asm  ("r1");
    register unsigned r2 asm  ("r2");
    register unsigned r3 asm  ("r3");
    r0=p0;
    r1=p1;
    r2=p2;
    r3=p3;
    asm volatile("svc #0" : "+r"(r0),"+r"(r1), "+r"(r2), "+r"(r3) : : "memory","cc");
    return r0;
}

int open(const char* filename){
    return syscall2( SYSCALL_OPEN, (unsigned)filename, 0 );
}

int read(int fd, char* buf, int size){
    return syscall3(SYSCALL_READ,fd,(unsigned)buf,size);
}

int write(int fd, char* buf, int size){
    return syscall3(SYSCALL_WRITE,fd,(unsigned)buf,size);
}


unsigned div(unsigned numerator, unsigned denominator){
    int i;
    unsigned accumulator=0;
    unsigned quotient=0;
    for(i=0;i<32;++i){
        accumulator = (accumulator<<1) | ((numerator & (1 << (31-i))) ? 1:0);
        quotient = (quotient<<1) | ( (accumulator>=denominator) ? 1:0 );
        accumulator -= (accumulator>=denominator) ? denominator:0;
    }
    return quotient;
}

  
unsigned mod(unsigned numerator, unsigned denominator){
    int i;
    unsigned accumulator=0;
    unsigned quotient=0;
    for(i=0;i<32;++i){
        accumulator = (accumulator<<1) | ((numerator & (1 << (31-i))) ? 1:0);
        quotient = (quotient<<1) | ( (accumulator>=denominator) ? 1:0 );
        accumulator -= (accumulator>=denominator) ? denominator:0;
    }
    return accumulator;
}


char* itoc(unsigned value, int width, char* buf){
    int places;
    char* p=buf;
    for(places=0;places < width || value>0;places++){
         *p = mod(value,10) + '0';
         value  = div(value,10);
         p++;
    }
    *p = 0;
    char* rv = p;
    
    p--;
    char* q = buf;
    while(p>q){
        char tmp = *p;
        *p = *q;
        *q = tmp;
        p--;
        q++;
    }
    return rv;
}
    
int main(){
    char buf[128];
    char numbuff[5];
    while(1){
        write(1,"--> ",4);
        int nr = read(0,buf,sizeof(buf));
        write(1,"You typed ",10);
        itoc(nr,sizeof(numbuff)-1,numbuff);
        write(1,numbuff,sizeof(numbuff)-1);
        write(1," characters: ",13);
        write(1,buf,nr);
        write(1,"\n",1);
    }
}