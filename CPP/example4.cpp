#include <iostream>
#include <cmath>
#include "utils/cppheader.h";

const int SIZE = 100000000;

class Deviation {
  private:
    int *myData, mySize;
    double result;

  public:
    Deviation(int* data, int size) : myData(data), mySize(size), result(0) {};

    void calculate() {
      double avg, acum;
      acum = 0;
      for(int i = 0; i < mySize; i++) {
        acum += myData[i];
      }
      avg = acum / mySize;
      acum = 0;
      for(int i = 0; i < mySize; i++) {

      }
    }
};

int main(int argc, char* argv[]) {
  Timer t;
  double ms;
  int* array = new int[SIZE];
  random_array(array, SIZE);
  display_array("array", array);
  Deviation d(array, SIZE);
  cout << "Starting...\n";
  ms = 0;
  for(int i = 0; i < N; i++) {
    t.start();
    d.calculate();
    ms += t.stop();
  }
  cout << "result = " << d.getResult() << "\n";
  cout << "avg time = " << (ms / N) << " ms" << "\n";
  delete[] array;
  return 0;
}
