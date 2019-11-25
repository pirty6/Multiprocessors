// Program that sorts an array using counting_sort
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class SortJoin extends RecursiveAction{
  private static final int MIN = 5_000;
  private int[] array, temp;
  private int start, end;

  public SortJoin(int start, int end, int[] array, int[] temp) {
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

  protected void computeDirectly() {
    int count;
    for(int i = start; i < end; i++) {
      count = 0;
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

  @Override
  protected void compute() {
      if((end - start) <= MIN) {
        computeDirectly();
      } else {
        int mid = (end - start) / 2;
        invokeAll(
          new SortJoin(start, mid, array, temp),
          new SortJoin(mid, end, array, temp)
        );
      }
  }

  public static void main(String args[]) {
    final int SIZE = 10_000;
    ForkJoinPool pool;

    long startTime, stopTime;
    double acum = 0;
    int array[] = new int[SIZE];
    int temp[] = new int[SIZE];
    Utils.randomArray(array);
    Utils.displayArray("before", array);

    for(int i = 0; i < 10; i++) {
        startTime = System.currentTimeMillis();
        pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke(new SortJoin(0, array.length, array, temp));

        stopTime = System.currentTimeMillis();
        acum += (stopTime - startTime);
        pool.shutdown();
      }
      array = Arrays.copyOf(temp, temp.length);
      Utils.displayArray("after", array);
      System.out.println("avg time = " + (acum / 10));
    }
}
