#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils/cheader.h"

#define SIZE 1000000

void counting_sort(int* array, int size, int copy) {
  int* temp = (int*) malloc(sizeof(int) * size);
  int i, j, count;

  #pragma omp parallel for shared(array, temp, size) private(j, count)
  for(i = 0; i < size; i++) {
    count = 0;
    for(j = 0; j < size; j++) {
      if(array[j] < array[i]) {
        count++;
      } else if(array[i] == array[j] && j < i) {
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

int main(int argc, char* argv[]) {
}
