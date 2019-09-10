// Program that calculates the factorial of a number using BigInteger
import java.math.BigInteger;

public class Factorial {
  private BigInteger result;
  private int n;

  public Factorial(int n){
    this.n = n;
    this.result = BigInteger.ONE;
  }

  public BigInteger getResult() {
    return result;
  }

  public void calculate() {
    result = BigInteger.valueOf(1);
    for(int i = 1; i <= n; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
  }

  public static void main(String args[]) {
    int N = 10;
    double ms = 0, start, end;
    Factorial obj = new Factorial(100_000);
    for(int i = 0; i < N; i++) {
      start = System.currentTimeMillis();
      obj.calculate();
      end = System.currentTimeMillis();
      ms += (end - start);
    }
    System.out.println("result = " + obj.getResult());
    System.out.println("avg time = " + (ms/N));
  }
}
