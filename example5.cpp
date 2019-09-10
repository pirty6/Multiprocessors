#include <iostream>
#include "utils/cppheader.h"

const int SIZE = 10000000;

using namespace std;

class SumArray {
private:
  int *myData, mySize;
  long result;

public:
  SumArray(int* data, int size) : myData(data), mySize(size) {}

  void calculate() {
    result = 0;
    for(int i = 0; i < mySize; i++) {
      result += myData[i];
    }
  }

  long getResult() const {
    return result;
  }
};

int main(int argc, char* argv[]) {
  Timer t;
  double ms;
  int* array = new int[SIZE];
  fill_array(array, SIZE);
  display_array("array", array);

  SumArray sa(array, SIZE);
  cout << "Starting...\n";
  ms = 0;
  for(int i = 0; i < N; i++) {
    t.start();
    sa.calculate();
    ms += t.stop();
  }
  cout << "sum = " << sa.getResult() << "\n";
  cout << "avg time = " << (ms/N) << "ms\n";

  delete [] array;
  return 0;
}
