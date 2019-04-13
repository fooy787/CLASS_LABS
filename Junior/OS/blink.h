#pragma once
#include "interrupt.h"
#include "kprintf.h"
#define WIDTH 800
#define HEIGHT 600
#define framebuffer ((volatile unsigned char*)((0x7ffffff - (WIDTH * HEIGHT * 2))&0xffffff0))

void set_pixel (int x, int y, short color1, short color2);