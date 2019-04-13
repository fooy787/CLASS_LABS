// SSE3.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include "ImageReaders.h"
#include "Stopwatch.h"

#include <intrin.h>
#include <immintrin.h>

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int main(int argc, char* argv[])
{
	using std::cout;

	Targa img(argv[1]);
	const TargaHeader& H = img.header();
	__m128i* p = (__m128i*)img.data();
	unsigned numPixels = H.width*H.height;
	unsigned bytesPerPixel = H.bitsPerPixel / 8;
	vector<__m128i> outPix;
	
	outPix.reserve(H.width*H.height * 3);
	__m128i* outP = (__m128i*)outPix.data();
	Stopwatch sw;
	int numBytes = H.height*H.width * 4;

	alignas(16)uint8_t mask1[16] = { 0, 1, 2, 4, 5, 6, 8, 9, 10, 12, 13, 14, 255, 255, 255, 255};
	alignas(16)uint8_t mask2[16] = { 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 0, 1, 2, 4};
	alignas(16)uint8_t mask3[16] = { 5, 6, 8, 9, 10, 12, 13, 14, 255, 255, 255, 255, 255, 255, 255, 255};
	alignas(16)uint8_t mask4[16] = { 255, 255, 255, 255, 255, 255, 255, 255, 0, 1, 2, 4, 5, 6, 8, 9,};
	alignas(16)uint8_t mask5[16] = { 10, 12, 13, 14, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255 };
	alignas(16)uint8_t mask6[16] = { 255, 255, 255, 255, 0, 1, 2, 4, 5, 6, 8, 9, 10, 12, 13, 14 };

	__m128i Mask1 = _mm_loadu_si128((__m128i*)mask1);
	__m128i Mask2 = _mm_loadu_si128((__m128i*)mask2);
	__m128i Mask3 = _mm_loadu_si128((__m128i*)mask3);
	__m128i Mask4 = _mm_loadu_si128((__m128i*)mask4);
	__m128i Mask5 = _mm_loadu_si128((__m128i*)mask5);
	__m128i Mask6 = _mm_loadu_si128((__m128i*)mask6);
	
	sw.start();
	int j = 0;
	if (H.descriptor == 8 | H.descriptor == 32) 
	{
		for (unsigned i = 0; i < (H.height * H.width) / 16; i++, p += 4, outP += 3) {

			__m128i pixIn1 = _mm_loadu_si128((__m128i*) (p));
			__m128i pixIn2 = _mm_loadu_si128((__m128i*) (p + 1));
			__m128i pixIn3 = _mm_loadu_si128((__m128i*) (p + 2));
			__m128i pixIn4 = _mm_loadu_si128((__m128i*) (p + 3));
			__m128i temp1 = _mm_shuffle_epi8(pixIn1, Mask1);
			__m128i temp2 = _mm_shuffle_epi8(pixIn2, Mask2);
			__m128i temp3 = _mm_shuffle_epi8(pixIn2, Mask3);
			__m128i temp4 = _mm_shuffle_epi8(pixIn3, Mask4);
			__m128i temp5 = _mm_shuffle_epi8(pixIn3, Mask5);
			__m128i temp6 = _mm_shuffle_epi8(pixIn4, Mask6);
			__m128i out1 = _mm_or_si128(temp1, temp2);
			__m128i out2 = _mm_or_si128(temp3, temp4);
			__m128i out3 = _mm_or_si128(temp5, temp6);

			_mm_storeu_si128((__m128i*) (outP), out1);
			_mm_storeu_si128((__m128i*) (outP + 1), out2);
			_mm_storeu_si128((__m128i*) (outP + 2), out3);
		}
		sw.stop();
		cout << "Time: " << sw.elapsed_us() << " microseconds\n";
		Bitmap outbmp(H.width, H.height, outPix.data());
		outbmp.write("out.bmp");
	}
	else
	{
		sw.stop();
		cout << "Time: " << sw.elapsed_us() << " microseconds\n";
		Bitmap outbmp(H.width, H.height, img.data());
		outbmp.write("out.bmp");
	}
	
	
	return 0;
}