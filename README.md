# Predator vs Prey Simulation
Built using Java, predvprey is a Conway's Game of Life-esque simulation where there are two types of cells: prey (green) and predators (red).

![demo unavailable :(](https://i.imgur.com/8O2VveA.gif "Demo (sorry for poor quality)")

## Rules
The game is played in rounds. Predators and prey each start the game with different amounts of "hp" and a given number of each are spread randomly across the "board".

Each round every predator obeys the following rules:
1.

Prey obey the following rules:
1. Check surroundings...
2. Randomly choose an acting direction.
3. If cells acting direction is available (the cell it wants to replicate into is not occupied) AND the cell can replicate (its health is above the replication threshold), a new cell is spawned in the acting direction with 1 hp and the current cell's hp is reset to 1.
2. If an empty cell or cells exists that is directly above, below, left or right then randomly pick a direction and that will be the "acting" direction.
3. Check hp...
4. If hp is at or above
