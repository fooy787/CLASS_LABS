// SSE8.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "Wave.h"
#include <intrin.h>
#include <iostream>
#include "Stopwatch.h"
#include <immintrin.h>

using namespace std;

int main(int argc, char* argv[])
{
	string filename = argv[1];

	Wave w(filename);
	if (w.format.format != Wave::FormatCode::FLOAT) {
		cout << "Not a float wave\n";
		return 1;
	}
	Stopwatch swatch;
	swatch.start();
	unsigned totalFloats = w.numFrames * w.format.bytesPerFrame / 4;
	int newSize = (int)(totalFloats / 2);

	unsigned totalYMMs = totalFloats / 16;

	Wave wout(w.format, (int)(w.numFrames / 2) + 1);


	alignas(32)float* f = (float*)w.data();
	alignas(32)float* f2 = (float*)wout.data();
	alignas(32)int maskArray[8] = { 0,2,4,6,0,0,0,0 };
	alignas(32)int maskArray2[8] = { 0,0,0,0,0,2,4,6 };
	__m256i mask = _mm256_loadu_si256((__m256i*)maskArray);
	__m256i mask2 = _mm256_loadu_si256((__m256i*)maskArray2);

	__m256i maskYMM = _mm256_load_si256(&mask);
	__m256i maskYMM2 = _mm256_load_si256(&mask2);
	for (unsigned i = 0; i<totalYMMs; i++) {
		__m256 in = _mm256_load_ps(f);

		__m256 in2 = _mm256_load_ps(f + 8);

		__m256 tmpOut = _mm256_permutevar8x32_ps(in, maskYMM);
		__m256 tmpOut2 = _mm256_permutevar8x32_ps(in2, maskYMM2);
		__m256 newOut = _mm256_blend_ps(tmpOut, tmpOut2, 240);
		_mm256_storeu_ps(f2, newOut);
		f += 16;
		f2 += 8;
	}
	swatch.stop();
	cout << swatch.elapsed_us() << "usec\n";
	wout.write("outw.wav");
	return 0;
}
