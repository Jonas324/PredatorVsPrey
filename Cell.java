import java.awt.Color;
public class Cell {
  protected int X;
  protected int Y;
  protected int health;
  protected int age;
  protected Color color;

  public Cell() {
    X = 0;
    Y = 0;
    health = 0;
    age = 0;
    Color = Color.black;
  }

  public Cell(int x, int y) {
    X = x;
    Y = y;
    health = 0;
    age = 0;protected
    Color = Color.black;
  }

  public Cell(int x, int y, int h) {
    X = x;
    Y = y;
    health = h;
    age = 0;
    Color = Color.black;
  }

  public Cell(int x, int y, int h, int a) {
    X = x;
    Y = y;
    health = h;
    age = a;
    Color = Color.black;
  }

  public Cell(int x, int y, int h, int a, Color c) {
    X = x;
    Y = y;
    health = h;
    age = a;
    color = c;
  }

  public int getX() {
    return X;
  }

  public void setX(int n) {
    X = n;
  }

  public int getY() {
    return Y;
  }

  public void setY(int n) {
    Y = n;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int n) {
    health = n;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int n) {
    age = n;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color c) {
    color = c;
  }

  public void act() {
    // make some decisions based on information retrieved by calling sense()
    // then decide to move, reproduce, etc.
  }

  protected void move() {
    // all cells move in the same way, but how they act might be different depending on type
  }

  protected void sense() {
    // TODO: how to get the status of surrounding cells
  }

  protected void die() {
    // all cells can die via some mechanism
  }
}
