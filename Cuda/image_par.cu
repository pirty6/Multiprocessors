/*----------------------------------------------------------------

*

* Multiprocesadores: Cuda

* Fecha: 11-Nov-2019

* Autor: A01206747 Mariana Perez
  Autor: A01205559 Roberto Nuñez

* Image = 1080 x 1920
  Speedup =  33.93700 ms / 0.00250 ms  = 13.5748

*--------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <opencv/highgui.h>
#include "cuda_runtime.h"
#include "cheader.h"

__global__ void grayscale(unsigned char *src, unsigned char *dest, int width,
                          int height, int nChannels) {
  int y = blockIdx.y * blockDim.y + threadIdx.y;
  int x = blockIdx.x * blockDim.x + threadIdx.x;

  if(y < height && x < width) {
    int pos = (y * width + x) * nChannels;

    unsigned char r = (float)src[pos];
    unsigned char g = (float)src[pos + 1];
    unsigned char b = (float)src[pos + 2];
    dest[pos] = dest[pos + 1] = dest[pos + 2] = (unsigned char)0.2126 * r + 0.7152 * g + 0.0722 * b;
  }
}

int main(int argc, char* argv[]) {
  int i, size;
	double acum;
	unsigned char *dev_src, *dev_dest;
  Timer t;

	if (argc != 2) {
		printf("usage: %s source_file\n", argv[0]);
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
	cvShowImage("Grayscale", dest);
	cvWaitKey(0);
	cvDestroyWindow("Original");
	cvDestroyWindow("Grayscale");
  cvSaveImage("out.jpg", dest);

	return 0;
}
