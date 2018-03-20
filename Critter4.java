package assignment4;

import assignment4.Critter.TestCritter;

public class Critter4 extends TestCritter {
	
	@Override
	public void doTimeStep() {
		walk(6); // Always walks down one.
		if (getEnergy() >= Params.min_reproduce_energy) {
			Critter4 child = new Critter4();
			reproduce(child, 6); // Always placed below.
		}
	}

	@Override
	public boolean fight(String opponent) {

	    return (opponent.equals("@")); // Only fights algae
	}

	@Override
	public String toString () {
		return "4";
	}
}
