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

public class MaxSumSeq {
  private int[] arr;
  private long result;

  public MaxSumSeq(int[] arr) {
    this.arr = arr;
    this.result = Integer.MIN_VALUE;
  }

  public long getResult() {
    return result;
  }

  public void maxSum() {
    for(int i = 0; i < arr.length; i++) {
      long curr_max = 0;
      for(int j = i; j < arr.length; j++) {
        curr_max += arr[j];
        result = Math.max(result, curr_max);
      }
    }
  }

    public static void main(String args[]) {
      final int SIZE = 1_000_000;
      int[] a = new int[SIZE];
      //int [] a = {-1, 6, -2, 5, -1, 4, 3, -4, -3, 1};
      Utils.randomArray(a);
      Utils.displayArray("Array", a);

      long startTime, stopTime;
      double acum = 0;
      MaxSumSeq obj = new MaxSumSeq(a);
      for(int i = 0; i < 10; i++) {
        startTime = System.currentTimeMillis();
        obj.maxSum();
        stopTime = System.currentTimeMillis();
        acum += (stopTime - startTime);
      }

      System.out.println("result = " + obj.getResult());
      System.out.println("avg time = " + (acum / 10));
    }
}
