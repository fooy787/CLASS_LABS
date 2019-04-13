#pragma once

#define va_arg(b, t) (*(t*)real_va_arg(&b,  sizeof(t)))
#define va_start(v, x) real_va_start(&v, &x, sizeof(x))

typedef struct va_list{
	char* q;
} va_list;

static void real_va_start(va_list * v, void * p, int sz)
{
	char* c = (char*)p;
	v-> q = c + sz;
}

static char* real_va_arg(va_list* v, int SZ)
{
	char* temp = v-> q;
	v->q += SZ;
	
	return temp;
}

static void va_end(va_list v) {}