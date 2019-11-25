//Program to calculate the factorial of a number using threads
import java.math.BigInteger;

//Crear thread
public class FactorialThreads extends Thread {
    private BigInteger result;
    private int start, end;

    public FactorialThreads(int start, int end) {
      // Pasar los parametros mediante objeto en lugar de funcion run
      this.start = start;
      this.end = end;
      this.result = BigInteger.ONE;
    }

    public BigInteger getResult() {
      return result;
    }

    // Metodo run que es void y no recibe parametros NO SE PUEDE CAMBIAR
    public void run() {
      result = BigInteger.ONE;
      for(int i = start; i <= end; i++) {
        result = result.multiply(BigInteger.valueOf(i));
      }
    }

    //Crear la carga de trabajo para los threads
    public static void main(String args[]) {
      final int NUM = 100_000;
      FactorialThreads threads[];
      // Tamaño del bloque
      int block;
      long startTime, stopTime;
      double acum;
      BigInteger result = BigInteger.ONE;

      threads = new FactorialThreads[Runtime.getRuntime().availableProcessors()];
      block = NUM / Runtime.getRuntime().availableProcessors();

      acum = 0;
      for(int i = 0; i < 10; i++) {
        // Crear threads
        for(int j = 0; j < threads.length; j++) {
          // Asignarle trabajo al thread
          if(j != threads.length - 1) {
            threads[j] = new FactorialThreads((j + 1) * block, (j + 2) * block);
          } else {
            threads[j] = new FactorialThreads((j + 1) * block, NUM);
          }
        }

        startTime = System.currentTimeMillis();

        // Empezar los threads
        for(int j = 0; j < threads.length; j++) {
          threads[j].start();
        }

        //Esperar que todos los threads hayan terminado
        for(int j = 0; j < threads.length; j++) {
          try {
            threads[j].join();
          } catch(InterruptedException ie) {
            ie.printStackTrace();
          }
        }

        //Recolectar los resultados de los threads
        result = BigInteger.ONE;
        for(int j = 0; j < threads.length; j++) {
          result = result.multiply(threads[j].getResult());
        }

        stopTime = System.currentTimeMillis();
        acum += (stopTime - startTime);
      }
    
      System.out.println("result = " + result);
      System.out.println("avg time = " + (acum / 10));
    }
}
