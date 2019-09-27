/*----------------------------------------------------------------

*

* Multiprocesadores: Fork-Join

* Fecha: 26-Sep-2015

* Autor: A01206747 Mariana Perez
  Autor: A01205559 Roberto Nuñez

* I = 10_000, J = 10_000
  Speedup =  31.0 / 26.2 = 1.183

  I = 10_000 J = 1_000
  Speedup = 4.4/5.2 = 0.846

*--------------------------------------------------------------*/
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class VectorMultJoin extends RecursiveAction {
  private static final int MIN = 5_000;
  private int[] vector;
  private int[][] matrix;
  private int[] result;
  private int start, end;

  public VectorMultJoin(int start, int end, int[] vector, int[][]matrix, int[] result) {
    this.start = start;
    this.end = end;
    this.vector = vector;
    this.matrix = matrix;
    this.result = result;
  }

  private void computeDirectly() {
    int col = matrix[0].length;

    for(int i = start; i < end; i++) {
      int sum = 0;
      for(int j = 0; j < col; j++) {
        sum += matrix[i][j] * vector[j];
      }
      result[i] = sum;
    }
  }

  @Override
  protected void compute() {
    if((end - start) <= MIN) {
      computeDirectly();
    } else {
      int mid = (end + start) / 2;
      invokeAll(
      new VectorMultJoin(start, mid, vector, matrix, result),
      new VectorMultJoin(mid, end, vector, matrix, result)
      );
    }
  }

  public static void main(String args[]) {
    ForkJoinPool pool;
    long startTime, stopTime;
    double acum = 0;

    final int I = 10_000;
    final int J = 1_000;
    int[] vector = new int[I];

    int[][] matrix = new int[J][I];

    //Utils.randomArray(vector);
    Utils.fillArray(vector);
    System.out.println("Vector:");
    System.out.println(Arrays.toString(vector));
    System.out.println("Matrix:");
    for(int i = 0; i < matrix.length; i++) {
      //Utils.randomArray(matrix[i]);
      Utils.fillArray(matrix[i]);
      System.out.println(Arrays.toString(matrix[i]));
    }
    /*int[] vector = {-6, -2, 5};
    int[][] matrix = {{-3, 5, -6}, {7, 10, -1}};*/
    /*int[] vector = {2, -3, 4, -1};
    int[][] matrix = {{-3, 0, 3, 2}, {1, 7, -1, 9}};*/
    int[] result = new int[matrix.length];

    System.out.println("-------------------------Multiplying-------------------------");
    for(int i = 0; i < 10; i++) {
      startTime = System.currentTimeMillis();
      pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
      pool.invoke(new VectorMultJoin(0, matrix.length, vector, matrix, result));
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    System.out.println("result = " + Arrays.toString(result));
    System.out.println("avg time = " + (acum / 10));

  }

}
