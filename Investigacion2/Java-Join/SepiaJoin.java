/*----------------------------------------------------------------

* Multiprocesadores: RGB to Sepia

* Fecha: 3-Dic-2019

* Autor: A01206747 Mariana Perez

* Image dimension = 1920 x 1080
  Image size = 1.6MB

  Speedup =  31.10000 / 10.60000 = 2.93396226

*--------------------------------------------------------------*/

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class SepiaJoin extends RecursiveAction {
  private static final int MIN = 10_000;
  private int[] src;
  private int[] dest;
  private int start, end, width, height;

  public SepiaJoin(int start, int end, int[] src, int[] dest, int width, int height) {
    this.start = start;
    this.end = end;
    this.src = src;
    this.dest = dest;
    this.width = width;
    this.height = height;
  }

  protected void computeDirectly() {
    int i;
    int ren, col;
    for (i = start; i < end; i++) {
      ren = i / width;
      col = i % width;
      setPixel(ren, col);
    }
  }

  private void setPixel(int ren, int col) {
    int pixel = src[(ren * width) + col];

    float r = (float) ((pixel & 0x00ff0000) >> 16);
    float g = (float) ((pixel & 0x0000ff00) >> 8);
    float b = (float) ((pixel & 0x000000ff) >> 0);

    float tr = (float)(0.393 * r + 0.769 * g + 0.189 * b);
    float tg = (float)(0.349 * r + 0.686 * g + 0.168 * b);
    float tb = (float)(0.272 * r + 0.534 * g + 0.131 * b);

    tr = (tr > 255) ? 255 : tr;
    tg = (tg > 255) ? 255 : tg;
    tb = (tb > 255) ? 255 : tb;

    int color = (0xff000000)
    | ((int)(tr) << 16)
    | ((int)(tg) << 8)
    | ((int)(tb));

    dest[(ren * width) + col] = color;
  }

  @Override
  protected void compute() {
    if((end - start) <= MIN) {
      computeDirectly();
    } else {
      int mid = start + ((end - start) / 2);
      invokeAll(
        new SepiaJoin(start, mid, src, dest, width, height),
        new SepiaJoin(mid, end, src, dest, width, height)
      );
    }
  }

  public static void main(String args[]) throws Exception {
    long startTime, stopTime;
    double acum = 0;
    if(args.length != 1) {
      System.out.println("Usage: java SepiaSeq [image_file]");
      System.exit(-1);
    }

    final String filename = args[0];
    File srcFile = new File(filename);
    final BufferedImage source = ImageIO.read(srcFile);
    int w = source.getWidth();
    int h = source.getHeight();
    int src[] = source.getRGB(0, 0, w, h, null, 0, w);
    int dest[] = new int[src.length];

    ForkJoinPool pool;
    for(int i = 0; i < 10; i++) {
      startTime = System.currentTimeMillis();
      pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
      pool.invoke(new SepiaJoin(0, w * h, src, dest, w, h));
      stopTime = System.currentTimeMillis();
      acum += (stopTime - startTime);
    }
    System.out.printf("avg time = %.5f\n", (acum / 10));
		final BufferedImage destination = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		destination.setRGB(0, 0, w, h, dest, 0, w);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageFrame.showImage("Original - " + filename, source);
            }
    });

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageFrame.showImage("Blur - " + filename, destination);
            }
    });
  }
}
