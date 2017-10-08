import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class UI extends JPanel implements MouseListener {
  JLabel title = new JLabel("Predator vs Prey Simulation");

  JButton step = new JButton("Go One Step");
  JButton run = new JButton("Run Simulation");
  JButton stop = new JButton("Stop Simulation");
  JButton reset = new JButton("Reset Simulation");

  public UI() {
    setLayout(new FlowLayout());

    add(title);
    add(step);
    add(run);
    add(stop);
    add(reset);

    step.addMouseListener(this);
    run.addMouseListener(this);
    stop.addMouseListener(this);
    reset.addMouseListener(this);
  }

  public void mouseClicked(MouseEvent e) {
    Object src = e.getSource();
    if(src == step) {
      // step clicked
      System.out.println("Step clicked");
    }
    if(src == run) {
      // run clicked
      System.out.println("Run clicked");
    }
    if(src == stop) {
      // stop clicked
      System.out.println("Stop clicked");
    }
    if(src == reset) {
      // reset clicked
      System.out.println("Reset clicked");
    }
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }
}
