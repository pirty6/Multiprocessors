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
#include "cheader.h"

void grayscale_pixel(IplImage * src, IplImage *dest, int ren, int col) {
  int pos;
  int step = src->widthStep / sizeof(uchar);
  pos = (ren * step) + (col * src->nChannels);
  unsigned char r = (float)src->imageData[pos];
  unsigned char g = (float)src->imageData[pos + 1];
  unsigned char b = (float)src->imageData[pos + 2];
  dest->imageData[pos] = dest->imageData[pos + 1] = dest->imageData[pos + 2] =
  (unsigned char) 0.2126 * r + 0.7152 * g + 0.0722 * b;

}

void grayscale(IplImage* src, IplImage* dest) {
  int size = src->width * src->height;
  int row, col;
  for(int i = 0; i < size; i++) {
    row = i / src->width;
    col = i % src ->width;
    grayscale_pixel(src, dest, row, col);
  }
}

int main(int argc, char* argv[]) {
  if(argc != 2) {
    printf("Usage: %s [dir_image_source]\n", argv[0]);
    return -1;
  }

  IplImage *src = cvLoadImage(argv[1], CV_LOAD_IMAGE_COLOR);
	IplImage *dest = cvCreateImage(cvSize(src->width, src->height), IPL_DEPTH_8U, 3);
	if (!src) {
		printf("Could not load image file: %s\n", argv[1]);
		return -1;
	}
  Timer t;
  double acum = 0;
  for(int i = 0; i < 10; i++) {
    t.start();
    grayscale(src, dest);
    acum += t.stop();
  }

  printf("avg time = %.5lf ms\n", (acum / 10));

  cvShowImage("Original", src);
  cvShowImage("Grayscale", dest);
  cvWaitKey(0);
  cvDestroyWindow("Original");
  cvDestroyWindow("Grayscale");

  return 0;
}
