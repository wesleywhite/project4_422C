package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Michael Blume
 * mab7645
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Spring 2018
 */


import java.util.ArrayList;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private static int timestep = 0;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static List<Critter> collection = new java.util.ArrayList<Critter>();
	private static String[][] board = new String[Params.world_width][Params.world_height];

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean hasMoved;

	
	protected final void walk(int direction) {
	    // CHECK FOR HAS MOVED HERE

		this.energy -= Params.walk_energy_cost; // Deduct the walk energy cost
		int oldX = x_coord;
		int oldY = y_coord;

		board[x_coord][y_coord] = null; // Clear the board of where it was

		move(direction, 1);
		board[x_coord][y_coord] = this.toString(); // Add to new place on board

		for (Critter critter : collection) { // But replace it if there is something else there too
			if (critter.x_coord == oldX && critter.y_coord == oldY) {
				board[oldX][oldY] = critter.toString();
			}
		}
	}
	
	protected final void run(int direction) {

		this.energy -= Params.run_energy_cost; // Deduct the run energy cost
		int oldX = x_coord;
		int oldY = y_coord;

		board[x_coord][y_coord] = null; // Clear the board of where it was

		move(direction, 2);
		board[x_coord][y_coord] = this.toString(); // Add to new place on board

		for (Critter critter : collection) { // But replace it if there is something else there too
			if (critter.x_coord == oldX && critter.y_coord == oldY) {
				board[oldX][oldY] = critter.toString();
			}
		}
	}

	private void move(int direction, int steps) {

		hasMoved = true;

		int height = Params.world_height;
		int width = Params.world_width;

		switch(direction) {
			// right
			case 0: x_coord = (x_coord + steps) % width;
					break;
			// right up
			case 1: x_coord = (x_coord + steps) % width;
					y_coord = Math.floorMod(y_coord - steps, height);
					break;
			// up
			case 2: y_coord = Math.floorMod(y_coord - steps, height);
					break;
			// left up
			case 3: x_coord = Math.floorMod(x_coord - steps, width);
					y_coord = Math.floorMod(y_coord - steps, height);
					break;
			// left
			case 4: x_coord = Math.floorMod(x_coord - steps, width);
					break;
			// down left
			case 5: x_coord = Math.floorMod(x_coord - steps, width);
					y_coord = (y_coord + steps) % height;
					break;
			// down
			case 6:	y_coord = (y_coord + steps) % height;
					break;
			// down right
			case 7: x_coord = (x_coord + steps) % width;
					y_coord = (y_coord + steps) % height;
					break;
		}
	}


	protected final void reproduce(Critter offspring, int direction) {
		// Confirm that the “parent” critter has energy at least as large as
		// Params.min_reproduce_energy. If not, then your reproduce function
		// should return immediately. Naturally, the parent must not be dead (e.g., did not
		// lose a fight in the previous time step), but you should have removed any such crit
		// ters from the critter collection and/or set their energy to zero anyway.
		if (this.energy >= Params.min_reproduce_energy) {
			offspring.energy = Math.floorDiv(this.energy, 2); // 1/2 rounding down
			this.energy = (int) Math.ceil((double) this.energy / 2); // 1/2 rounding up


			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord; // Give parent's coordinates

			offspring.walk(direction); // This puts the baby on the board, but not in the collection.
			offspring.energy += Params.walk_energy_cost; // Adds back energy that was taken out in walk.

			babies.add(offspring); // Not added to the collection until after the time step.

		}
		// Assign the child energy equal to ½ of the parent’s energy (rounding fractions
		// down). Reassign the parent so that it has ½ of its energy (rounding fraction up).

		// Assign the child a position indicated by the parent’s current position and the spec
		// ified direction. The child will always be created in a position immediately adja
		// cent to the parent. If that position is occupied, put the child there anyway. The
		// child will not “encounter” any other critters this time step
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
		Class c = Class.forName(critter_class_name);
		Critter newCritter = (Critter) c.newInstance();
		newCritter.energy = Params.start_energy;
		newCritter.x_coord = getRandomInt(Params.world_width);
		newCritter.y_coord = getRandomInt(Params.world_height);
		newCritter.hasMoved = false;
		collection.add(newCritter);
		board[newCritter.x_coord][newCritter.y_coord] = newCritter.toString();
		}
		catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			Class c = Class.forName(critter_class_name);
			for (Critter critter : collection) {
				if (critter.getClass().equals(c)) {
					result.add(critter);
				}
			}
		} catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
            // need to kill the critter if set energy is below 0
		    super.energy = new_energy_value;
		    if (super.energy <= 0) {
		        Critter.remove(this);
            }
		}

		protected void setX_coord(int new_x_coord) {
            // need to update the board on the set x and set y
		    int oldX = super.x_coord, oldY = super.y_coord;
		    board[oldX][oldY] = null;
		    super.x_coord = new_x_coord;
		    board[super.x_coord][super.y_coord] = this.toString();
		    for (Critter critter : collection) {
		        if (critter.x_coord == oldX && critter.y_coord == oldY) {
		            board[oldX][oldY] = critter.toString();
                }
            }
		}

		protected void setY_coord(int new_y_coord) {
            // need to update the board on the set x and set y
            int oldX = super.x_coord, oldY = super.y_coord;
            board[oldX][oldY] = null;
            super.y_coord = new_y_coord;
            board[super.x_coord][super.y_coord] = this.toString();
            for (Critter critter : collection) {
                if (critter.x_coord == oldX && critter.y_coord == oldY) {
                    board[oldX][oldY] = critter.toString();
                }
            }
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return collection;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		// Remove critters from collection
		collection.clear();
		// Clear the board
		for (int i = 0; i < Params.world_height; i++) {
			for (int j = 0; j < Params.world_width; j++) {
				board[i][j] = null;
			}
		}
	}
	
	public static void worldTimeStep() {

		// 1. increment timestep; timestep++;
		timestep++;

		// 2. doTimeSteps(); This is where wach critter will call walk/run
		for(Critter critter : collection) {
            critter.doTimeStep();
		}
		// 3. Do the fights. doEncounters();
		doEncounters();
		// Clear the hasMoved flag each time step
		for (Critter critter : collection) {
            critter.hasMoved = false;
		}

		// 4. updateRestEnergy();
		List<Critter> remove = new ArrayList<Critter>();

		for(Critter critter : collection) {
            critter.energy -= Params.rest_energy_cost;
			if (critter.energy <= 0) {
                remove.add(critter);
                for (Critter crit : collection) {
                    if (!crit.equals(critter) && crit.x_coord == critter.x_coord && crit.y_coord == critter.y_coord)
                        board[crit.x_coord][crit.y_coord] = crit.toString();
                }
			}
		}
		collection.removeAll(remove);


		// 5. Generate Algae genAlgae();
		genAlgae();

		// 6. Move babies to general population. They are already on the board.
		collection.addAll(babies);
		babies.clear();
	}


    /**
     * Generates Algae
     */
	private static void genAlgae() {
        for (int i = 0; i < Params.refresh_algae_count; i++) {
            Algae alg = new Algae();
            alg.setEnergy(Params.start_energy);
            int x = getRandomInt(Params.world_height);
            int y = getRandomInt(Params.world_width);
            alg.setX_coord(x);
            alg.setY_coord(y);
            collection.add(alg);
            board[x][y] = alg.toString();
        }
    }

	/**
	 * Returns list of 2 critters at the same spot, or null if none are found.
	 */
	private static List<Critter> samePlace() {
		for (int i = 0; i < collection.size() - 1; i++) {
			for (int j = i + 1; j < collection.size(); j++) {

				if (collection.get(i).x_coord == collection.get(j).x_coord && collection.get(i).y_coord == collection.get(j).y_coord) {
					ArrayList<Critter> tempList = new ArrayList<Critter>();
					tempList.add(collection.get(j));
					tempList.add(collection.get(i));
					return tempList;
				}

			}
		}
		return null;
	}

    /**
     * Returns true if the current x and y is occupied, false otherwise.
     */
	private static boolean isOccupied(int x, int y) {
		for (Critter crit : collection) {
			if (crit.x_coord == x && crit.y_coord == y)
				return true;
		}
		return false;
	}

    /**
     * Removes critter from the collection, and replaces its spot with another critter if there is one there.
     */
	private static void remove(Critter critter) {
        collection.remove(critter);
        board[critter.x_coord][critter.y_coord] = null;
        for (Critter crit : collection) {
            if (crit.x_coord == critter.x_coord && crit.y_coord == critter.y_coord)
                board[critter.x_coord][critter.y_coord] = crit.toString();
        }

    }

    /**
     * Handles all the fights, running away, etc.
     */
	private static void doEncounters() {
		List<Critter> crits = samePlace();

		while (crits != null) {

			Critter first = crits.get(0);
			Critter second = crits.get(1);
			int firstRoll, secondRoll;
			boolean firstFight, secondFight;

			if (first.toString().equals("@")) {
			    // Algae cannot run away.
                first.hasMoved = true;
                firstRoll = -1;
            }

			if (second.toString().equals("@")) {
			    // Algae cannot run away.
                second.hasMoved = true;
                secondRoll = -1;
            }


			firstFight = first.fight(second.toString());
			if (!firstFight) {
				// Wants to run away
				// And has not moved yet in time step
				if (!first.hasMoved) {
					// int random = getRandomInt(8);
					int x = (first.x_coord + 1) % Params.world_width;
					int y = first.y_coord;
					if (!isOccupied(x, y)) {
						first.walk(0);
						first.energy += Params.walk_energy_cost; // Add back the energy, it is subtracted later
					}
				}
				first.energy -= Params.walk_energy_cost; // Subtract energy even if it cannot walk
			}

			if (first.energy <= 0) {
//				collection.remove(first);
//				board[first.x_coord][first.y_coord] = null;
//				for (Critter critter : collection) {
//					if (critter.x_coord == first.x_coord && critter.y_coord == first.y_coord)
//						board[first.x_coord][first.y_coord] = critter.toString();
//				}
                remove(first);
			}


			secondFight = second.fight(first.toString());
			if (!secondFight) {
				// Wants to run away
                // And has not moved yet in time step
				if (!second.hasMoved) {
					// int random = getRandomInt(8);
					int x = Math.floorMod(second.x_coord - 1, Params.world_width); // tries to go left
					int y = second.y_coord;
					if (!isOccupied(x, y)) {
						second.walk(4);
						second.energy += Params.walk_energy_cost; // Add back the energy, it is subtracted later
					}
				}
				second.energy -= Params.walk_energy_cost; // Subtract energy even if it cant walk
			}

			if (second.energy <= 0) {
//				board[second.x_coord][second.y_coord] = null;
//				collection.remove(second);
//				for (Critter critter : collection) {
//					if (critter.x_coord == second.x_coord && critter.y_coord == second.y_coord)
//						board[second.x_coord][second.y_coord] = critter.toString();
//				}
                remove(second);
			}



			if (first.x_coord == second.x_coord && first.y_coord == second.y_coord && collection.contains(first) && collection.contains(second)) {

			    // Roll the dice
				if (firstFight) {
					firstRoll = getRandomInt(first.energy);
				} else if (first.toString().equals("@")) {
					firstRoll = -1;
				} else {
				    firstRoll = 0;
                }

				if (secondFight) {
					secondRoll = getRandomInt(second.energy);
				} else if (second.toString().equals("@")) {
					secondRoll = -2;
				} else {
				    secondRoll = 0;
                }

                // Check who wins
				if (firstRoll >= secondRoll) {
					first.energy += second.energy / 2;
					board[second.x_coord][second.y_coord] = first.toString();
					collection.remove(second);
				} else {
					second.energy += first.energy / 2;
					board[first.x_coord][first.y_coord] = second.toString();
					collection.remove(first);
				}

			}

			crits = samePlace();
		}

	}

    /**
     * Display the world and the 2D array of strings
     */
	public static void displayWorld() {
		// Print top border
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		/*
		for each space, check if critter occupies it
			if so: print their symbol
			else: print an empty space
		*/
		for(int y = 0; y < Params.world_height; y++) {
			System.out.print('|');
			for(int x = 0; x < Params.world_width; x++) {
				if(board[x][y] == null) {
					System.out.print(' ');
				}
				else {
					System.out.print(board[x][y]);
				}
			}
			System.out.println('|');
		}
		//print bottom border
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}
