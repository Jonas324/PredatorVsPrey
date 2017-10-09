import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
public class Index extends JFrame implements MouseListener {
  // TODO: implement way to input and save a seed for a simulation
  Random ran;

  Container con = getContentPane();

  final int DISPLAY_WIDTH = 1920;
  final int DISPLAY_HEIGHT = 1080;

  Color black = new Color(0,0,0);

  Board board;
  JPanel ui;

  // TODO: allow for customization of starting values in here
  final int X = 300; // board width
  final int Y = 300; // board height

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

  int predStart = 500; // number of starting predators
  int predStartHealth = 50;
  int predReproHealth = 7;

  int preyStart = 1000; // number of starting prey
  int preyStartHealth = 3;
  int cellsReproduceAt = 6;

  int gen = 0; // generation we are currently on

  boolean isBoardSetup = false;

  JLabel titleLbl = new JLabel("Predator vs Prey Simulation");

  JLabel genLbl = new JLabel("Generation: ");
  JLabel genCountLbl = new JLabel("0");

  JPanel numPnl = new JPanel();
  JLabel totLbl = new JLabel("Total: ");
  JLabel totCountLbl = new JLabel("0");
  JLabel preyLbl = new JLabel("Prey: ");
  JLabel preyCountLbl = new JLabel("0");
  JLabel predLbl = new JLabel("Predators: ");
  JLabel predCountLbl = new JLabel("0");

  JPanel preyFatPnl = new JPanel();
  JLabel preyFatLbl = new JLabel("Fattest Prey: ");
  JLabel preyFatCountLbl = new JLabel("0");
  JLabel preyFatXLbl = new JLabel("X: ");
  JLabel preyFatXCountLbl = new JLabel("0");
  JLabel preyFatYLbl = new JLabel("Y: ");
  JLabel preyFatYCountLbl = new JLabel("0");


  JPanel predFatPnl = new JPanel();
  JLabel predFatLbl = new JLabel("Fattest Predator: ");
  JLabel predFatCountLbl = new JLabel("0");
  JLabel predFatXLbl = new JLabel("X: ");
  JLabel predFatXCountLbl = new JLabel("0");
  JLabel predFatYLbl = new JLabel("Y: ");
  JLabel predFatYCountLbl = new JLabel("0");

  JPanel totTotPnl = new JPanel();
  JLabel totTotLbl = new JLabel("Total HP: ");
  JLabel totTotCountLbl = new JLabel("0");
  JLabel preyTotLbl = new JLabel("Total HP Prey: ");
  JLabel preyTotCountLbl = new JLabel("0");
  JLabel predTotLbl = new JLabel("Total HP Predator: ");
  JLabel predTotCountLbl = new JLabel("0");

  JPanel totAvgPnl = new JPanel();
  JLabel totAvgLbl = new JLabel("Average: ");
  JLabel totAvgCountLbl = new JLabel("0");
  JLabel preyAvgLbl = new JLabel("Average Prey: ");
  JLabel preyAvgCountLbl = new JLabel("0");
  JLabel predAvgLbl = new JLabel("Average Predator: ");
  JLabel predAvgCountLbl = new JLabel("0");

  JButton stepBtn = new JButton("Go One Step");
  JButton step2Btn = new JButton("Go Two Steps");
  JButton step100Btn = new JButton("Go 100 Steps");
  JButton statStepBtn = new JButton("Stats and Step");
  JButton statsBtn = new JButton("Compute Statistics");
  JButton resetBtn = new JButton("Reset Simulation");
  JButton setupBtn = new JButton("Setup a board");
  JButton ziBtn = new JButton ("Zoom in");
  JButton zoBtn = new JButton ("Zoom out");

  public Index() {
    super("Predator vs Prey Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    //setResizable(false);
    setLayout(new BorderLayout());

    board = new Board((X*4), (Y*4));
    board.setPreferredSize(board.getPreferredSize());
    con.add(board, BorderLayout.WEST);

    ui = new JPanel();
    ui.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), DISPLAY_HEIGHT));
    con.add(ui, BorderLayout.EAST);
    ui.setLayout(new FlowLayout());
    ui.add(titleLbl);

    ui.add(genLbl);
    ui.add(genCountLbl);

    ui.add(numPnl);
    numPnl.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), 50));
    numPnl.setLayout(new FlowLayout());
    numPnl.add(totLbl);
    numPnl.add(totCountLbl);
    numPnl.add(predLbl);
    numPnl.add(predCountLbl);
    numPnl.add(preyLbl);
    numPnl.add(preyCountLbl);

    ui.add(preyFatPnl);
    preyFatPnl.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), 50));
    preyFatPnl.setLayout(new FlowLayout());
    preyFatPnl.add(preyFatLbl);
    preyFatPnl.add(preyFatCountLbl);
    preyFatPnl.add(preyFatXLbl);
    preyFatPnl.add(preyFatXCountLbl);
    preyFatPnl.add(preyFatYLbl);
    preyFatPnl.add(preyFatYCountLbl);

    ui.add(predFatPnl);
    predFatPnl.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), 50));
    predFatPnl.setLayout(new FlowLayout());
    predFatPnl.add(predFatLbl);
    predFatPnl.add(predFatCountLbl);
    predFatPnl.add(predFatXLbl);
    predFatPnl.add(predFatXCountLbl);
    predFatPnl.add(predFatYLbl);
    predFatPnl.add(predFatYCountLbl);

    ui.add(totTotPnl);
    totTotPnl.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), 50));
    totTotPnl.setLayout(new FlowLayout());
    totTotPnl.add(totTotLbl);
    totTotPnl.add(totTotCountLbl);
    totTotPnl.add(preyTotLbl);
    totTotPnl.add(preyTotCountLbl);
    totTotPnl.add(predTotLbl);
    totTotPnl.add(predTotCountLbl);

    ui.add(totAvgPnl);
    totAvgPnl.setPreferredSize(new Dimension(DISPLAY_WIDTH - (X*4), 50));
    totAvgPnl.setLayout(new FlowLayout());
    totAvgPnl.add(totAvgLbl);
    totAvgPnl.add(totAvgCountLbl);
    totAvgPnl.add(preyAvgLbl);
    totAvgPnl.add(preyAvgCountLbl);
    totAvgPnl.add(predAvgLbl);
    totAvgPnl.add(predAvgCountLbl);



    ui.add(stepBtn);
    ui.add(step2Btn);
    ui.add(step100Btn);
    ui.add(statStepBtn);
    ui.add(statsBtn);
    ui.add(resetBtn);
    ui.add(setupBtn);
    ui.add(ziBtn);
    ui.add(zoBtn);

    setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
    pack();

    stepBtn.addMouseListener(this);
    step2Btn.addMouseListener(this);
    step100Btn.addMouseListener(this);
    statStepBtn.addMouseListener(this);
    statsBtn.addMouseListener(this);
    resetBtn.addMouseListener(this);
    setupBtn.addMouseListener(this);
    ziBtn.addMouseListener(this);
    zoBtn.addMouseListener(this);
  }

  public static void main(String[] args) {
    Index app = new Index();
  }

  // all the logic for a generation contained here (triggered by clicking the step button)
  public void step() {
    // TODO: increase effeciency by: searching for predators, having them eat if they can, decrementing their health (your health isn't decremented this round if you reproduce), moving them if they can't do either.
    // next, check which prey can reproduce. If you reprodcue, you can't move this round. increment prey's health if they subsequently move. Prey health only increases if they survive the suround (so predators don't get free health)
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



    // this loop handles reproduction logic
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 1) {
          // prey found!
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


          // what direction do I prefer to act in?
          //Random rand = new Random();
          int dir = ran.nextInt(4);
          // System.out.println("prey at x: " + x + " y: " + y + " prefers to act in direction: " + dir);
          if((dir == 0) && rightFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x+1][y][0] = 1; // place i'm birthing is now prey
              cells[x+1][y][1] = 1; // and it has 1 health
              board.updateCell(x+1, y, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 1) && downFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x][y+1][0] = 1; // place i'm birthing is now prey
              cells[x][y+1][1] = 1; // and it has 1 health
              board.updateCell(x, y+1, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 2) && leftFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x-1][y][0] = 1; // place i'm birthing is now prey
              cells[x-1][y][1] = 1; // and it has 1 health
              board.updateCell(x-1, y, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 3) && upFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x][y-1][0] = 1; // place i'm birthing is now prey
              cells[x][y-1][1] = 1; // and it has 1 health
              board.updateCell(x, y-1, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          }
        }
      }
    }

    // predator eating logic
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 2) {
          // predator found!
          // can I eat?
          //boolean ate = false;
          //Random rand = new Random();
          int dir = ran.nextInt(4);
          // System.out.println("predator at x: " + x + " y: " + y + " wants to go in direction: " + dir);
          if(dir == 0) {
            if(x+1 < X) {
              if(cells[x+1][y][0] == 1) {
              //if((cells[x+1][y][0] == 1) && (!ate)) {
                // time to eat
                cells[x][y][1] += cells[x+1][y][1];
                cells[x+1][y][0] = 2;
                cells[x+1][y][1] = predReproHealth;
                board.updateCell(x+1, y, Color.red);
                //ate = true;
              }
            }
          } else if(dir == 1) {
            if(y+1 < Y) {
              if(cells[x][y+1][0] == 1) {
              //if((cells[x][y+1][0] == 1) && (!ate)) {
                // time to eat
                cells[x][y][1] += cells[x][y+1][1];
                cells[x][y+1][0] = 2;
                cells[x][y+1][1] = predReproHealth;
                board.updateCell(x, y+1, Color.red);
                //ate = true;
              }
            }
          } else if(dir == 2) {
            if(x-1 >= 0) {
              if(cells[x-1][y][0] == 1) {
              //if((cells[x-1][y][0] == 1) && (!ate)) {
                // time to eat
                cells[x][y][1] += cells[x-1][y][1];
                cells[x-1][y][0] = 2;
                cells[x-1][y][1] = predReproHealth;
                board.updateCell(x-1, y, Color.red);
              //  ate = true;
              }
            }
          } else if(dir == 3) {
            if(y-1 >= 0) {
              //if((cells[x][y-1][0] == 1) && (!ate)) {
              if(cells[x][y-1][0] == 1) {
                // time to eat
                cells[x][y][1] += cells[x][y-1][1];
                cells[x][y-1][0] = 2;
                cells[x][y-1][1] = predReproHealth;
                board.updateCell(x, y-1, Color.red);
                //ate = true;
              }
            }
          }
        }
      }
    }

    // finally, increment/decrement hp
    // decrement pred hp
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 2) {
          cells[x][y][1] = cells[x][y][1] - 3;
        }
      }
    }

    // now that the predators are done moving for the step, we decide which to kill
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 2) {
          //if((cells[x][y][1] <= 1) || (cells[x][y][1] >= 1500)) {
          if(cells[x][y][1] <= 1) {
            // this predator is dead
            cells[x][y][0] = 0;
            cells[x][y][1] = 0;
            board.updateCell(x, y, Color.black);
          }
        }
        /*
        if(cells[x][y][0] == 1) {
          if(cells[x][y][1] >= 400) {
            // this prey is dead
            cells[x][y][0] = 0;
            cells[x][y][1] = 0;
            board.updateCell(x, y, Color.black);
          }
        }
        */
      }
    }

    // increment prey hp
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 1) {
          ++cells[x][y][1];
        }
      }
    }

    // MOVE LAST
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 1) {
          // prey found!
          // am I surrounded?
          boolean rightFree = false;
          boolean downFree = false;
          boolean leftFree = false;
          boolean upFree = false;
          boolean trapped = true;
          if(x+1 < X) {
            if(cells[x+1][y][0] == 0) { // TODO: OPTIMIZE
              // right is occupied or a wall TODO: abstract "wall" part out to all iterations in the loop (move the boolean eval up a layer so it isn't executed every time)
              rightFree = true;
              trapped = false;
            }
          }

          if(y+1 < Y) {
            if(cells[x][y+1][0] == 0) {
              // below is occupied or a wall
              downFree = true;
              trapped = false;
            }
          }

          if(x-1 >= 0) {
            if(cells[x-1][y][0] == 0) {
              // left is occupied or a wall
              leftFree = true;
              trapped = false;
            }
          }

          if(y-1 >= 0) {
            if(cells[x][y-1][0] == 0) {
              // above is occupied or a wall
              upFree = true;
              trapped = false;
            }
          }


          // what direction do I prefer to move in?
          //boolean moved = false;
          //while(!moved && (trapped == false)) {
            //Random rand = new Random();
            int dir = ran.nextInt(4);
            // System.out.println("prey at x: " + x + " y: " + y + " prefers to move in direction: " + dir);
            if((dir == 0) && rightFree) {
              // going right x+1
              // copy cells values into new location
              cells[x+1][y][0] = 1; // place i'm moving is now prey
              cells[x+1][y][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x+1, y, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 1) && downFree) {
              // I want to move!
              // going down y+1
              // copy cells values into new location
              cells[x][y+1][0] = 1; // place i'm moving is now prey
              cells[x][y+1][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x, y+1, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 2) && leftFree) {
              // I want to move!
              // going left x-1
              // copy cells values into new location
              cells[x-1][y][0] = 1; // place i'm moving is now prey
              cells[x-1][y][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x-1, y, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 3) && upFree) {
              // I want to move!
              // going up y-1
              // copy cells values into new location
              cells[x][y-1][0] = 1; // place i'm moving is now prey
              cells[x][y-1][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x, y-1, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              //moved = true;
            }
          //}
        }
        if(cells[x][y][0] == 2) {
          // predator found!

          // am I surrounded?
          boolean rightFree = false;
          boolean downFree = false;
          boolean leftFree = false;
          boolean upFree = false;
          boolean trapped = true;
          if(x+1 < X) {
            if(cells[x+1][y][0] == 0) { // TODO: OPTIMIZE
              // right is occupied or a wall TODO: abstract "wall" part out to all iterations in the loop (move the boolean eval up a layer so it isn't executed every time)
              rightFree = true;
              trapped = false;
            }
          }

          if(y+1 < Y) {
            if(cells[x][y+1][0] == 0) {
              // below is occupied or a wall
              downFree = true;
              trapped = false;
            }
          }

          if(x-1 >= 0) {
            if(cells[x-1][y][0] == 0) {
              // left is occupied or a wall
              leftFree = true;
              trapped = false;
            }
          }

          if(y-1 >= 0) {
            if(cells[x][y-1][0] == 0) {
              // above is occupied or a wall
              upFree = true;
              trapped = false;
            }
          }
          // what direction do I want to go?
          boolean moved = false;
          //while(!moved && (trapped == false)) {
            //Random rand = new Random();
            int dir = ran.nextInt(4);
            // System.out.println("predator at x: " + x + " y: " + y + " wants to go in direction: " + dir);
            if((dir == 0) && rightFree) {
              // going right x+1
              // copy cells values into new location
              cells[x+1][y][0] = 2; // place i'm moving is now predator
              cells[x+1][y][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x+1, y, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 1) && downFree) {
              // going down y+1
              // copy cells values into new location
              cells[x][y+1][0] = 2; // place i'm moving is now predator
              cells[x][y+1][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x, y+1, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 2) && leftFree) {
              // going left x-1
              // copy cells values into new location
              cells[x-1][y][0] = 2; // place i'm moving is now predator
              cells[x-1][y][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x-1, y, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 3) && upFree) {
              // going up y-1
              // copy cells values into new location
              cells[x][y-1][0] = 2; // place i'm moving is now predator
              cells[x][y-1][1] = cells[x][y][1]; // and it has my health
              board.updateCell(x, y-1, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              board.updateCell(x, y, Color.black);
              moved = true;
            }
          //}
        }
      }
    }



    genCountLbl.setText("" + ++gen);
    isBoardSetup = false;
  }

  /*
  public void run() {
    // run called
    System.out.println("Run called");
    for(int x = 0; x < 10; ++x) {
      step();
      try {
        Thread.sleep(2000);             // Sleep for 2 seconds
      }catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
      repaint();
    }
  }

  public void stop() {
    // stop called
    System.out.println("Stop called");
  }
  */

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

    gen = 0;
    isBoardSetup = false;
  }

  // setup a new board WITH CELLS
  public void setup() {
    // setup called
    System.out.println("Setup called");
    reset();

    ran = new Random();

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
          System.out.println("trying to place at x: " + xPos + " y: " + yPos);
          System.out.println(board.readCell(xPos, yPos));
          if(board.readCell(xPos, yPos) == black.getRGB()) { // check if position is already occupied or not
            cells[xPos][yPos][0] = 1;
            cells[xPos][yPos][1] = preyStartHealth;
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
            cells[xPos][yPos][1] = predStartHealth;
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

  public void stats() {
    int numPrey = 0;
    int numPred = 0;

    int fatPrey = 0;
    int fatPred = 0;

    int totPrey = 0;
    int totPred = 0;

    int fatXPrey = 0;
    int fatYPrey = 0;
    int fatXPred = 0;
    int fatYPred = 0;

    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        int t = cells[x][y][0];
        int h = cells[x][y][1];
        if(t == 1) {
          ++numPrey; // get number of prey
          totPrey += h; // get total health prey
          if(h >= fatPrey) { // get fattest prey
            fatPrey = h;
            fatXPrey = x;
            fatYPrey = y;
          }
        } else if(t == 2) {
          ++numPred; // get number of pred
          totPred += h; // get total health pred
          if(h >= fatPred) { // get fattest pred
            fatPred = h;
            fatXPred = x;
            fatYPred = y;
          }
        }
        // get median health prey
        // get median health pred
      }
    }
    // update labels
    totCountLbl.setText("" + (numPrey + numPred));
    preyCountLbl.setText("" + numPrey);
    predCountLbl.setText("" + numPred);

    preyFatCountLbl.setText("" + fatPrey);
    preyFatXCountLbl.setText("" + fatXPrey);
    preyFatYCountLbl.setText("" + fatYPrey);

    predFatCountLbl.setText("" + fatPred);
    predFatXCountLbl.setText("" + fatXPred);
    predFatYCountLbl.setText("" + fatYPred);

    totTotCountLbl.setText("" + (totPrey + totPred));
    preyTotCountLbl.setText("" + totPrey);
    predTotCountLbl.setText("" + totPred);

    totAvgCountLbl.setText("" + ((totPrey + totPred) / (numPrey + numPred)));
    preyAvgCountLbl.setText("" + (totPrey / numPrey));
    predAvgCountLbl.setText("" + (totPred / numPred));

  }

  public void statStep() {
    step();
    stats();
  }

  public void step2() {
    step();
    step();
    stats();
  }

  public void step100() {
    for(int x = 0; x < 100; ++x) {
      step();
    }
    stats();
  }

  public void zi() {
    board.zoom(2);
  }

  public void zo() {
    board.zoom(1);
  }

  public void mouseClicked(MouseEvent e) {
    Object src = e.getSource();
    if(src == stepBtn) {
      // step clicked
      System.out.println("StepBtn clicked");
      step();
    }
    /*
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
    */
    if(src == step2Btn) {
      // step2 clicked
      System.out.println("Step2Btn clicked");
      step2();
    }
    if(src == step100Btn) {
      // step100 clicked
      System.out.println("step100Btn clicked");
      step100();
    }
    if(src == statStepBtn) {
      // statStep clicked
      System.out.println("statStepBtn clicked");
      statStep();
    }
    if(src == statsBtn) {
      // stats clicked
      System.out.println("statsBtn clicked");
      stats();
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
    if(src == ziBtn) {
      // zi clicked
      System.out.println("ziBtn clicked");
      zi();
    }
    if(src == zoBtn) {
      // zo clicked
      System.out.println("zoBtn clicked");
      zo();
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
