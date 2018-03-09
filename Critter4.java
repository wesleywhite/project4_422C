package assignment4;

import assignment4.Critter.TestCritter;

public class Critter4 extends TestCritter {
	
	@Override
	public void doTimeStep() {
	}

	@Override
	public boolean fight(String opponent) {

		return true;
	}

	@Override
	public String toString () {
		return "4";
	}
}
