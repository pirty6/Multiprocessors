/*----------------------------------------------------------------

*

* Multiprocesadores: TBB

* Fecha: 3-Dic-2019

* Autor: A01206747 Mariana Perez

* Image = 1080 x 1920
  Speedup =  104.82820 ms / 13.21060 ms  = 7.93515813

*--------------------------------------------------------------*/


#include <stdio.h>
#include <stdlib.h>
#include <opencv/highgui.h>

#include <tbb/blocked_range.h>
#include <tbb/task_scheduler_init.h>
#include <tbb/parallel_for.h>

#include "cppheader.h"

using namespace tbb;

const int GRAIN = 10000;

class SepiaPar {
private:
  IplImage *src;
  IplImage *dest;

  void set_pixel(int ren, int col) const{
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

public:
  SepiaPar(IplImage* source, IplImage* destination) :src(source), dest(destination) {}

  void operator() (const blocked_range<int> &r) const {
    int row, col;
    for(int i = r.begin(); i != r.end(); i++) {
      row = i / src->width;
      col = i % src ->width;
      set_pixel(row, col);
    }
  }

};



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
  int size = src->width * src->height;
  for(int i = 0; i < 10; i++) {
    t.start();
    parallel_for( blocked_range<int>(0, size, GRAIN), SepiaPar(src, dest) );
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
