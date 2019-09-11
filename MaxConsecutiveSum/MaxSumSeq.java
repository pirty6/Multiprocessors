import java.util.Arrays;

public class MaxSumSeq {
  private int[] arr;
  private int result;

  public MaxSumSeq(int[] arr) {
    this.arr = arr;
    this.result = Integer.MIN_VALUE;
  }

  public long getResult() {
    return result;
  }

  public int maxSubArray(int s, int e) {
    if(s == e){
      return arr[s];
    }
    int mid = s + (e - s)/2;
    int leftMaxSum = maxSubArray(s, mid);
    int rightMaxSum = maxSubArray(mid+1, e);

    int sum = 0;
    int leftMidMax =0;
    for (int i = mid; i >=s ; i--) {
        sum += arr[i];
        if(sum>leftMidMax)
            leftMidMax = sum;
    }
    sum = 0;
    int rightMidMax =0;
    for (int i = mid+1; i <= e ; i++) {
        sum += arr [i];
        if(sum>rightMidMax)
            rightMidMax = sum;
    }
    int centerSum = leftMidMax + rightMidMax;
    return result = Math.max(centerSum, Math.max(leftMaxSum, rightMaxSum));
  }

  public static void main(String args[]) {
    final int SIZE = 1_000_000;
    int[] a = new int[SIZE];
    //int [] a = {-1, 6, -2, 5, -1, 4, 3, -4, -3, 1};
    Utils.randomArray(a);
    Utils.displayArray("Array", a);

    long startTime, stopTime;
    double acum = 0;
    int result = 0;
    MaxSumSeq obj = new MaxSumSeq(a);
    for(int i = 0; i < 10; i++) {
      startTime = System.currentTimeMillis();
      obj.maxSubArray(0, a.length - 1);
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    System.out.println("result = " + obj.getResult());
    System.out.println("avg time = " + (acum / 10));
  }
}
