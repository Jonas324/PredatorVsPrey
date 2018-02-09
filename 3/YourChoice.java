/*

	File Name:
	YourChoice.java

	Author:
	David Whynot

	Date Created:
	2/9/18

	Description:
	a. Create a class named YourChoice, which represents an object of your choice.  Data fields should include variables that are appropriate for your object.  Include set (mutator) and get (accessor) methods for each data field. Include a no-arg constructor that initializes an object to whatever values you decide upon.  Save this as YourChoice.java.

	b. Create an application called DemoYourChoice that demonstrates the use of the constructor, and also instantiates two more different objects, demonstrating the use of the set and get methods.  There should be three instantiated objects – one created using the constructor, and two more using the gets and sets.  Each object’s data should be displayed.

	c. Extra credit for prompting the user for the data rather than just using literals/constants.

	Type:
	GUI

*/

import java.awt.Color;
import java.util.*;

public class YourChoice {
	// fields
	Random ran;
	Color c;
	int x, y, red, green, blue;

	// constructor
	public YourChoice() {
		ran = new Random();
		x = ran.nextInt(320);
		y = ran.nextInt(180);
		red = 0;
		green = 50;
		blue = 200;
		c = new Color(red, green, blue);
	}
	// getters
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getRed() {
		return red;
	}
	public int getGreen() {
		return green;
	}
	public int getBlue() {
		return blue;
	}

	// setters
	public void setX(int i) {
		x = i;
	}
	public void setY(int i) {
		y = i;
	}
	public void setRed(int i) {
		red = i;
	}
	public void setGreen(int i) {
		green = i;
	}
	public void setBlue(int i) {
		blue = i;
	}

	// methods
	public void updateColor() {
		c = new Color(red, green, blue);
	}
}
