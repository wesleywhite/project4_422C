package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Wesley White
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }
        
        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        parse(kb);
        /* Write your code above */
        System.out.flush();

    }
    
    public static String parse(Scanner keyboard) {
		String command = keyboard.nextLine();
		String split[] = command.split(" ");
		String input = split[0];


		if(input.equals("quit")) {
			System.exit(0);
		}
		else if(input.equals("show")) {
			//invoke Critter.displayWorld()
            Critter.displayWorld();
		}
		else if(input.equals("step")) {
            int amount = 1;
            if (split.length == 2)
                amount = Integer.parseInt(split[1]);

            while (amount > 0) {
                Critter.worldTimeStep();
                amount--;
            }

		}
		else if(input.equals("seed")) {
		    if (split.length == 2)
		        Critter.setSeed(Long.parseLong(split[1]));
		}
		else if(input.equals("make")) {
		    int amount = 1;
		    if (split.length == 3)
		        amount = Integer.parseInt(split[2]);
            try {
                while (amount > 0) {
                    Critter.makeCritter(split[1]);
                    amount--;
                }
            } catch (InvalidCritterException e) {
                System.out.println(e.toString());
            }
		}
		else if(input.equals("stats")) {
		    String className = split[1];

            try {
                List<Critter> temp = Critter.getInstances(className);
                Critter.runStats(temp);

            } catch (InvalidCritterException e) {
                System.out.println(e.toString());
            }

        }
		else {
			System.exit(0);
		}
		return null;
	}
}
