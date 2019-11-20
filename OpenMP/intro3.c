#include <stdio.h>
#include <omp.h>

int main(int argc, char* argv[]) {
  // Memoria compatida (TODO LO QUE ESTA AFUERA DE PRAGMA)
  printf("Using the share clause\n");
  int x = 1;
  #pragma omp parallel shared(x) num_threads(3)
  {
    x++;
    printf("In the parallel block x is %i\n", x);
  }
  x = 2;
  #pragma omp parallel private(x) num_threads(3)
  {
    x++;
    printf("In the parallel block x is %i\n", x);
  }
  printf("Outside the parallel block, x is %i\n", x);
  return 0;
}
