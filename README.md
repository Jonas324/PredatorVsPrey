# Predator vs Prey Simulation
Built using Java, predvprey is a Conway's Game of Life-esque simulation where there are two types of cells: prey (green) and predators (red).

![demo unavailable :(](https://i.imgur.com/BTfC17X.gif "Demo (sorry for poor quality)")

## Usage

`git clone https://github.com/davidmwhynot/predvprey_simulation_v1.git`

`cd predvprey_simulation_v1`

`javac Index.java`

`java Index`

To start the simulation, first `Setup a Board`, then `Run`.

While running the simulation, do not try to adjust the zoom, do not click on any of the "step" buttons, do not setup a board, and do not reset the simulation until it has been stopped. You can stop the simulation by clicking `Stop`. You can resume the simulation at any time by pressing `Run` again.

To adjust the zoom, first `Stop` > `Reset Simulation` and then `Zoom In` or `Zoom Out`.

To advance the simulation manually, first `Stop` the simulation and then click on any of the `Step` buttons. While manually advancing, you can click `Compute Statistics` at any time to update the statistics about the current simulation.

## Rules
The game is played in rounds. Predators and prey each start the game with different amounts of "hp" and a given number of each are spread randomly across the "board".

Each round every cell obeys the following rules:
1. Prey reproduce. Eacy prey cell...
	1. Checks surroundings
	2. Randomly chooses an acting direction
	3. Checks if acting direction is available
	4. Checks if hp is above reproduction threshold
	5. If so, it reproduces...
		- spawn a new cell in an adjacent space
		- acting direction is used to choose location of this space
		- the current cell's hp is reset to 1
		- the child cell is created with 1 hp
2. Cells move by picking a random direction. If that direction is not already occupied then they move there.
3. Predators try to eat according to the following rules:
	a. Check surroundings
	b. Are there any cells in range to eat?
	c. If so, pick a random one of those cells (there might be multiple) and eat it!
	d. Eating causes the current predator to spawn a new predator in place of the consumed prey with a certain number of hit points
	e. Eating also causes the current predator to gain hitpoints equal to the hitpoints of the prey it consumed
4. Each round, every predator loses 1 hp. If their hp drops to 0, they die!
5. Finally, at the end of the round the surviving prey gain 1 hp.
