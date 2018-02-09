/*

	File Name:
	DemoYourChoice.java

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

import java.util.*;

public class DemoYourChoice {
	// variables


	public static void main(String[] args) {
		// INTRO
		System.out.println("\n\t DemoYourChoice\n\t David Whynot\n\n\n");

		// MAIN
		// thread this for performance (not really needed for this use case, but is how the engine is designed to work)
		// e = new Engine();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Engine();
      }
    });

		// create default object

	}
}
