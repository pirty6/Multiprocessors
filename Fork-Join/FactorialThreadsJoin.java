//Program to calculate the factorial of a number using threads
import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

//Crear thread
public class FactorialThreadsJoin extends RecursiveTask<BigInteger> {
    private static final int MIN = 10_000;
    private int start, end;

    public FactorialThreadsJoin(int start, int end) {
      // Pasar los parametros mediante objeto en lugar de funcion run
      this.start = start;
      this.end = end;
    }

    // Metodo run que es void y no recibe parametros NO SE PUEDE CAMBIAR
    public BigInteger computeDirectly() {
      BigInteger result = BigInteger.ONE;
      for(int i = start; i <= end; i++) {
        result = result.multiply(BigInteger.valueOf(i));
      }
      return result;
    }

    @Override
    protected BigInteger compute() {
      if((end - start) <= MIN) {
        return computeDirectly();
      } else {
        int mid = (end + start) / 2;
        FactorialThreadsJoin lowerMid = new FactorialThreadsJoin(start, mid);
        lowerMid.fork();
        FactorialThreadsJoin upperMid = new FactorialThreadsJoin(mid + 1, end);
        return upperMid.compute().multiply(lowerMid.join());
      }
    }

    //Crear la carga de trabajo para los threads
    public static void main(String args[]) {
      final int NUM = 100_000;
      ForkJoinPool pool;

      long startTime, stopTime;
      double acum;
      BigInteger result = BigInteger.ONE;

      acum = 0;
      for(int i = 0; i < 10; i++) {
        startTime = System.currentTimeMillis();
        pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        result = pool.invoke(new FactorialThreadsJoin(1, NUM));
     
        stopTime = System.currentTimeMillis();
        acum += (stopTime - startTime);
      }

      System.out.println("result = " + result);
      System.out.println("avg time = " + (acum / 10));
    }
}
