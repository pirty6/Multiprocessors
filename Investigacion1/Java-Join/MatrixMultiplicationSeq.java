/*----------------------------------------------------------------

* Multiprocesadores: Sequential matrix multiplication

* Fecha: 26-Sep-2015

* Autor: A01205559 Roberto Nuñez

* X = 1_000, Y = 2_000
  Speedup =  10142.2 / 8869.5 = 1.14349174

*--------------------------------------------------------------*/
import java.util.Arrays;

public class MatrixMultiplicationSeq {
  private int[][] matrixA;
  private int[][] matrixB;
  private int[][] matrixC;

  public MatrixMultiplicationSeq(int[][] matrixA, int[][] matrixB) {
    this.matrixA = matrixA;
    this.matrixB = matrixB;
    this.matrixC = new int[matrixA.length][matrixB[0].length];
  }

  private void multiply() {
    if(matrixA.length != matrixB[0].length) {
      System.out.println("Matrix multiplication cannot be performed");
      return;
    }
    for(int i = 0; i < matrixA.length; i++) {
      for(int j = 0; j < matrixB[0].length; j++) {
        matrixC[i][j] = 0;
        for(int k = 0; k < matrixA[0].length; k++) {
          matrixC[i][j] += (matrixA[i][k] * matrixB[k][j]);
        }
      }
    }
  }

  private int[][] getResult() {
    return matrixC;
  }

  public static void main(String args[]) {
    double acum = 0;
    long startTime, stopTime;
    final int X = 1_000;
    final int Y = 2_000;
    int[][] matrixA = new int[X][Y];
    int[][] matrixB = new int[Y][X];

    // int[][] matrixA = {{1,-1,1},{2,2,3},{-2,-3,-1}};
    // int[][] matrixB = {{1,0,4},{0,2,5},{1,3,0}};

    for(int i = 0; i < X; i++) {
      Utils.fillArray(matrixA[i]);
    }

    for(int i = 0; i < Y; i++) {
      Utils.fillArray(matrixB[i]);
    }

    System.out.println("Matrix A:");
    for(int i = 0; i < X; i++) {
      System.out.println(Arrays.toString(matrixA[i]));
    }

    System.out.println("Matrix B");
    for(int i = 0; i < Y; i++) {
      System.out.println(Arrays.toString(matrixB[i]));
    }

    System.out.println("Starting...");
    MatrixMultiplicationSeq obj = new MatrixMultiplicationSeq(matrixA, matrixB);
    for(int i = 0; i < 10; i++) {
      startTime = System.currentTimeMillis();
      obj.multiply();
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    int[][] matrixC = obj.getResult();
    System.out.println("Result:");
    for(int i = 0; i < X; i++) {
      System.out.println(Arrays.toString(matrixC[i]));
    }
    System.out.println("avg time = " + (acum / 10));
  }
}
