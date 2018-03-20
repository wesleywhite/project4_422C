package assignment4;

import assignment4.Critter.TestCritter;

public class Critter2 extends TestCritter {
	
	@Override
	public void doTimeStep() {
		walk(2); // Always walks up.
		if (getEnergy() >= Params.min_reproduce_energy) {
			Critter2 child = new Critter2();
			reproduce(child, 2); // Always placed above.
		}

	}

	@Override
	public boolean fight(String opponent) {

		return true; // Always fights
	}

	@Override
	public String toString () {
		return "2";
	}
}