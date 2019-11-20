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

using namespace std;

const long ROWS = 10000;
const long COLS = 10000;

class GameOfLife {
private:
  int **matrix;
  int **result;

public:
  GameOfLife(int **matrix, int **result) : matrix(matrix), result(result) {}

  void nextGeneration() {
    for(int i = 1; i < ROWS - 1; i++) {
      for(int j = 1; j < COLS - 1; j++) {
        int alive = 0;
        for(int k = -1; k <= 1; k++) {
          for(int l = -1; l <= 1; l++) {
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

  int** getResult() const {
    return result;
  }
};

int main(int argc, char* argv[]) {
  double ms;
  Timer t;
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
  GameOfLife obj(a, result);
  cout << "Starting..." << endl;
  for(int i = 0; i < 10; i++) {
    t.start();
    obj.nextGeneration();
    ms += t.stop();
  }
  result = obj.getResult();
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
