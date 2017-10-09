import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
public class Index extends JFrame implements MouseListener {
  // TODO: implement way to input and save a seed for a simulation
  Random ran = new Random();

  Container con = getContentPane();

  final int DISPLAY_WIDTH = 1000;
  final int DISPLAY_HEIGHT = 900;

  Color black = new Color(0,0,0);

  Board board;
  JPanel ui;

  // TODO: allow for customization of starting values in here
  final int X = 800; // board width
  final int Y = 800; // board height

  int[][][] cells = new int[X][Y][2];
  /*
    3d array with size = 2 times the size of the board (2(X*Y)). X*Y = max number of creatures possible (every pixel on the board is occupied by a creature, no "dead" pixels).

    (x,y) position of a creature on the board is indicated by its (x,y) position in the array

    Values in each row are as follows:
    [X][Y][0] or col 0 - Type
      0 - dead (black)
      1 - prey (green)
      2 - predator (red)

    [X][Y][1] or col 1 - Health
      integer >= 0 (0 = dead)

  */

  int predStart = 50; // number of starting predators
  int predStartHealth = 1;

  int preyStart = 50; // number of starting prey
  int preyStartHealth = 1;

  int gen = 0; // generation we are currently on
  int predCount = 0; // number of predators
  int preyCount = 0; // number of prey

  boolean isBoardSetup = false;

  JLabel titleLbl = new JLabel("Predator vs Prey Simulation");
  JLabel genLbl = new JLabel("Generation: ");
  JLabel genCountLbl = new JLabel("0");
  JLabel predLbl = new JLabel("Predators: ");
  JLabel predCountLbl = new JLabel("0");
  JLabel preyLbl = new JLabel("Prey: ");
  JLabel preyCountLbl = new JLabel("0");

  JButton stepBtn = new JButton("Go One Step");
  JButton runBtn = new JButton("Run Simulation");
  JButton stopBtn = new JButton("Stop Simulation");
  JButton resetBtn = new JButton("Reset Simulation");
  JButton setupBtn = new JButton("Setup a board");

  public Index() {
    super("Predator vs Prey Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    //setResizable(false);
    setLayout(new BorderLayout());

    board = new Board(X, Y);
    board.setPreferredSize(board.getPreferredSize());
    con.add(board, BorderLayout.WEST);

    ui = new JPanel();
    ui.setPreferredSize(new Dimension(DISPLAY_WIDTH - X, DISPLAY_HEIGHT - Y));
    con.add(ui, BorderLayout.EAST);
    ui.setLayout(new FlowLayout());
    ui.add(titleLbl);
    ui.add(genLbl);
    ui.add(genCountLbl);
    ui.add(predLbl);
    ui.add(predCountLbl);
    ui.add(preyLbl);
    ui.add(preyCountLbl);

    ui.add(stepBtn);
    ui.add(runBtn);
    ui.add(stopBtn);
    ui.add(resetBtn);
    ui.add(setupBtn);

    setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

    stepBtn.addMouseListener(this);
    runBtn.addMouseListener(this);
    stopBtn.addMouseListener(this);
    resetBtn.addMouseListener(this);
    setupBtn.addMouseListener(this);
  }

  public static void main(String[] args) {
    Index app = new Index();
  }

  // all the logic for a generation contained here (triggered by clicking the step button)
  public void step() {
    // step called
    System.out.println("Step called");
    /*
      directions:
        0 - x+1 (right)
        1 - y+1 (down)
        2 - x-1 (left)
        3 - y-1 (up)
      NOTE: PLUS Y (1+y) IS DOWN ON THE SCREEN
    */
    // first, handle the logic for every prey on the board by searching the array for col1 values = 1
    // lets just try to make them move in a random direction, and handle all of that logic
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 1) {
          // prey found!
          // can I reproduce?

          // am I surrounded?
          boolean rightFree = false;
          boolean downFree = false;
          boolean leftFree = false;
          boolean upFree = false;
          if(x+1 < X) {
            if(cells[x+1][y][0] == 0) { // TODO: OPTIMIZE
              // right is occupied or a wall TODO: abstract "wall" part out to all iterations in the loop (move the boolean eval up a layer so it isn't executed every time)
              rightFree = true;
            }
          }

          if(y+1 < Y) {
            if(cells[x][y+1][0] == 0) {
              // below is occupied or a wall
              downFree = true;
            }
          }

          if(x-1 >= 0) {
            if(cells[x-1][y][0] == 0) {
              // left is occupied or a wall
              leftFree = true;
            }
          }

          if(y-1 >= 0) {
            if(cells[x][y-1][0] == 0) {
              // above is occupied or a wall
              upFree = true;
            }
          }


          // what direction do I want to go?
          Random rand = new Random();
          int dir = rand.nextInt(4);
          System.out.println("prey at x: " + x + " y: " + y + " wants to go in direction: " + dir);
          if((dir == 0) && rightFree) {
            // going right x+1
            // copy cells values into new location
            cells[x+1][y][0] = 1; // place i'm moving is now prey
            cells[x+1][y][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x+1, y, Color.green);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 1) && downFree) {
            // going down y+1
            // copy cells values into new location
            cells[x][y+1][0] = 1; // place i'm moving is now prey
            cells[x][y+1][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x, y+1, Color.green);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 2) && leftFree) {
            // going left x-1
            // copy cells values into new location
            cells[x-1][y][0] = 1; // place i'm moving is now prey
            cells[x-1][y][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x-1, y, Color.green);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 3) && upFree) {
            // going up y-1
            // copy cells values into new location
            cells[x][y-1][0] = 1; // place i'm moving is now prey
            cells[x][y-1][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x, y-1, Color.green);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          }
        }
        if(cells[x][y][0] == 2) {
          // predator found!

          // am I surrounded?
          boolean rightFree = false;
          boolean downFree = false;
          boolean leftFree = false;
          boolean upFree = false;
          if(x+1 < X) {
            if(cells[x+1][y][0] == 0) { // TODO: OPTIMIZE
              // right is occupied or a wall TODO: abstract "wall" part out to all iterations in the loop (move the boolean eval up a layer so it isn't executed every time)
              rightFree = true;
            }
          }

          if(y+1 < Y) {
            if(cells[x][y+1][0] == 0) {
              // below is occupied or a wall
              downFree = true;
            }
          }

          if(x-1 >= 0) {
            if(cells[x-1][y][0] == 0) {
              // left is occupied or a wall
              leftFree = true;
            }
          }

          if(y-1 >= 0) {
            if(cells[x][y-1][0] == 0) {
              // above is occupied or a wall
              upFree = true;
            }
          }
          // can I eat?

          // what direction do I want to go?
          Random rand = new Random();
          int dir = rand.nextInt(4);
          System.out.println("predator at x: " + x + " y: " + y + " wants to go in direction: " + dir);
          if((dir == 0) && rightFree) {
            // going right x+1
            // copy cells values into new location
            cells[x+1][y][0] = 2; // place i'm moving is now predator
            cells[x+1][y][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x+1, y, Color.red);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 1) && downFree) {
            // going down y+1
            // copy cells values into new location
            cells[x][y+1][0] = 2; // place i'm moving is now predator
            cells[x][y+1][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x, y+1, Color.red);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 2) && leftFree) {
            // going left x-1
            // copy cells values into new location
            cells[x-1][y][0] = 2; // place i'm moving is now predator
            cells[x-1][y][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x-1, y, Color.red);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          } else if((dir == 3) && upFree) {
            // going up y-1
            // copy cells values into new location
            cells[x][y-1][0] = 2; // place i'm moving is now predator
            cells[x][y-1][1] = cells[x][y][1]; // and it has my health
            board.updateCell(x, y-1, Color.red);

            cells[x][y][0] = 0; // place I just left is now dead
            cells[x][y][1] = 0; // and is also dead
            board.updateCell(x, y, Color.black);
          }
        }
      }
    }
  }

  public void run() {
    // run called
    System.out.println("Run called");
  }

  public void stop() {
    // stop called
    System.out.println("Stop called");
  }

  public void reset() {
    // reset called
    System.out.println("Reset called");
    // TODO: implement way to save the previous configuration/replay/seed etc. before clearing the board
    board.fillCanvas(Color.black);
    // reset the cells array
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        cells[x][y][0] = 0;
        cells[x][y][1] = 0;
      }
    }

    isBoardSetup = false;
  }

  // setup a new board WITH CELLS
  public void setup() {
    // setup called
    System.out.println("Setup called");
    reset();

    if(isBoardSetup) {
      System.out.println("Error: board already setup");
    } else {
      // first, generate starting locations for random prey
      for(int x = 0; x < preyStart; ++x) { // TODO: Optimize!
        System.out.println("x: " + x);
        boolean placed = false;
        while(placed == false) {
          int xPos = 0;
          int yPos = 0;
          xPos = ran.nextInt(X);
          yPos = ran.nextInt(Y);
          System.out.println(board.readCell(xPos, yPos));
          if(board.readCell(xPos, yPos) == black.getRGB()) { // check if position is already occupied or not
            cells[xPos][yPos][0] = 1;
            cells[xPos][yPos][1] = 1;
            board.updateCell(xPos, yPos, Color.green);
            placed = true;
            System.out.println("Prey placed at x: " + xPos + " y: " + yPos);
          }
        }
      }
      // same for pred
      for(int xx = 0; xx < predStart; ++xx) {
        System.out.println("xx: " + xx);
        boolean placed = false;
        while(placed == false) {
          int xPos = 0;
          int yPos = 0;
          xPos = ran.nextInt(X);
          yPos = ran.nextInt(Y);
          System.out.println(board.readCell(xPos, yPos));
          if(board.readCell(xPos, yPos) == black.getRGB()) { // check if position is already occupied or not
            cells[xPos][yPos][0] = 2;
            cells[xPos][yPos][1] = 1;
            board.updateCell(xPos, yPos, Color.red);
            placed = true;
            System.out.println("Predator placed at x: " + xPos + " y: " + yPos);
          }
        }
      }

      isBoardSetup = true;
    }
    System.out.println("End setup");
  }

  public void mouseClicked(MouseEvent e) {
    Object src = e.getSource();
    if(src == stepBtn) {
      // stepBtn clicked
      System.out.println("StepBtn clicked");
      step();
    }
    if(src == runBtn) {
      // run clicked
      System.out.println("RunBtn clicked");
      run();
    }
    if(src == stopBtn) {
      // stop clicked
      System.out.println("StopBtn clicked");
      stop();
    }
    if(src == resetBtn) {
      // reset clicked
      System.out.println("ResetBtn clicked");
      reset();
    }
    if(src == setupBtn) {
      // setup clicked
      System.out.println("SetupBtn clicked");
      setup();
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
