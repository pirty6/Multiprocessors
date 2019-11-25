//Program that does the maclaurin series

public class Maclaurin {
  private static final int LIMITS = 100_000_000;
  private double x, result;

   public Maclaurin(double x) {
     this.x = x;
     this.result = 0;
   }

   public double getResult() {
     return result;
   }

   public void calculate() {
     double aux1, aux2;

     result = 0;
     for(int i = 0; i < LIMITS; i++) {
       aux1 = (i % 2 == 0) ? 1.0 : -1.0;
       aux2 = (2 * i) + 1;
       result += ((aux1 / aux2) * Math.pow(x, aux2));
     }
   }

   public static void main(String args[]) {
     int N = 10;
     double ms = 0, start, end;
     Maclaurin obj = new Maclaurin(0.99);
     for(int i = 0; i < N; i++) {
       start = System.currentTimeMillis();
       obj.calculate();
       end = System.currentTimeMillis();
       ms += (end - start);
     }
     System.out.println("result = " + obj.getResult() + " <=> " + Math.atan(0.99));
     System.out.println("avg time = " + (ms/N));
   }
}
