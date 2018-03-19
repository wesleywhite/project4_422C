package assignment4;

import assignment4.Critter.TestCritter;

public class Critter3 extends TestCritter {
	
	@Override
	public void doTimeStep() {
		run(4); // Always runs to the left.
		if (getEnergy() >= Params.min_reproduce_energy) {
			Critter3 child = new Critter3();
			reproduce(child, 4); // Always placed to the left.
		}
	}

	@Override
	public boolean fight(String opponent) {
        return getEnergy() >= (Params.start_energy / 2); // Fights if it has at least half of the starting energy
    }

	@Override
	public String toString () {
		return "3";
	}
}