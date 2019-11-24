import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class SepiaJoin extends RecursiveAction {
  private static final int MIN = 1_000;
  private int start, end, cols;
  private BufferedImage img;

  public SepiaJoin(int start, int end, int cols, BufferedImage img) {
    this.start = start;
    this.end = end;
    this.cols = cols;
    this.img = img;
  }

  protected void computeDirectly() {
    for (int i = start; i < end; i++) {
      for (int j = 0; j < cols; j++) {
        int rgb = img.getRGB(j, i);

        int red = rgb & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb >> 16) & 0xFF;

        float L = (float) (0.2126 * (float) red + 0.7152 * (float) green + 0.0722 * (float) blue);

        int color;
        color = 234 * (int) L / 255;
        color = (color << 8) | 176 * (int) L / 255;
        color = (color << 8) | 3 * (int) L / 255;

        img.setRGB(j, i, color);
      }
    }
  }

  @Override
  protected void compute() {
    if((end - start) <= MIN) {
      computeDirectly();
    } else {
      int mid = (end - start) / 2;
      invokeAll(
        new SepiaJoin(start, mid, cols, img),
        new SepiaJoin(mid, end, cols, img)
      );
    }
  }

  public static void main(String[] args) throws IOException {
    if(args.length != 1) {
      System.out.println("Usage: java SepiaSeq [src_image]");
      return;
    }
    BufferedImage img = ImageIO.read(new File(args[0]));
    ForkJoinPool pool;
    int rows = img.getHeight();
    int cols = img.getWidth();
    long startTime, stopTime;
    double acum = 0;

    for(int i = 0; i < 1; i++) {
      startTime = System.currentTimeMillis();
      pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
      pool.invoke(new SepiaJoin(0, rows, cols, img));
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    ImageIO.write(img, "jpg", new File("./sepia_copy"));
    System.out.println("avg time = " + (acum / 10) + "ms");
    System.out.println("Finished");
  }
}
