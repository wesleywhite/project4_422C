package assignment4;

import assignment4.Critter.TestCritter;

public class Critter1 extends TestCritter {
	
	@Override
	public void doTimeStep() {
	    walk(0); // Always walks to the right.
        if (getEnergy() >= Params.min_reproduce_energy) {
            Critter1 child = new Critter1();
            reproduce(child, 0); // Always placed to the right.
        }

	}

	@Override
	public boolean fight(String opponent) {

		return true; // Always fights.
	}

	@Override
	public String toString () {
		return "1";
	}
}
