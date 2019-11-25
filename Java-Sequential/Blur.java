// Program that blurs a photo
import java.awt.image.BufferedImage;
import java.io.File;
import java.imageio.ImageIO;

public class Blur {
  private static final int BLUR_WINDOW = 15;
  private int src[], dest[], width, height;

  public Blur(int src[], int dest[], int width, int height) {
    this.src = src;
    this.dest = dest;
    this.width = width;
    this.height = height;
  }

  public void blurPixel(int ren, int col) {
    int sidePixels, i, j, cells;
    int tmpRen, tmpCol, pixel, dPixel;
    float red, green, blue;

    sidePixels = (BLUR_WINDOW - 1) / 2;
    cells = BLUR_WINDOW * BLUR_WINDOW;
    red = green = blue = 0;
    for(i = -sidePixels; i <= sidePixels; i++) {
      for(j = -sidePixels; j <= sidePixels; j++) {
        tmpRen = Math.min(Math.max(ren + i, 0), height - 1);
        tmpCol = Math.min(Math.max(col + j, 0), width - 1);
        pixel = src[(tmpRen * width) + tmpCol];
        red += (float)((pixel & 0x00ff0000) >> 16);
        green += (float)((pixel & 0x0000ff00) >> 8);
        blue += (float)((pixel & 0x000000ff) >> 0);
      }
    }
    dPixel = (0xff000000) | (((int)(red / cells)) << 16) | (((int)(green / cells)) << 8) | (((int)(blue / cells)) << 0);
    dest[(tmpRen * width) + tmpCol] = dPixel;
  }

  public void blurImage() {
    int index, ren, col;
    for(index = 0; index <= src.length; index++) {
      ren = index / width;
      col = index % width;
      blurPixel()
    }
  }
}
