#include "stdafx.h"
#include "Wave.h"
#include <intrin.h>
#include <iostream>
#include "Stopwatch.h"

using namespace std;

int main(int argc, char* argv[])
{
	string filename = argv[1];
	alignas(16) float decay = atof(argv[2]);
	alignas(16) float delay = atof(argv[3]);

	__m128 decayX = _mm_load1_ps(&decay);
	__m128 delayX = _mm_load1_ps(&delay);

	Wave w(filename);
	if (w.format.format != Wave::FormatCode::FLOAT) {
		cout << "Not a float wave\n";
		return 1;
	}
	Stopwatch swatch;
	swatch.start();
	unsigned totalFloats = w.numFrames * w.format.bytesPerFrame / 4;
	unsigned totalXMMs = totalFloats / 4;
	float* f = (float*)w.data();
	float delayAmount = delay * w.format.samplesPerSecond;
	int newFloatSize = delay * w.format.samplesPerSecond + totalFloats;
	float* delayedData = new float[newFloatSize];
	for (int i = 0; i < delayAmount; i++)
	{
		delayedData[i] = 0.0f;
	}

	__m128 toBegHist = _mm_load_ps(delayedData);
	int counter = 0;
	for (unsigned i = 0; i<totalXMMs; i++) {
		__m128 cur = _mm_load_ps(f);
		if (counter >= delayAmount)
		{
			delayedData -= counter;
			counter = 0;
		}
		
		__m128 next = _mm_load_ps(delayedData);

		next = _mm_mul_ps(next, decayX);
		__m128 newF = _mm_add_ps(cur, next);
		_mm_store_ps(f, newF);
		_mm_store_ps(delayedData, newF);
		f += 4;
		delayedData += 4;
		counter += 4;
	}
	swatch.stop();
	cout << swatch.elapsed_us() << "usec\n";

	w.write("outx.wav");

	return 0;
}