import java.awt.*;
import javax.swing.*;
public class Index extends JFrame {
  Container con = getContentPane();
  final int DISPLAY_WIDTH = 1000;
  final int DISPLAY_HEIGHT = 900;
  final int X = 800;
  final int Y = 800;
  public Index() {
    super("Predator vs Prey Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    //setResizable(false);
    setLayout(new BorderLayout());

    Board board = new Board(X, Y);
    board.setPreferredSize(board.getPreferredSize());
    con.add(board, BorderLayout.WEST);

    UI ui = new UI();
    ui.setPreferredSize(new Dimension(DISPLAY_WIDTH - X, DISPLAY_HEIGHT - Y));
    con.add(ui, BorderLayout.EAST);

    setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
  }

  public static void main(String[] args) {
    Index app = new Index();
  }
}
