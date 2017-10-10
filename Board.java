import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Board extends JPanel {
  static private BufferedImage canvas;
  int scale = 1;
  public Board(int X, int Y) {
    canvas = new BufferedImage(X, Y, BufferedImage.TYPE_INT_ARGB);
    fillCanvas(Color.black);
  }

  public Dimension getPreferredSize() {
    return new Dimension(canvas.getWidth(), canvas.getHeight());
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(canvas, null, null);
  }

  public void fillCanvas(Color c) {
    int color = c.getRGB();
    for (int x = 0; x < canvas.getWidth(); ++x) {
      for (int y = 0; y < canvas.getHeight(); ++y) {
        canvas.setRGB(x, y, color);
      }
    }
    repaint();
  }

  public void updateCell(int x, int y, Color c) {
    int color = c.getRGB();
    // 1x1
    if(scale == 1) {
      canvas.setRGB(x, y, color);
    }
    // 2x2
    else if(scale == 2) {
      canvas.setRGB((x*2), (y*2), color);
      canvas.setRGB(((x*2)+1), (y*2), color);
      canvas.setRGB((x*2), ((y*2)+1), color);
      canvas.setRGB(((x*2)+1), ((y*2)+1), color);
    }
    // 3x3 (2x2 with a gap so you can see separation between cells)
    else if(scale == 3) {
      canvas.setRGB((x*3), (y*3), color);
      canvas.setRGB(((x*3)+1), (y*3), color);
      canvas.setRGB((x*3), ((y*3)+1), color);
      canvas.setRGB(((x*3)+1), ((y*3)+1), color);
    }
    // 4x4 (3x3 with a gap so you can see separation between cells)
    else if(scale == 4) {
      canvas.setRGB((x*4), (y*4), color);
      canvas.setRGB(((x*4)+1), (y*4), color);
      canvas.setRGB(((x*4)+2), (y*4), color);
      canvas.setRGB(((x*4)+2), (y*4)+1, color);
      canvas.setRGB((x*4), ((y*4)+1), color);
      canvas.setRGB((x*4), ((y*4)+2), color);
      canvas.setRGB((x*4)+1, ((y*4)+2), color);
      canvas.setRGB(((x*4)+1), ((y*4)+1), color);
      canvas.setRGB(((x*4)+2), ((y*4)+2), color);
    }
    repaint();
  }

  public int readCell(int x, int y) {
    Color c;
    if(scale == 1)
      c = new Color(canvas.getRGB(x, y));
    else if(scale == 2)
      c = new Color(canvas.getRGB(x*2, y*2));
    else if(scale == 3)
      c = new Color(canvas.getRGB(x*3, y*3));
    else if(scale == 4)
      c = new Color(canvas.getRGB(x*4, y*4));
    else
      c = new Color(canvas.getRGB(x, y));
    return c.getRGB();
  }

  public void zoom(int factor) {
    canvas = resize(canvas, (canvas.getWidth() / scale) * factor, (canvas.getHeight() / scale) * factor);
    scale = factor;
    repaint();
  }

  public static BufferedImage resize(BufferedImage src, int w, int h)
  {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    int x, y;
    int ww = src.getWidth();
    int hh = src.getHeight();
    int[] ys = new int[h];
    for (y = 0; y < h; y++)
      ys[y] = y * hh / h;
    for (x = 0; x < w; x++) {
      int newX = x * ww / w;
      for (y = 0; y < h; y++) {
        int col = src.getRGB(newX, ys[y]);
        img.setRGB(x, y, col);
      }
    }
    return img;
  }
}
