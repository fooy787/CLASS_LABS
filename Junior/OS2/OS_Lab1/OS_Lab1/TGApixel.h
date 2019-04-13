#pragma once
#include "stdafx.h"
#include <stdint.h>

#pragma pack(push, 1)
struct TGAHeader {
	uint8_t idSize;
	uint8_t colorType;
	uint8_t compression;
	uint8_t colormap[5];
	uint16_t origin[2];
	uint16_t w,h;
	uint8_t bpp;
	uint8_t descriptor;
};
#pragma pack(pop)

struct pixel { uint8_t b, g, r; };