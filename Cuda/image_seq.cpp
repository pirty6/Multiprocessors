#include <stdio.h>
#include <stdlib.h>
#include <opencv/highgui.h>
#include "cheader.h"

void grayscale(IplImage * src, IplImage *dest) {
  double r,g,b, l;
  int pos;
  for(int i = 0; i < src->width; i++) {
    for(int j = 0; j < src->height; j++) {
      pos = (j * src->width + i) * src->nChannels;
      r = (double)src->imageData[pos];
      g = (double)src->imageData[pos + 1];
      b = (double)src->imageData[pos + 2];
      l = 0.2126 * r + 0.7152 * g + 0.0722 * b;
      dest->imageData[pos] = l;
      dest->imageData[pos + 1] = l;
      dest->imageData[pos + 2] = l;
    }
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
  int acum = 0;
  for(int i = 0; i < 10; i++) {
    t.start();
    grayscale(src, dest);
    acum += t.stop();
  }

  printf("Average time = %.5d ms\n", (acum / 10));

  cvShowImage("Original", src);
  cvShowImage("Grayscale", dest);
  cvWaitKey(0);
  cvDestroyWindow("Original");
  cvDestroyWindow("Grayscale");

  return 0;
}
