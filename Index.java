import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
public class Index extends JFrame /*implements MouseListener*/ {
  // TODO: implement way to input and save a seed for a simulation
  Random ran;
  Boolean status = false;

  Container con = getContentPane();

  final int DISPLAY_WIDTH = 1600;
  final int DISPLAY_HEIGHT = 1000;

  Color black = new Color(0,0,0);

  Board board;
  JFrame ui;

  // TODO: allow for customization of starting values in here
  private int factor = 50;
  private int initFactor = 50;
  int[] aspectRatio = {16, 9};

  int X = aspectRatio[0]*factor; // board width
  int Y = aspectRatio[1]*factor; // board height

  int zoom = 1;

  int[][][] cells = new int[X][Y][2];
  /*
    3d array with size = 2 times the size of the board (2(X*Y)). X*Y = max number of creatures possible (every pixel on the board is occupied by a creature, no "dead" pixels).

    (x,y) position of a creature on the board is indicated by its (x,y) position in the array

    Values in each row are as follows:
    [X][Y][0] or col 0 - Type
      0 - dead (black)
      1 - prey (green)
      2 - predator (red)
      3 - omni (blue)

    [X][Y][1] or col 1 - Health
      integer >= 0 (0 = dead)

    [X][Y][2] or col 2 - Generations alive
      integer >= 0

  */

	int omniStart = Math.floor(.25*factor*initFactor);
	int omniStartHealth = 200;
	int omniReproHealth = 10;
	int decrementOmniHP = 1;


  int predStart = 1*factor*initFactor; // number of starting predators
  int predStartHealth = 200;
  int predReproHealth = 22;
  int decrementPredHP = 6;

  int preyStart = 5*factor*initFactor; // number of starting prey
  int preyStartHealth = 1;
  int cellsReproduceAt = 10;

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

  JPanel btnPnl = new JPanel();

  JPanel stepPnl = new JPanel();
  JButton stepBtn = new JButton("Go One Step");
  JButton step2Btn = new JButton("Go Two Steps");
  JButton step100Btn = new JButton("Go 100 Steps");
  JButton statStepBtn = new JButton("Stats and Step");

  JPanel statsPnl = new JPanel();
  JButton statsBtn = new JButton("Compute Statistics");

  JPanel runPnl = new JPanel();
  JButton resetBtn = new JButton("Reset Simulation");
  JButton setupBtn = new JButton("Setup a board");
  JButton runBtn = new JButton ("Run");
  JButton stopBtn = new JButton ("Stop");

  JPanel zoomPnl = new JPanel();
  JLabel zoomLbl = new JLabel("Zoom: ");
  JLabel zoomCountLbl = new JLabel("1");
  JButton ziBtn = new JButton ("Zoom in");
  JButton zoBtn = new JButton ("Zoom out");

  public Index() {
    super("Predator vs Prey Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setResizable(false);
    setLayout(new BorderLayout());

    board = new Board(X, Y);
    board.setPreferredSize(board.getPreferredSize());
    con.add(board);

    ui = new JFrame();
    ui.setPreferredSize(new Dimension(400, 900));
    ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ui.setLayout(new FlowLayout());
    ui.setVisible(true);
    ui.add(titleLbl);

    ui.add(genLbl);
    ui.add(genCountLbl);

    ui.add(numPnl);
      numPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      numPnl.setLayout(new FlowLayout());
      numPnl.add(totLbl);
      numPnl.add(totCountLbl);
      numPnl.add(predLbl);
      numPnl.add(predCountLbl);
      numPnl.add(preyLbl);
      numPnl.add(preyCountLbl);

    ui.add(preyFatPnl);
      preyFatPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      preyFatPnl.setLayout(new FlowLayout());
      preyFatPnl.add(preyFatLbl);
      preyFatPnl.add(preyFatCountLbl);
      preyFatPnl.add(preyFatXLbl);
      preyFatPnl.add(preyFatXCountLbl);
      preyFatPnl.add(preyFatYLbl);
      preyFatPnl.add(preyFatYCountLbl);

    ui.add(predFatPnl);
      predFatPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      predFatPnl.setLayout(new FlowLayout());
      predFatPnl.add(predFatLbl);
      predFatPnl.add(predFatCountLbl);
      predFatPnl.add(predFatXLbl);
      predFatPnl.add(predFatXCountLbl);
      predFatPnl.add(predFatYLbl);
      predFatPnl.add(predFatYCountLbl);

    ui.add(totTotPnl);
      totTotPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      totTotPnl.setLayout(new FlowLayout());
      totTotPnl.add(totTotLbl);
      totTotPnl.add(totTotCountLbl);
      totTotPnl.add(preyTotLbl);
      totTotPnl.add(preyTotCountLbl);
      totTotPnl.add(predTotLbl);
      totTotPnl.add(predTotCountLbl);

    ui.add(totAvgPnl);
      totAvgPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      totAvgPnl.setLayout(new FlowLayout());
      totAvgPnl.add(totAvgLbl);
      totAvgPnl.add(totAvgCountLbl);
      totAvgPnl.add(preyAvgLbl);
      totAvgPnl.add(preyAvgCountLbl);
      totAvgPnl.add(predAvgLbl);
      totAvgPnl.add(predAvgCountLbl);

    //ui.add(btnPnl);
      //btnPnl.setLayout(new BoxLayout(btnPnl, BoxLayout.Y_AXIS));

    //btnPnl.add(runPnl);
    ui.add(runPnl);
      runPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      runPnl.setLayout(new FlowLayout());
      runPnl.add(runBtn);
      runPnl.add(stopBtn);
      runPnl.add(resetBtn);
      runPnl.add(setupBtn);

    //btnPnl.add(stepPnl);
    ui.add(stepPnl);
      stepPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      stepPnl.setLayout(new FlowLayout());
      stepPnl.add(stepBtn);
      stepPnl.add(step2Btn);
      stepPnl.add(step100Btn);
      stepPnl.add(statStepBtn);

    //btnPnl.add(zoomPnl);
    ui.add(zoomPnl);
      zoomPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      zoomPnl.setLayout(new FlowLayout());
      zoomPnl.add(ziBtn);
      zoomPnl.add(zoBtn);
      zoomPnl.add(zoomLbl);
      zoomPnl.add(zoomCountLbl);

    //btnPnl.add(statsPnl);
    ui.add(statsPnl);
      statsPnl.setPreferredSize(new Dimension(400, DISPLAY_HEIGHT / 12));
      statsPnl.setLayout(new FlowLayout());
      statsPnl.add(statsBtn);

    ui.pack();

    setSize(X, Y);

    stepBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        step();
      }
    });
    runBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        run();
      }
    });
    stopBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        stop();
      }
    });
    step2Btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        step2();
      }
    });
    step100Btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        step100();
      }
    });
    statStepBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        statStep();
      }
    });
    statsBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        stats();
      }
    });
    resetBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        reset();
      }
    });
    setupBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        setup();
      }
    });
    ziBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        zi();
      }
    });
    zoBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        zo();
      }
    });

    // disable some buttons initially
    stepBtn.setEnabled(false);
    runBtn.setEnabled(false);
    step2Btn.setEnabled(false);
    step100Btn.setEnabled(false);
    statStepBtn.setEnabled(false);
    statsBtn.setEnabled(false);
    zoBtn.setEnabled(false);
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Index();
      }
    });
  }

  public void render() {
    //System.out.println("render");
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 0) {
          board.updateCellNoPaint(x, y, Color.black);
        } else if(cells[x][y][0] == 1) {
          board.updateCellNoPaint(x, y, Color.green);
        } else if(cells[x][y][0] == 2) {
          board.updateCellNoPaint(x, y, Color.red);
        } else if(cells[x][y][0] == 3) {
					board.updateCellNoPaint(x, y, Color.blue);
				}
      }
    }
    board.repaint();
  }

  // all the logic for a generation contained here (triggered by clicking the step button)
  public void step() {
    // TODO: increase effeciency by: searching for predators, having them eat if they can, decrementing their health (your health isn't decremented this round if you reproduce), moving them if they can't do either.
    // next, check which prey can reproduce. If you reprodcue, you can't move this round. increment prey's health if they subsequently move. Prey health only increases if they survive the suround (so predators don't get free health)
    // step called
    //System.out.println("Step called");

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
              //board.updateCell(x+1, y, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 1) && downFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x][y+1][0] = 1; // place i'm birthing is now prey
              cells[x][y+1][1] = 1; // and it has 1 health
              //board.updateCell(x, y+1, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 2) && leftFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x-1][y][0] = 1; // place i'm birthing is now prey
              cells[x-1][y][1] = 1; // and it has 1 health
              //board.updateCell(x-1, y, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          } else if((dir == 3) && upFree) {
            // can I reproduce?
            if(cells[x][y][1] >= cellsReproduceAt) {
              // YES!
              // copy cells values into new location
              cells[x][y-1][0] = 1; // place i'm birthing is now prey
              cells[x][y-1][1] = 1; // and it has 1 health
              //board.updateCell(x, y-1, Color.green);

              cells[x][y][1] = 1; // my health resets to one
            }
          }
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
              //board.updateCell(x+1, y, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 1) && downFree) {
              // I want to move!
              // going down y+1
              // copy cells values into new location
              cells[x][y+1][0] = 1; // place i'm moving is now prey
              cells[x][y+1][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x, y+1, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 2) && leftFree) {
              // I want to move!
              // going left x-1
              // copy cells values into new location
              cells[x-1][y][0] = 1; // place i'm moving is now prey
              cells[x-1][y][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x-1, y, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              //moved = true;
            } else if((dir == 3) && upFree) {
              // I want to move!
              // going up y-1
              // copy cells values into new location
              cells[x][y-1][0] = 1; // place i'm moving is now prey
              cells[x][y-1][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x, y-1, Color.green);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
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
              //board.updateCell(x+1, y, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 1) && downFree) {
              // going down y+1
              // copy cells values into new location
              cells[x][y+1][0] = 2; // place i'm moving is now predator
              cells[x][y+1][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x, y+1, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 2) && leftFree) {
              // going left x-1
              // copy cells values into new location
              cells[x-1][y][0] = 2; // place i'm moving is now predator
              cells[x-1][y][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x-1, y, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              moved = true;
            } else if((dir == 3) && upFree) {
              // going up y-1
              // copy cells values into new location
              cells[x][y-1][0] = 2; // place i'm moving is now predator
              cells[x][y-1][1] = cells[x][y][1]; // and it has my health
              //board.updateCell(x, y-1, Color.red);

              cells[x][y][0] = 0; // place I just left is now dead
              cells[x][y][1] = 0; // and is also dead
              //board.updateCell(x, y, Color.black);
              moved = true;
            }
          //}
        }
      }
    }

    // predator eating logic
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 2) {
          // predator found!
          // can I eat?
          boolean foodRight = false;
          boolean foodDown = false;
          boolean foodLeft = false;
          boolean foodUp = false;
          boolean foodExists = false;
          if(x+1 < X) {
            if(cells[x+1][y][0] == 1) {
              foodRight = true;
              foodExists = true;
            }
          }
          if(y+1 < Y) {
            if(cells[x][y+1][0] == 1) {
              foodDown = true;
              foodExists = true;
            }
          }
          if(x-1 >= 0) {
            if(cells[x-1][y][0] == 1) {
              foodLeft = true;
              foodExists = true;
            }
          }
          if(y-1 >= 0) {
            if(cells[x][y-1][0] == 1) {
              foodUp = true;
              foodExists = true;
            }
          }

          boolean ate = false;
          // int count = 0;
          while(!ate && foodExists) {
            //System.out.println("we in here " + count);
            //Random rand = new Random();
            int dir = ran.nextInt(4);
            //System.out.println("direction: " + dir + " count: " + count);

            if((dir == 0) && foodRight) {
              /*System.out.println("enter if 1 " + count);

                if(cells[x+1][y][0] == 1) {*/
                //if((cells[x+1][y][0] == 1) && (!ate)) {
                  // time to eat
                  cells[x][y][1] += cells[x+1][y][1]; // i get the prey's health
                  cells[x+1][y][0] = 0; // prey is now nothing (temporarily)
                  cells[x+1][y][1] = predReproHealth; // child has predetermined health
                  //board.updateCell(x+1, y, Color.red);
                  ate = true;
                /*} else {
                  foodRight = false;
                  System.out.println("foodRight false " + count);
                }
              }*/
            } else if((dir == 1) && foodDown) {
              /*System.out.println("enter if 2 " + count);

                if(cells[x][y+1][0] == 1) {*/
                //if((cells[x][y+1][0] == 1) && (!ate)) {
                  // time to eat
                  cells[x][y][1] += cells[x][y+1][1];
                  cells[x][y+1][0] = 0; // prey is now nothing (temporarily)
                  cells[x][y+1][1] = predReproHealth;
                  //board.updateCell(x, y+1, Color.red);
                  ate = true;
                /*} else {
                  foodDown = false;
                  System.out.println("foodDown false " + count);
                }
              }*/
            } else if((dir == 2) && foodLeft) {
              /*System.out.println("enter if 3 " + count);

                if(cells[x-1][y][0] == 1) {*/
                //if((cells[x-1][y][0] == 1) && (!ate)) {
                  // time to eat
                  cells[x][y][1] += cells[x-1][y][1];
                  cells[x-1][y][0] = 0; // prey is now nothing (temporarily)
                  cells[x-1][y][1] = predReproHealth;
                  //board.updateCell(x-1, y, Color.red);
                  ate = true;
                /*} else {
                  foodLeft = false;
                  System.out.println("foodLeft false " + count);
                }
              }*/
            } else if((dir == 3) && foodUp) {
              /*System.out.println("enter if 4 " + count);

                //if((cells[x][y-1][0] == 1) && (!ate)) {
                if(cells[x][y-1][0] == 1) {*/
                  // time to eat
                  cells[x][y][1] += cells[x][y-1][1];
                  cells[x][y-1][0] = 0; // prey is now nothing (temporarily)
                  cells[x][y-1][1] = predReproHealth;
                  //board.updateCell(x, y-1, Color.red);
                  ate = true;
                /*} else {
                  foodUp = false;
                  System.out.println("foodUp false " + count);
                }
              }*/
            }
            //System.out.println("end of while " + count);
            //++count;
          }
        }
      }
    }
    // now that the predators are done eating, lets set the type of those new predators to 2 instead of 0 (we know these exist because their health > 0)
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if((cells[x][y][0] == 0) && (cells[x][y][1] > 0)) {
          cells[x][y][0] = 2;
        }
      }
    }

    // , increment/decrement hp
    // decrement pred hp
    for(int x = 0; x < X; ++x) {
      for(int y = 0; y < Y; ++y) {
        if(cells[x][y][0] == 2) {
          cells[x][y][1] = cells[x][y][1] - decrementPredHP;
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
            //board.updateCell(x, y, Color.black);
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




    //render();
    genCountLbl.setText("" + ++gen);
    isBoardSetup = false;
  }

  public void run() {
    // run called
    System.out.println("Run called");
    status = true;
    Thread t = new Thread() {
      @Override
      public void run() {
        while(status) {
          step();
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

    Thread s = new Thread() {
      @Override
      public void run() {
        while(status) {
          stats();
        }
      }
    };
    s.start();

  }

  public void stop() {
    // stop called
    System.out.println("Stop called");
    if(status) {
     status = false;
   } else {
     System.out.println("Not running");
   }
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

    gen = 0;
    isBoardSetup = false;
  }

  // setup a new board WITH CELLS
  public void setup() {
    // setup called
    System.out.println("Setup called");
    if(status) {
      System.out.println("Error: simulation already running");
    } else if(isBoardSetup) {
      System.out.println("Error: board already setup");
    } else {
      reset();
      ran = new Random();

      // first, generate starting locations for random prey
      for(int x = 0; x < preyStart; ++x) { // TODO: Optimize!
        //System.out.println("x: " + x);
        boolean placed = false;
        while(placed == false) {
          int xPos = 0;
          int yPos = 0;
          xPos = ran.nextInt(X);
          yPos = ran.nextInt(Y);
          //System.out.println("trying to place at x: " + xPos + " y: " + yPos);
          //System.out.println(board.readCell(xPos, yPos));
          if(board.readCell(xPos, yPos) == black.getRGB()) { // check if position is already occupied or not
            cells[xPos][yPos][0] = 1;
            cells[xPos][yPos][1] = preyStartHealth;
            board.updateCell(xPos, yPos, Color.green);
            placed = true;
            //System.out.println("Prey placed at x: " + xPos + " y: " + yPos);
          }
        }
      }
      // same for pred
      for(int xx = 0; xx < predStart; ++xx) {
        //System.out.println("xx: " + xx);
        boolean placed = false;
        while(placed == false) {
          int xPos = 0;
          int yPos = 0;
          xPos = ran.nextInt(X);
          yPos = ran.nextInt(Y);
          //System.out.println(board.readCell(xPos, yPos));
          if(board.readCell(xPos, yPos) == black.getRGB()) { // check if position is already occupied or not
            cells[xPos][yPos][0] = 2;
            cells[xPos][yPos][1] = predStartHealth;
            board.updateCell(xPos, yPos, Color.red);
            placed = true;
            //System.out.println("Predator placed at x: " + xPos + " y: " + yPos);
          }
        }
      }
      isBoardSetup = true;
    }
    stepBtn.setEnabled(true);
    runBtn.setEnabled(true);
    step2Btn.setEnabled(true);
    step100Btn.setEnabled(true);
    statStepBtn.setEnabled(true);
    statsBtn.setEnabled(true);
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
		render();
  }

  public void step2() {
    step();
    step();
    stats();
		render();
  }

  public void step100() {
    for(int x = 0; x < 100; ++x) {
      step();
    }
    stats();
		render();
  }

  public void zi() {
    if(zoom < 4) {
      ++zoom;
      board.zoom(zoom);
      zoBtn.setEnabled(true);
    }
    if(zoom == 4) {
      zoBtn.setEnabled(true);
      ziBtn.setEnabled(false);
    }
    zoomCountLbl.setText("" + zoom);
    pack();
  }

  public void zo() {
    if(zoom > 1) {
      --zoom;
      board.zoom(zoom);
      ziBtn.setEnabled(true);
    }
    if(zoom == 1) {
      zoBtn.setEnabled(false);
      ziBtn.setEnabled(true);
    }
    zoomCountLbl.setText("" + zoom);
    pack();
  }
   /*
  public void mouseClicked(MouseEvent e) {
    Object src = e.getSource();
    if(src == stepBtn) {
      // step clicked
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
  */
}
