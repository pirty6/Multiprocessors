#include <iostream>
#include "cppheader.h"
#include <tbb/task_scheduler_init.h>
#include <tbb/parallel_reduce.h>
#include <tbb/blocked_range.h>

// Reduce es para un reductive task
// Parallel for puede ser anonimo porque no regresa nada

using namespace std;
using namespace tbb;

const long NUM_RECTS = 100000000;
const long GRAIN = 100000;

class calculatingPi {
private:
  double sum;

public:
  calculatingPi(): sum(0) {}

  // Constructor para tbb para separar la tarea en tareas mas pequeñas
  calculatingPi(calculatingPi &x, split) : sum(0) {}

  void operator() (const blocked_range<int> & r) {
    double mid, height, width;
    sum = 0;
    width = 1.0 / (double) NUM_RECTS;
    for(int i = r.begin(); i != r.end(); i++) {
      mid = (i + 0.5) * width;
      height = 4.0 / (1.0 + (mid * mid));
      sum += height;
    }
  }

  void join(const calculatingPi &x) {
    sum += x.sum();
  }
};

int main(int argc, char* argv[]) {
  Timer t;
  double ms, area;
  cout << "Starting..." << endl;
  ms = 0;
  for(int i = 0; i < 10; i++) {
    t.start();
    calculatingPi cp;
    parallel_reduce(blocked_range(0, NUM_RECTS, GRAIN), cp);
    area = cp.sum * (1.0 / (double) NUM_RECTS);
    ms += t.stop();
  }
  cout << "PI = " << area << endl;
  cout << "avg time = " << (ms / 10) << " ms" << endl;
  return 0;
}
