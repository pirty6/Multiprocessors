#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils/cheader.h"

#define SIZE 10000

// Program that parallelises the algorith counting sort
void counting_sort(int *array, int size, int copy) {
  int *temp = (int*) malloc(sizeof(int) * size);
  int i, j, count;
  for(int i = 0; i < size; i++) {
    count = 0;
    for(int j = 0; j < size; j++) {
      if(array[j] < array[i]) {
        count++;
      } else if(array[j] == array[i] && j < i) {
        count++;
      }
    }
    temp[count] = array[i];
  }
  if(copy) {
    memcpy(array, temp, sizeof(int) * size);
  }
  free(temp);
}

int main(int argc, char* argv) {
  int i, *array;
  double ms;
  array = (int*) malloc(sizeof(int) * SIZE);
  random_array(array, SIZE);
  display_array("before", array);
  printf("Starting...\n");
  for(i = 0; i < N; i++) {
    start_timer();
    counting_sort(array, SIZE, (i == (N - 1)));
    ms += stop_timer();
  }
  display_array("after", array);
  printf("avg time = %.5lf ms\n", (ms / N));
  return 0;
}
