/*----------------------------------------------------------------

* Multiprocesadores: Sequential matrix multiplication

* Fecha: 26-Sep-2015

* Autor: A01205559 Roberto Nuñez

* X = 1_000, Y = 2_000
  Speedup =  10142.2 / 1993.6 = 5.08737961

*--------------------------------------------------------------*/
import java.util.Arrays;

public class MatrixMultiplicationThreads extends Thread {
  private int[][] matrixA;
  private int[][] matrixB;
  private int[][] matrixC;
  private int start, end;

  public MatrixMultiplicationThreads(int start, int end, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
    this.start = start;
    this.end = end;
    this.matrixA = matrixA;
    this.matrixB = matrixB;
    this.matrixC = matrixC;
  }

  public void run() {
    int col = matrixA[0].length;
    for(int i = start; i < end; i++) {
      for(int j = 0; j < matrixB[0].length; j++) {
        matrixC[i][j] = 0;
        for(int k = 0; k < matrixA[0].length; k++) {
          matrixC[i][j] += (matrixA[i][k] * matrixB[k][j]);
        }
      }
    }
  }

  public static void main(String args[]) {
    MatrixMultiplicationThreads threads[];
    int block;
    double acum = 0;
    long startTime, stopTime;
    final int X = 1_000;
    final int Y = 2_000;

    threads = new MatrixMultiplicationThreads[Runtime.getRuntime().availableProcessors()];
    block = X / Runtime.getRuntime().availableProcessors();

    int[][] matrixA = new int[X][Y];
    int[][] matrixB = new int[Y][X];
    // int[][] matrixA = {{1,-1,1},{2,2,3},{-2,-3,-1}};
    // int[][] matrixB = {{1,0,4},{0,2,5},{1,3,0}};
    // int[][] matrixA = {{1,2,-3},{4,0,-2}};
    // int[][] matrixB = {{3,1},{2,4},{-1,5}};
    int[][] matrixC = new int[X][X];

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
    System.out.println("Matrix B:");
    for(int i = 0; i < Y; i++) {
      System.out.println(Arrays.toString(matrixB[i]));
    }
    System.out.println("Starting");

    for (int j = 0; j < 10; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new MatrixMultiplicationThreads((i * block), ((i + 1) * block), matrixA, matrixB, matrixC);
				} else {
					threads[i] = new MatrixMultiplicationThreads((i * block), X, matrixA, matrixB, matrixC);
				}
			}

			startTime = System.currentTimeMillis();
			for (int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			for (int i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stopTime = System.currentTimeMillis();
			acum +=  (stopTime - startTime);
		}

    System.out.println("Result:");
    for(int i = 0; i < X; i++) {
      System.out.println(Arrays.toString(matrixC[i]));
    }
    System.out.println("avg time = " + (acum / 10));
  }
}
