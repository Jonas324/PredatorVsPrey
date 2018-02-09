import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class Engine extends JFrame {
	Board board;
	JFrame ui;
	boolean status;
	String explainTxt = "This application will allow you to pick the RGB colors for three creatures. Your creatures will then be added to the black board you see and dance around randomly.";

	Container con = getContentPane();
	Color black = new Color(0,0,0);

	// ui components
	JLabel titleLbl = new JLabel("DemoYourChoice");

	JLabel explainLbl = new JLabel(explainTxt);

	// int[][][] cells = new int[320][180][2];
	List<YourChoice> cells = new ArrayList<YourChoice>();


	// constructor (needed to run the JFrame)
	public Engine() {
		super("DemoYourChoice");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setResizable(false);
    setLayout(new BorderLayout());

    board = new Board(1280, 720);
    board.setPreferredSize(board.getPreferredSize());
    con.add(board);

    ui = new JFrame("DemoYourChoice");
    ui.setPreferredSize(new Dimension(600, 800));
    ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ui.setLayout(new FlowLayout());
    ui.setVisible(true);
    ui.add(titleLbl);


		ui.pack();

    setSize(1280, 720);
	}
	// methods

	// start the threads
	public void run() {
    // run called
    System.out.println("Run called");
    status = true;
    Thread t = new Thread() {
      @Override
      public void run() {
        while(status) {
					System.out.println("step");
          // step();
					//render();
        }
      }
    };
    t.start();

    Thread h = new Thread() {
      @Override
      public void run() {
        while(status) {
          render();
        }
      }
    };
    h.start();
  }

	// render the Board
	public void render() {
		// for(int x = 0; x < 1280; ++x) {
    //   for(int y = 0; y < 720; ++y) {
    //     if(cells[x][y][0] == 0) {
    //       board.updateCellNoPaint(x, y, Color.black);
    //     } else if(cells[x][y][0] == 1) {
    //       board.updateCellNoPaint(x, y, Color.green);
    //     } else if(cells[x][y][0] == 2) {
    //       board.updateCellNoPaint(x, y, Color.red);
    //     }
    //   }
    // }

    board.repaint();
	}
}
