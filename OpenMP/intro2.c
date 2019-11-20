#include <stdio.h>
#include <omp.h>

int main(int argc, char* argv[]) {
  // Memoria compatida (TODO LO QUE ESTA AFUERA DE PRAGMA)
  int i = 0;
  #pragma omp parallel
  {
    i++;
    int total = omp_get_num_threads();
    int iam = omp_get_thread_num();
    printf("Hello world!! I am the thread %i from %i threads ==> i = %i\n", iam, total, i);
  }
  return 0;
}
