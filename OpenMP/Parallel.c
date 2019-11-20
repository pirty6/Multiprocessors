/*----------------------------------------------------------------

*

* Multiprocesadores: OpenMP

* Fecha: 7-Oct-2019

* Autor: A01205559 Roberto Nuñez
         A01206747 Mariana Perez

* I = 10_000, J = 10_000
  Speedup = 194.69 / 36.25 = 5.37

  I = 10_000 J = 1_000
  Speedup = 19.96 / 4.25 = 4.69

*--------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include "cheader.h"
#include <omp.h>

#define I 10000
#define J 1000

void multiply(int* vector, int** matrix, int* result) {
  int rows = J;
  int cols = I;

  int i, j, sum;
  #pragma omp parallel for shared(vector, matrix, result) private(j, i, sum)
  for(i = 0; i < rows; i++) {
    sum = 0;
    for(j = 0; j < cols; j++) {
      sum += matrix[i][j] * vector[j];
    }
    result[i] = sum;
  }
}


int main(int argc, char* argv[]) {
  int* vector = (int*)malloc(sizeof(int) * I);
  int** matrix = (int**)malloc(J * sizeof(int*));
  for(int i = 0; i < J; i++) matrix[i] = (int * )malloc(sizeof(int) * I);

  fill_array(vector, I);

  for(int i = 0; i < J; i++) {
    fill_array(matrix[i], I);
  }

  /*int vector[] = {2,-3,4,-1};
  int matrix[J][I] = {{-3, 0, 3, 2}, {1, 7, -1, 9}};*/

  /*int vector[I] = {-6, -2, 5};
  int matrix[J][I] = {{-3, 5, -6}, {7, 10, -1}};*/

  int* result = (int*) malloc(sizeof(int) * J);
  printf("-------------------------Multiplying-------------------------\n");
  double ms = 0;
  for(int i = 0; i < 10; i++) {
    start_timer();
    multiply(vector, matrix, result);
    ms += stop_timer();
  }
  printf("\navg time = %.51f ms\n", (ms / 10));
  for(int i = 0; i < J; i++) {
    printf("%i ", result[i]);
  }
  printf("\n");
  free(vector);
  free(result);
  for(int i = 0; i < J; i++) {
    free(matrix[i]);
  }
  free(matrix);
}
