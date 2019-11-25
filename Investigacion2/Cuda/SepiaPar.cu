/*----------------------------------------------------------------

*

* Multiprocesadores: Cuda

* Fecha: 3-Dic-2019

* Autor: A01205559 Roberto Nuñez

* Image = 1080 x 1920
  Speedup =  104.82820 ms / 0.00520 ms  = 20159.2692

*--------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <opencv/highgui.h>
#include "cuda_runtime.h"
#include "cppheader.h"

__global__ void grayscale(unsigned char *src, unsigned char *dest, int width,
                          int height, int nChannels) {
  int y = blockIdx.y * blockDim.y + threadIdx.y;
  int x = blockIdx.x * blockDim.x + threadIdx.x;

  if(y < height && x < width) {
    int pos = (y * width + x) * nChannels;

    float r = src[pos + 2];
    float g = src[pos + 1];
    float b = src[pos + 0];

    dest[pos + 2] = ((0.393f * r + 0.769f * g + 0.189f * b) > 255) ? 255 : (0.393f * r + 0.769f * g + 0.189f * b);
    dest[pos + 1] = ((0.349f * r + 0.686f * g + 0.168f * b) > 255) ? 255 : (0.349f * r + 0.686f * g + 0.168f * b);
    dest[pos + 0] = ((0.272f * r + 0.534f * g + 0.131f * b) > 255) ? 255 : (0.272f * r + 0.534f * g + 0.131f * b);
  }
}

int main(int argc, char* argv[]) {
  int i, size;
	double acum;
	unsigned char *dev_src, *dev_dest;
  Timer t;

	if (argc != 2) {
		printf("usage: %s [dir_image_source]\n", argv[0]);
		return -1;
	}

	IplImage *src = cvLoadImage(argv[1], CV_LOAD_IMAGE_COLOR);
	IplImage *dest = cvCreateImage(cvSize(src->width, src->height), IPL_DEPTH_8U, 3);
	if (!src) {
		printf("Could not load image file: %s\n", argv[1]);
		return -1;
	}

	size = src->width * src->height * src->nChannels * sizeof(uchar);
	cudaMalloc((void**) &dev_src, size);
	cudaMalloc((void**) &dev_dest, size);

	cudaMemcpy(dev_src, src->imageData, size, cudaMemcpyHostToDevice);

  dim3 dimGrid(ceil((float)src->width / 16),
               ceil((float)src->height / 16));
  dim3 dimBlock(16, 16, 1);

	acum = 0;
	for (i = 0; i < 10; i++) {
		t.start();
		grayscale<<<dimGrid, dimBlock>>>(dev_src, dev_dest, src->width, src->height, src->nChannels);
		acum += t.stop();
	}

	cudaMemcpy(dest->imageData, dev_dest, size, cudaMemcpyDeviceToHost);

	cudaFree(dev_dest);
	cudaFree(dev_src);

	printf("avg time = %.5lf ms\n", (acum / 10));

	cvShowImage("Original", src);
	cvShowImage("Sepia", dest);
	cvWaitKey(0);
	cvDestroyWindow("Original");
	cvDestroyWindow("Sepia");

	return 0;
}
