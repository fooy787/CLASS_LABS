// SSE2_nonSimD.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "Image.h"
#include <iostream>
#include <intrin.h>
#include "Stopwatch.h"
#include <cstdio>

int main(int argc, char* argv[])
{
	Image* mImage = new Image(argv[1]);
	int brightnessAmount = atoi(argv[2]);
	uint8_t* curPixel = mImage->pixels();
	int mHeight = mImage->height();
	int mWidth = mImage->width();
	Stopwatch swatch;
	swatch.start();
	for (int i = 0; i < mHeight; i++)
	{
		for (int j = 0; j < mWidth; j++)
		{
			*curPixel = min(max(*curPixel + brightnessAmount, 0),255);
			curPixel++;
			*curPixel = min(max(*curPixel + brightnessAmount, 0), 255);
			curPixel++;
			*curPixel = min(max(*curPixel + brightnessAmount, 0), 255);
			curPixel += 2;
		}
	}
	swatch.stop();

	mImage->writePng("out.png");
	std::cout << swatch.elapsed_us() << " usec\nHit enter to exit.";
	std::getchar();
    return 0;
}

