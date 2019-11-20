#include <iostream>
#include "cheader.h"
#include <tbb/blocked_range.h>
#include <tbb/parallel_for.h>
#include <tbb/task_scheduler_init.h>

using namespace std;
using namespace tbb;


const int SIZE = 100000000;
const int GRAIN = 10000;

class AddVectors {
private:
  int *a, *b, *c;

public:
  AddVectors(int* arrayC, int* arrayA, int* arrayB) : c(arrayC), a(arrayA), b(arrayB) {}

  void calculate() {
    for(int i = 0; i < size; i++) {
      c[i] = a[i] + b[i];
    }
  }

  void operator() (const blocked_range <int> &r) const {
    for(int i = r.begin(); i != r.end(); i++) {
      c[i] = a[i] + b[i];
    }
  }
};

int main(int argc, char* argv[]) {
  Timer t;
  double ms;
  int *a = new int[SIZE];
  fill_array(a, SIZE);
  display_array("a", a);

  int *b = new int[SIZE];
  fill_array(b, SIZE);
  display_array("b", b);

  int *c = new int[SIZE];
  fill_array("c", SIZE);

  cout << "Starting..." << endl;
  ms = 0;
  for(int i = 0; i < 10; i++) {
    t.start();
    parallel_for(blocked_range<int>(0, SIZE, GRAIN), AddVectors(c,a,b));
    ms += t.stop();
  }
  display_array("c", c);
  cout << "avg time" <<(ms / 10) << "ms" << endl;
  delete []a;
  delete []b;
  delete []c;
  return 0;
}
