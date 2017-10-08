import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Board extends JPanel {
  private BufferedImage canvas;
  
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
    canvas.setRGB(x, y, color);
  }
}
