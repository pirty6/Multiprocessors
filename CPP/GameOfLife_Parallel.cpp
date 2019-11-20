/*----------------------------------------------------------------

*

* Multiprocesadores: Fork-Join

* Fecha: 18-Oct-2019

* Autor: A01206747 Mariana Perez
  Autor: A01205559 Roberto Nuñez

* I = 10_000, J = 10_000
  Speedup =  6026.22 / 1109.09 = 5.4334815

*--------------------------------------------------------------*/

#include <iostream>
#include "cppheader.h"
#include <tbb/blocked_range.h>
#include <tbb/task_scheduler_init.h>
#include <tbb/parallel_for.h>

using namespace std;
using namespace tbb;

const int GRAIN  = 10;
const long ROWS = 10000;
const long COLS = 10000;

class GameOfLife {
private:
  int **matrix;
  int **result;

public:
  GameOfLife(int **matrix, int **result): matrix(matrix), result(result) {}

  void operator() (const blocked_range<int> &r) const{
    for(int i = r.begin(); i != r.end(); i++) {
      for(int j = 1; j < COLS - 1; j++) {
        int alive = 0;
        for(int k = -1; k <= 1; k++) {
          for(int l = -1; l <= 1; l++) {
            if(((i + k) < 0) || ((i + k) > (ROWS - 1))) continue;
            if(((j + l) < 0) || ((j + l) > (COLS - 1))) continue;
            alive += matrix[i + k][j + l];
          }
        }
        alive -= matrix[i][j];
        if(matrix[i][j] == 1 && alive < 2) {
          result[i][j] = 0;
        } else if(matrix[i][j] == 1 && alive > 3) {
          result[i][j] = 0;
        } else if(matrix[i][j] == 0 && alive == 3) {
          result[i][j] = 1;
        } else {
          result[i][j] = matrix[i][j];
        }
      }
    }
  }

};

int main(int argc, char* argv[]) {
  Timer t;
  double ms;
  int** a = new int*[ROWS];
  for(int i = 0; i < ROWS; i++) {
    a[i] = new int[COLS];
  }
  int** result = new int*[ROWS];
  for(int i = 0; i < ROWS; i++) {
    result[i] = new int[COLS];
  }
  for(int i = 0; i < ROWS; i++) {
    fill(a[i], COLS);
  }
  for(int i = 0; i < ROWS; i++) {
    for(int j = 0; j < COLS; j++) {
      cout << a[i][j] << " ";
    }
    cout << endl;
  }
  ms = 0;
  cout << "Starting..." << endl;
  for(int i = 0; i < 10; i++) {
    t.start();
    parallel_for(blocked_range<int> (0, ROWS, GRAIN), GameOfLife(a, result));
    ms += t.stop();
  }

  for(int i = 0; i < ROWS; i++) {
    for(int j = 0; j < COLS; j++) {
      cout << result[i][j] << " ";
    }
    cout << "\n";
  }
  cout << "avg time = " << (ms / N) << " ms" << endl;
  for(int i = 0; i < ROWS; i++) {
    delete[] a[i];
  }
  delete [] a;
  for(int i = 0; i < ROWS; i++) {
    delete[] result[i];
  }
  delete [] result;
  return 0;
}
