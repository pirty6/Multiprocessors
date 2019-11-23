/*----------------------------------------------------------------

* Multiprocesadores: C matrix multiplication

* Fecha: 26-Sep-2015

* Autor: A01205559 Roberto Nuñez

* X = 1_000, Y = 2_000
  Speedup =  1392.76660  / 0.00260 = 535679.462

*--------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "cheader.h"

#define X 1000
#define Y 2000

void multiply(int** matrixA, int** matrixB, int** matrixC) {
  for(int i = 0; i < X; i++) {
    for(int j = 0; j < X; j++) {
      matrixC[i][j] = 0;
      for(int k = 0; k < Y; k++) {
        matrixC[i][j] += (matrixA[i][k] * matrixB[k][j]);
      }
    }
  }
}

int main(int argc, char* argv[]) {
  double acum;

  int** matrixA = (int**)malloc(X * sizeof(int*));
  for(int i = 0; i < X; i++) matrixA[i] = (int*)malloc(sizeof(int) * Y);
  int** matrixB = (int**)malloc(Y * sizeof(int*));
  for(int i = 0; i < Y; i++) matrixB[i] = (int*)malloc(sizeof(int) * X);
  // int matrixA[X][Y] = {{1,-1,1},{2,2,3},{-2,-3,-1}};
  // int matrixB[Y][X] = {{1,0,4},{0,2,5},{1,3,0}};
  // int matrixA[X][Y] = {{1,2,-3},{4,0,-2}};
  // int matrixB[Y][X] = {{3,1},{2,4},{-1,5}};
  int** matrixC = (int**)malloc(X * sizeof(int*));
  for(int i = 0; i < X; i++) matrixC[i] = (int*)malloc(sizeof(int) * X);

  for(int i = 0; i < X; i++) {
    fill_array(matrixA[i], Y);
  }
  for(int i = 0; i < Y; i++) {
    fill_array(matrixB[i], X);
  }
  printf("Matrix A:\n");
  for(int i = 0; i < X; i++) {
    for(int j = 0; j < Y; j++) {
      printf("%i ", matrixA[i][j]);
    }
    printf("\n");
  }
  printf("Matrix B:\n");
  for(int i = 0; i < Y; i++) {
    for(int j = 0; j < X; j++) {
      printf("%i ", matrixB[i][j]);
    }
    printf("\n");
  }
  printf("Starting");
  for(int i = 0; i < 10; i++) {
    start_timer();
    multiply(matrixA, matrixB, matrixC);
    acum += stop_timer();
  }
  printf("Result:\n");
  for(int i = 0; i < X; i++) {
    for(int j = 0; j < X; j++) {
      printf("%i ", matrixC[i][j]);
    }
    printf("\n");
  }
  printf("avg time = %.5f ms\n", (acum / 10));
  for(int i = 0; i < X; i++) {
    free(matrixA[i]);
  }
  free(matrixA);
  for(int i = 0; i < Y; i++) {
    free(matrixB[i]);
  }
  free(matrixB);
}
