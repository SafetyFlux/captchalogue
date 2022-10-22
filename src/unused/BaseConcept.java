package unused;

import java.io.FileNotFoundException;
import java.util.Scanner;

import utility.BinaryConverter;
import utility.DigitValues;


public class BaseConcept {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner reader = new Scanner(System.in);
		DigitValues dv = new DigitValues();
		BinaryConverter bc = new BinaryConverter();
		char digits[] = new char[8];							// A string of digits (e.g. Ag5c?a)
		int holes[] = new int[48];								// All holes - true = punched, false = not punched
		int sixBin[] = new int[6];								// Six-bit binary value
		int selection = 0;										// Menu selection
		boolean run = true;										// Keeps the program running
		boolean err = false;
		String binInput = "";									// Binary input (binary to digit)
		String strInput = "";									// String input (string to card)
		char digInput = '-';									// Digit input (digit to various values)
		int intInput = 0;										// Integer input (integer to digit)
		int iVal = 0;											// Placeholder integer for digit position in array
		while (run) {
			// Begins main selection menu
			while (selection == 0) {
				System.out.println();
				System.out.println("1) Digit to Integer");				// Convert digit to integer (A to 10)
				System.out.println("2) Integer to Digit");				// Convert integer to digit (11 to B)
				System.out.println("3) Digit to Binary");				// Convert digit to binary (A to 001010)
				System.out.println("4) Binary to Digit");				// Convert binary to digit (001011 to B)
				System.out.println("5) Digit to Line");					// Convert digit to a line of holes (A to [ ][ ][X][ ][X][ ])
				System.out.println("6) String to Card");				// Convert string of digits to entire card
				System.out.println("7) Quit");
				System.out.print("Select function: ");
				selection = reader.nextInt();
			}	// Ends main menu

			// Begins selection 1 menu (digit to integer)
			while (selection == 1) {
				System.out.print("Enter a digit ($ to quit): ");
				digInput = reader.next().charAt(0);
				if (digInput == '$')
					selection = 0;
				else if (digInput == '?')		// This and next else if must be included to translate
					System.out.println(digInput + " ==> 62");
				else if (digInput == '!')
					System.out.println(digInput + " ==> 63");
				else
					System.out.println(digInput + " ==> " + dv.getDigitValue(digInput));
			}	// Ends selection 1 menu

			// Begins selection 2 menu (integer to digit)
			while (selection == 2) {
				System.out.print("Enter an integer (- to quit): ");
				intInput = reader.nextInt();
				if (intInput < 0)
					selection = 0;
				else if (intInput > 63)
					System.out.println("Integer must be between 0 and 63.");
				else
					System.out.println(intInput + " ==> " + dv.getDigit(intInput));
			}	// Ends selection 2 menu

			// Begins selection 3 menu (digit to binary)
			while (selection == 3) {
				System.out.print("\nEnter a digit ($ to quit): ");
				digInput = reader.next().charAt(0);
				if (digInput == '$')
					selection = 0;
				else if (digInput == '?') {		// This and next else if must be included to translate
					iVal = 62;
				}
				else if (digInput == '!') {
					iVal = 63;
				}
				else {
					iVal = dv.getDigitValue(digInput);
				}
				sixBin = bc.intToBinary(iVal);
				System.out.print(digInput + " ==> ");
				for (int i = 0; i < sixBin.length; i++)
					System.out.print(sixBin[i]);
			}	// Ends selection 3 menu

			// Begins selection 4 menu (binary to digit)
			while (selection == 4) {
				err = false;
				System.out.print("Enter six bits (\"quit\" to quit): ");
				binInput = reader.next();
				if (binInput.length() != 6 && !binInput.equalsIgnoreCase("quit"))
					System.out.println("The number must be 6 digits long (no spaces).");
				else if (binInput.equalsIgnoreCase("quit")) {
					System.out.println();
					selection = 0;
				}
				else {
					for (int i = binInput.length(); i > 0; i--) {
						if (binInput.charAt(i - 1) != '0' && binInput.charAt(i - 1) != '1') {
							System.out.println("The number must contain only 0s and 1s.");
							err = true;
							break;
						}
						else {
							// Char code for 0 is 48
							if ((((int)(binInput.charAt(i - 1))) == 48))
								sixBin[i - 1] = 0;
							// Char code for 1 is 49
							else if (((int)(binInput.charAt(i - 1))) == 49)
								sixBin[i - 1] = 1;
						}
					}
					if (!err) {
						iVal = bc.binaryToInt(sixBin);
						System.out.println(binInput + " ==> " + dv.getDigit(iVal));
					}
				}
			}	// Ends selection 4 menu

			// Begins selection 5 menu (digit to punched line)
			while (selection == 5) {
				System.out.print("Enter a digit ($ to quit): ");
				digInput = reader.next().charAt(0);
				if (digInput == '$')
					selection = 0;
				else if (digInput == '?') {		// This and next else if must be included to translate
					iVal = 62;
				}
				else if (digInput == '!') {
					iVal = 63;
				}
				else {
					iVal = dv.getDigitValue(digInput);
				}
				sixBin = bc.intToBinary(iVal);
				for (int i = 0; i < sixBin.length; i++) {
					if (sixBin[i] == 0)
						System.out.print("[ ]");
					else
						System.out.print("[X]");
				}
				System.out.println();
			}	// Ends selection 5 menu

			// Begins selection 6 menu (string to card)
			while (selection == 6) {
				System.out.print("Enter a string of digits (or \"quit\" to quit): ");
				strInput = reader.next();
				if (strInput.equals("quit"))
					selection = 0;
				else if (strInput.length() != 8)
					System.out.println("The string must be 8 digits long.");
				else {
					for (int i = 0; i < digits.length; i++)
						digits[i] = strInput.charAt(i);
					for (int i = 0; i <  holes.length; i+=6) {
						if (digits[i / 6] == '?')
							iVal = 62;
						else if (digits[i / 6] == '!')
							iVal = 63;
						else
							iVal = dv.getDigitValue(digits[i / 6]);
						sixBin = bc.intToBinary(iVal);
						for (int j = 0; j < sixBin.length; j++) {
							holes[i + j] = sixBin[j];
						}
					}
					for (int i = 12; i > 0; i--) {
						for (int j = 0; j < 4; j++) {
							if (holes[(12 * j) + (12 - i)] == 0)
								System.out.print("[ ]");
							else
								System.out.print("[X]");
						}
						System.out.println();
					}
				}
			}

			if (selection < 0 || selection > 7) {		// Statement for selections that are out-of-bounds
				System.out.println("Please enter a number between 1 and 7.\n");
				selection = 0;
			}

			if (selection == 7) {		// Statement to quit the program
				System.out.println("Quitting...");
				reader.close();
				run = false;
			}

		}

	}

}
