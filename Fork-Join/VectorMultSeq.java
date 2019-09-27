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

public class VectorMultSeq {
  private int[] vector;
  private int[][] matrix;
  private int[] result;

  public VectorMultSeq(int[] vector, int[][] matrix) {
    this.vector = vector;
    this.matrix = matrix;
    this.result = new int[matrix.length];
  }

  public int[] getResult() {
    return result;
  }

  public void multiply() {
    int rows = matrix.length;
    int cols = matrix[0].length;

    for(int i = 0; i < rows; i++) {
      int sum = 0;
      for(int j = 0; j < cols; j++) {
        sum += matrix[i][j] * vector[j];
      }
      result[i] = sum;
    }
  }

  public static void main(String args[]) {
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
    /*int[] vector = {2, -3, 4, -1};
    int[][] matrix = {{-3, 0, 3, 2}, {1, 7, -1, 9}};*/
    /*int[] vector = {-6, -2, 5};
    int[][] matrix = {{-3, 5, -6}, {7, 10, -1}};*/
    System.out.println("-------------------------Multiplying-------------------------");
    long startTime, stopTime;
    double acum = 0;
    VectorMultSeq obj = new VectorMultSeq(vector, matrix);
    for(int i = 0; i < 10; i++) {
      startTime = System.currentTimeMillis();
      obj.multiply();
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    System.out.println("Result");
    System.out.println(Arrays.toString(obj.getResult()));
    System.out.println("avg time = " + (acum / 10));
  }
}
