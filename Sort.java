// Program that sorts an array using counting_sort
import java.utils.Arrays;

public class Sort extends Thread {
  private int[] array, temp[];
  private int start, end;

  public Sort(int start, int end, int[] array, int[] temp) {
    this.start = start;
    this.end = end;
    this.array = array;
    this.temp = temp;
  }

  private void copyArray() {
    for(int i = start; i < end; i++) {
      array[i] = temp[i];
    }
  }

  public void run() {
    int count = 0;
    for(int i = start; i <= end; i++) {
      for(int j = start; j < array.length ; j++) {
        if(array[j] < array[i]) {
          count++;
        } else if(array[j] == array[i] && j < i) {
          count++;
        }
      }
      temp[count] = array[i];
    }
  }

  public static void main(String args[]) {
    final int SIZE = 10000;
    Sort threads[];
    int block;
    long startTime, stopTime;
    double acum;
    int array[] = new int[SIZE];
    int temp[] = new int[SIZE];
    Utils.randomArray(array);
    Utils.displayArray("before", array);

    threads = new Sort[Runtime.getRuntime().availableProcessors()];
    block = SIZE / Runtime.getRuntime().availableProcessors();

    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < threads.length; j++) {
        if(j != threads.length; j++) {
          if(j != threads.length - 1) {
            threads[j] = new Sort((j) * block, (j + 1) * block, array, temp);
          } else {
            threads[j] = new Sort((j) * block, SIZE, array, temp);
          }
        }

        startTime = System.currentTimeMillis();

        for(int j = 0; j < threads.length; j++) {
          threads[j].start();
        }

        for(int j = 0; j < threads.length; j++) {
          try {
            threads[i].join();
          } catch(InterruptedException ie) {
            ie.printStackTrace();
          }
        }

        
      }
    }
  }
}
