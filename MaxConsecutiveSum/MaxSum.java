/*----------------------------------------------------------------

*

* Multiprocesadores: Threads en Java.

* Fecha: 9-Sep-2019

* Autor: A01205559 Roberto Nuñez
         A01206747 Mariana Perez

*

*--------------------------------------------------------------*/
import java.util.Arrays;

public class MaxSum extends Thread {
  private int [] array;
  private int start, end;
  private int max;

  public MaxSum(int start, int end, int[] array) {
    this.start = start;
    this.end = end;
    this.array = array;
    this.max = Integer.MIN_VALUE;
  }

  public int getResult() {
    return max;
  }

  public int maxSubArray(int s, int e) {
    if(s == e) {
      return array[s];
    }
    int mid = s + (e - s) / 2;
    int leftMaxSum = maxSubArray(s, mid);
    int rightMaxSum = maxSubArray(mid + 1, e);

    int sum = 0;
    int leftMidMax = 0;
    for(int i = mid; i >= s; i--) {
      sum += array[i];
      if(sum > leftMidMax) {
        leftMidMax = sum;
      }
    }
    sum = 0;
    int rightMidMax = 0;
    for(int i = mid + 1; i <= e; i++) {
      sum += array[i];
      if(sum > rightMidMax) {
        rightMidMax = sum;
      }
    }
    int centerSum = leftMidMax + rightMidMax;
    return max = Math.max(centerSum, Math.max(leftMaxSum, rightMaxSum));
  }

  public void run() {
    System.out.println("Thread " + Thread.currentThread().getId() + " is running");
    maxSubArray(start, end);
  }

  public static void main(String args[]) {
    final int SIZE = 1_000_000;
    int[] a =  {-1, 6, -2, 5, -1, 4, 3, -4, -3, 1};

    /*int[] a = new int[SIZE];
    Utils.randomArray(a);
    Utils.displayArray("Array", a);*/

    MaxSum threads[];
    int block;
    long startTime, stopTime;
    double acum;
    int result = Integer.MIN_VALUE;

    //threads = new MaxSum[3];
    threads = new MaxSum[1];
    block = a.length / Runtime.getRuntime().availableProcessors();

    acum = 0;
    int mid = (0 - a.length - 1) / 2;
    for(int i = 0; i < 10; i++) {
      threads[0] = new MaxSum(1 * block, a.length-1, a);
      //threads[1] = new MaxSum(1 * block, (mid * block) , a);
      //threads[2] = new MaxSum((mid + 1)* block, a.length-1, a);

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
