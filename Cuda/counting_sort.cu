#include <stdio.h>
#include <cuda_runtime.h>

#define SIZE 1000000

__global__ void counting_sort(int* array, int *temp, int size) {
  int i, j, count;
  i = threadIdx.x + (blockIdx.x * blockDim.x);
  if (i < size) {
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
}

int main(int argc, char* argv[]) {
  int i, *array;
  int *d_array, *d_temp;

  double ms;

  array = (int*) malloc(sizeof(int) * SIZE);
  random_array(array, SIZE);
  display_array("before", array);

  cudaMalloc((void**) &d_array, SIZE * sizeof(int));
  cudaMalloc((void**) &d_temp, SIZE * sizeof(int));

  cudaMemcpy(d_array, array, SIZE * sizeof(int), cudaMemcpyHostToDevice);

  printf("Starting...\n");
  for(i = 0; i < 10; i++) {
    start_timer();
    counting_sort<<<SIZE / THREADS, THREADS>>>(d_array, d_temp, SIZE);
    ms += stop_timer();
  }

  cudaMemcpy(array, d_temp, SIZE * sizeof(int), cudaMemcpyDeviceToHost);
  display_array("after", array);

  printf("avg time = %5ld ms\n", (ms / N));
  cudaFree(d_array); cudaFree(d_temp);
  free(array);
  return 0;
}
