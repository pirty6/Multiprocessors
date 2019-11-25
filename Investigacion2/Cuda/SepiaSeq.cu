/*----------------------------------------------------------------

*

* Multiprocesadores: Cuda

* Fecha: 3-Dic-2019

* Autor: A01206747 Mariana Perez

* Image = 1080 x 1920
  Speedup =  104.82820 ms / 0.00520 ms  = 20159.2692

*--------------------------------------------------------------*/


#include <stdio.h>
#include <stdlib.h>
#include <opencv/highgui.h>
#include "cppheader.h"

void set_pixel(IplImage * src, IplImage *dest, int ren, int col) {
  int pos;
  int step = src->widthStep / sizeof(uchar);
  pos = (ren * step) + (col * src->nChannels);
  unsigned char r = (float)src->imageData[pos + 2];
  unsigned char g = (float)src->imageData[pos + 1];
  unsigned char b = (float)src->imageData[pos + 0];
  dest->imageData[pos + 2] = (unsigned char) (((0.393f * r + 0.769f * g + 0.189f * b) > 255) ? 255 : (0.393f * r + 0.769f * g + 0.189f * b));
  dest->imageData[pos + 1] = (unsigned char) (((0.349f * r + 0.686f * g + 0.168f * b) > 255) ? 255 : (0.349f * r + 0.686f * g + 0.168f * b));
  dest->imageData[pos + 0] = (unsigned char) (((0.272f * r + 0.534f * g + 0.131f * b) > 255) ? 255 : (0.272f * r + 0.534f * g + 0.131f * b));

}

void grayscale(IplImage* src, IplImage* dest) {
  int size = src->width * src->height;
  int row, col;
  for(int i = 0; i < size; i++) {
    row = i / src->width;
    col = i % src ->width;
    set_pixel(src, dest, row, col);
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
  cvShowImage("Sepia", dest);
  cvWaitKey(0);
  cvDestroyWindow("Original");
  cvDestroyWindow("Sepia");

  return 0;
}
