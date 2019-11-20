// Code to calculate the standard deviation of a set of values
#include <stdio.h>
#include <stdlib.h>
#include "utils/cheader.h"
#include <omp.h>

define SIZE 1000000000;

double sum_array(int* array, int size) {
  double acum = 0;
  int i;
  #pragma omp parallel for shared(array, size) reduction(+: acum)
  for(i = 0; i < size; i++) {
    acum += array[i];
  }
  return acum;
}

int main(int argc, char* argv[]) {
  int i, j, *a;
  double ms, result;

  a = (int * )malloc(sizeof(int) * SIZE);
  fill_array(a, SIZE);
  display_array("a", a);

  printf("Starting...\n");
  ms = 0;
  for(i = 0; i < 10; i++) {
    start_time();
    result = sum_array(a, SIZE);
  }

}
