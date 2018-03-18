package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */


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
	private static String[][] board = new String[Params.world_height][Params.world_width]; // I think you had width and height flipped.

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

	
	protected final void walk(int direction) {
		//this needs fixing so that the critter who walks has its coordinates updated
		//Q5 in FAQ talks about it but it didn't really help me
		//also view needs to be updated with coordinate changes
		//Params.walk_energy_cost needs to be deducted from Critter's energy

		this.energy -= Params.walk_energy_cost; // Deduct the walk energy cost

		switch(direction) {
		//(1,0)
		case 0: x_coord += 1;
				break;
		//(1,1)
		case 1: x_coord += 1;
				y_coord += 1;
				break;
		//(0,1)
		case 2: y_coord += 1;
				break;
		//(-1,1)
		case 3: x_coord -= 1;
				y_coord += 1;
				break;
		//(-1,0)
		case 4: x_coord -= 1;
				break;
		//(-1,-1)
		case 5: x_coord -= 1;
				y_coord -= 1;
				break;
		//(0,-1)
		case 6:	y_coord -= 1;
				break;
		//(1,-1)
		case 7: x_coord += 1;
				y_coord -= 1;
				break;
		}
	}
	
	protected final void run(int direction) {

		//this needs fixing so that the critter who walks has its coordinates updated
		//Q5 in FAQ talks about it but it didn't really help me
		//also view needs to be updated with coordinate changes

		this.energy -= Params.run_energy_cost; // Deduct the run energy cost

		switch(direction) {
			//(2,0)
			case 0: x_coord += 2;
				break;
			//(2,2)
			case 1: x_coord += 2;
				y_coord += 2;
				break;
			//(0,2)
			case 2: y_coord += 2;
				break;
			//(-2,2)
			case 3: x_coord -= 2;
				y_coord += 2;
				break;
			//(-2,0)
			case 4: x_coord -= 2;
				break;
			//(-2,-2)
			case 5: x_coord -= 2;
				y_coord -= 2;
				break;
			//(0,-2)
			case 6:	y_coord -= 2;
				break;
			//(2,-2)
			case 7: x_coord += 2;
				y_coord -= 2;
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
			this.energy = (int) Math.ceil(this.energy / 2); // 1/2 rounding up

			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord; // Give parent's coordinates

			offspring.walk(direction);
			offspring.energy += Params.walk_energy_cost; // Temporary fix.

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
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
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
			return population;
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
		for(Critter crit : collection) {
			collection.remove(crit);
		}
		// Clear the board
		for (int i = 0; i < Params.world_height; i++) {
			for (int j = 0; j < Params.world_width; j++) {
				board[i][j] = null;
			}
		}
	}
	
	public static void worldTimeStep() {
		//FAQ says that this is the order of stuff in worldTimeStep
		// 1. increment timestep; timestep++;
		timestep++;
		// 2. doTimeSteps(); This is where wach critter will call walk/run
		for(Critter crit : collection) {
			crit.doTimeStep();
		}
		// 3. Do the fights. doEncounters();


		// 4. updateRestEnergy();

		for(Critter crit : collection) {
			crit.energy -= Params.rest_energy_cost;
			if (crit.energy <= 0)
				collection.remove(crit); // Dead critters are removed.
		}

		// 5. Generate Algae genAlgae();
		// 6. Move babies to general population. population.addAll(babies); babies.clear();
		collection.addAll(babies);
		babies.clear();
	}
	
	public static void displayWorld() {
		// Complete this method.
		//print top border
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
		for(int x = 0; x < Params.world_width; x++) {
			System.out.print('|');
			for(int y = 0; y < Params.world_height; y++) {
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
