/*----------------------------------------------------------------

*

* Multiprocesadores: Threads en Java.

* Fecha: 9-Sep-2019

* Autor: A01205559 Roberto Nuñez
         A01206747 Mariana Perez

*
  Tamaño de pruebas = 1_000_000
  Speedup = Best_sequential_algorithm/ best_parallel_algorithm
  Speedup = 346722.1 / 6223.4 = 55.71

*--------------------------------------------------------------*/
import java.util.Arrays;

public class MaxSum extends Thread {
  private int [] array;
  private int start, end;
  private long max;
  private int x, y;

  public MaxSum(int start, int end, int[] array) {
    this.start = start;
    this.end = end;
    this.array = array;
    this.max = Integer.MIN_VALUE;
  }

  public long getResult() {
    return max;
  }

  public void run() {
    for(int i = start; i < end; i++) {
      long curr_max = 0;
      for(int j = i; j < end; j++) {
        curr_max += array[j];
        max = Math.max(curr_max, max);
      }
    }
  }

  public static void main(String args[]) {
    final int SIZE = 1_000_000;
    //int[] a = {-2,1,-3,4,-1,2,1,-5,4};
    int[] a = new int[SIZE];
    Utils.randomArray(a);
    Utils.displayArray("Array", a);


    MaxSum threads[];
    int block;
    long startTime, stopTime;
    double acum;
    long result = 0;

    threads = new MaxSum[Runtime.getRuntime().availableProcessors()];
    block = a.length / Runtime.getRuntime().availableProcessors();

    acum = 0;
    for(int i = 0; i < 10; i++) {
       result = Integer.MIN_VALUE;
      for(int j = 0; j < threads.length; j++) {
        if(j != threads.length - 1) {
          threads[j] = new MaxSum((j) * block, (j + 1) * block, a);
        } else {
          threads[j] = new MaxSum((j) * block, a.length, a);
        }
      }

      startTime = System.currentTimeMillis();

      for(int j = 0; j < threads.length; j++) {
        threads[j].start();
      }

      for(int j = 0; j < threads.length; j++) {
        try {
          threads[j].join();
        } catch(InterruptedException ie) {
          ie.printStackTrace();
        }
      }

      for(int j = 0; j < threads.length; j++) {
        result = Math.max(result, threads[j].getResult());
      }

      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);

    }

    System.out.println("result = " + result);
    System.out.println("avg time = " + (acum / 10));

  }
}
