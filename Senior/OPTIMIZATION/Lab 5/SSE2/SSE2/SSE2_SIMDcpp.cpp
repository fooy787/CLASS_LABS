// SSE2_SimD.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "Image.h"
#include <iostream>
#include <intrin.h>
#include <immintrin.h>
#include "Stopwatch.h"
#include <cstdio>

struct alignas(16) XMMInt {
	int v[4];
};


int main(int argc, char* argv[])
{
	Image mImage = Image(argv[1]);
	alignas(16)unsigned char brightnessAmount = std::stoi(argv[2]);
	
	__m128i* curPixel = (__m128i*)mImage.pixels();
	
	int mSize = mImage.height() * mImage.width() * mImage.bytesPerPixel();

	alignas(16) char brightnessArray[16] = { brightnessAmount, brightnessAmount, brightnessAmount, 0, brightnessAmount, brightnessAmount, brightnessAmount, 0, brightnessAmount, brightnessAmount, brightnessAmount, 0, brightnessAmount, brightnessAmount, brightnessAmount, 0 };
	__m128i brightnessXMM = _mm_set_epi8(brightnessArray[3], brightnessArray[2], brightnessArray[1], brightnessArray[0], brightnessArray[7], brightnessArray[6], brightnessArray[5], brightnessArray[4], brightnessArray[11], brightnessArray[10], brightnessArray[9], brightnessArray[8], brightnessArray[15], brightnessArray[14], brightnessArray[13], brightnessArray[12]);

	Stopwatch swatch;
	swatch.start();
	if (std::stoi(argv[2]) > 0) {
		for (int i = 0; i < mSize; i += 16)
		{
			__m128i mCurPixel = _mm_load_si128(curPixel);
			mCurPixel = _mm_adds_epu8(mCurPixel, brightnessXMM);
			_mm_store_si128(curPixel, mCurPixel);

			curPixel++;
		}
	}
	else
	{
		for (int i = 0; i < mSize; i += 16)
		{
			__m128i mCurPixel = _mm_load_si128(curPixel);
			mCurPixel = _mm_subs_epu8(mCurPixel, brightnessXMM);
			_mm_store_si128(curPixel, mCurPixel);

			curPixel++;
		}
	}
	swatch.stop();

	mImage.writePng("out.png");
	std::cout << swatch.elapsed_us() << " usec\nHit enter to exit.";
	std::getchar();
	return 0;
}

