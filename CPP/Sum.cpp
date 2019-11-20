#include <iostream>
#include <cmath>
#include "cppheader.h"
#include <tbb/task_scheduler_init.h>
#include <tbb/parallel_reduce.h>
#include <tbb/blocked_range.h>

using namespace std;
using namespace tbb;

const long SIZE 100000000
const long GRAIN 100000

class SumArray {
private:
  int *array;

public:
  double result;

  SumArray(int* array) : array(array), result(0) {}
  SumArray(SumArray &x, split) : array(x.array), result(0) {}

  void operator() (const blocked_range<int> &r) {
    result = 0;
    for(int i = r.begin(); i != r.end(); i++) {
      result += array[i];
    }
  }

  void join(const SumArray &x) {
      result += x.result;
  }
};

int main(int argc, char* argv[]) {
  long result = 0;
  double ms;
  int *a;
  a = new int[SIZE];
  fill_array(a, SIZE);
  display_array("a", a);
  ms = 0;
  cout << "Starting..." << endl;
  for(int i = 0; i < 10; i++) {
    t.start();
    SumArray obj(a);
    parallel_reduce(blocked_range<int>(0, SIZE, GRAIN), obj);
    result = obk.result;
    ms += t.stop();
  }
  cout << "sum = " << result << endl;
  cout <<"avg time = " << (ms / 10) << " ms" << endl;
  delete[] a;
  return 0;

}
