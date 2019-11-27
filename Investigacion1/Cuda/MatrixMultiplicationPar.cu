/*----------------------------------------------------------------

* Multiprocesadores: Cuda matrix multiplication

* Fecha: 26-Sep-2015

* Autor: A01205559 Roberto Nuñez

* X = 1_000, Y = 2_000
  Speedup =  1392.76660  / 0.00310 = 535679.462

*--------------------------------------------------------------*/

 #include "cuda_runtime.h"
 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include "cheader.h"

 #define X 1000
 #define Y 2000
 #define BLOCK_SIZE 1


 __global__ void multiply(int* a, int* b, int* c, int x, int y) {
   int row = blockIdx.y * blockDim.y + threadIdx.y;
   int col = blockIdx.x * blockDim.x + threadIdx.x;

   int temp =  0;
   if(row < x && col < x) {
     for(int i = 0; i < y; i++) {
       temp += a[row * y + i] * b[i * x + col];
     }
   }
   c[row * x + col] = temp;
 }

int main() {
  double acum = 0;
  int* matrixA;
  int* matrixB;
  // int matrixA[X * Y] = {1,-1,1,2,2,3,-2,-3,-1};
  // int matrixB[Y * X] = {1,0,4,0,2,5,1,3,0};
  // int matrixA[X * Y] = {1,2,-3,4,0,-2};
  // int matrixB[Y * X] = {3,1,2,4,-1,5};

  int* matrixC;
  int* d_matrixA, *d_matrixB, *d_matrixC;

  matrixA = (int*)malloc(sizeof(int) * X * Y);
  matrixB = (int*)malloc(sizeof(int) * X * Y);
  matrixC = (int*)malloc(sizeof(int) * X * X);

  for(int i = 0; i < Y * X; i++) {
    matrixA[i] = (i % Y) + 1;
  }

  for(int i = 0; i < Y * X; i++) {
    matrixB[i] = (i % X) + 1;
  }

  printf("Matrix A:\n");
  for(int i = 0; i < X * Y; i++) {
    if(i % Y == 0) printf("\n");
    printf("%i ", matrixA[i]);
  }
  printf("\n");
  printf("Matrix B:\n");
  for(int i = 0; i < Y * X; i++) {
    if(i % X == 0) printf("\n");
    printf("%i ", matrixB[i]);
  }
  printf("\n");

  cudaMalloc((void**)&d_matrixA, sizeof(int) * X * Y);
  cudaMalloc((void**)&d_matrixB, sizeof(int) * X * Y);
  cudaMalloc((void**)&d_matrixC, sizeof(int) * X * X);

  cudaMemcpy(d_matrixA, matrixA, sizeof(int) * X * Y, cudaMemcpyHostToDevice);
  cudaMemcpy(d_matrixB, matrixB, sizeof(int) * X * Y, cudaMemcpyHostToDevice);

  unsigned int grid_rows = (X + BLOCK_SIZE - 1) / BLOCK_SIZE;
  unsigned int grid_cols = (Y + BLOCK_SIZE - 1) / BLOCK_SIZE;
  dim3 dimGrid(grid_cols, grid_rows);
  dim3 dimBlock(BLOCK_SIZE, BLOCK_SIZE);

  for(int i = 0; i < 10; i++) {
    start_timer();
    multiply<<<dimGrid,dimBlock>>>(d_matrixA, d_matrixB, d_matrixC, X, Y);
    acum += stop_timer();
  }

  cudaMemcpy(matrixC, d_matrixC, sizeof(int) * X * X, cudaMemcpyDeviceToHost);
  cudaThreadSynchronize();
  printf("Result:\n");
  for(int i = 0; i < X * X; i++) {
    if(i % X == 0) printf("\n");
    printf("%i ", matrixC[i]);
  }
  printf("\n");

  printf("avg time = %.5f ms\n", (acum / 10));
  cudaFree(d_matrixA);
  cudaFree(d_matrixB);
  cudaFree(d_matrixC);

  free(matrixA);
  free(matrixB);
  free(matrixC);

  return 0;
}
