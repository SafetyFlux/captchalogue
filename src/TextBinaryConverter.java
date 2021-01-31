/*
 	Author: Dallin Gardner
 	Date Created: 10/1/19
 	Date Modified: 10/7/19
 	Notes: A text-based binary to integer converter to
 		   test if the concept is possible in Java.
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class TextBinaryConverter {

	public static void main(String[] args) {

		BinaryConverter conv = new BinaryConverter();

		Scanner reader = new Scanner(System.in);
		int selection = 0;						// Selection for the menu
		int intInput = 0;						// User input for integer to binary
		String binaryInput = "";				// User input for binary to integer
		int[] newBin = new int[6];				// Output for integer to binary
		int newInt = 0;							// Output for binary to integer
		boolean err = false;					// Boolean to prevent binary to integer from giving output after invalid input
		boolean run = true;						// Boolean to keep the converter running
		System.out.println("Welcome to the binary converter!");				// Starter message (displays once)
		// Begins main program loop
		while(run){
			// Begins main selection menu
			while(selection == 0){
				System.out.println("1) Convert Integer to Binary (1 Byte)");		// Selection 1
				System.out.println("2) Convert Binary to Integer (1 Byte)");		// Selection 2
				System.out.println("3) Quit");										// Selection 3
				System.out.print("Make a selection: ");								// Prompt user for selection
				selection = reader.nextInt();
			}	// Ends main selection menu

			// Begins selection 1 menu (integer to binary; 1 byte)
			while(selection == 1){
				// Prompt user for integer
				System.out.print("Enter the integer (must be between 0 and 63) or negative # to quit: ");
				// Try-catch block for InputMismatchExceptions (e.g. entering a String instead of an int)
				try{
					intInput = reader.nextInt();
					if(intInput > 63)							// If the input is outside the binary range
						System.out.println("The number must be between 0 and 63.");
					else if(intInput < 0){						// Go back to main menu
						System.out.println();
						selection = 0;
					}
					else{
						newBin = conv.intToBinary(intInput);	// Perform the conversion via helper method
						// Display results
						System.out.print(intInput + " in binary: ");
						// Print new array of 0s and 1s on the same line
						for(int i = 0; i < newBin.length; i++)
							System.out.print(newBin[i]);
						System.out.println();					// Add new line to prevent blending
					}
				}catch(InputMismatchException ex){
					System.out.println("Error: Must be a number.");
					reader.next();								// Required to avoid endless looping
				}
			}	// Ends selection 1 menu

			// Begins selection 2 menu (binary to integer; 1 byte)
			while(selection == 2){
				err = false;	// Set err variable to false at the start of each loop
				// User input as a String so it can be converted to an array of integers
				System.out.print("Enter the binary number (format - 000000) or \"quit\" to quit: ");
				binaryInput = reader.next();
				// If the input doesn't contain the correct number of characters
				if(binaryInput.length() != 6 && !binaryInput.equalsIgnoreCase("quit"))
					System.out.println("The number must be 6 digits long (no spaces).");
				// Go back to main menu
				else if(binaryInput.equalsIgnoreCase("quit")){
					System.out.println();
					selection = 0;
				}
				// Everything else
				else{
					// The for loop begins to check the String (array of chars) and convert if the numbers are valid
					for(int i = binaryInput.length(); i > 0; i--){
						// If a character isn't 1 or 0, go back to the beginning of the converter (NOT main menu)
						if(binaryInput.charAt(i - 1) != '0' && binaryInput.charAt(i - 1) != '1'){
							System.out.println("The number must contain only 0s and 1s.");
							err = true;		// Set err to true so the rest of the code doesn't run
							break;
						}
						else{
							// Char code for 0 is 48
							if((((int)(binaryInput.charAt(i - 1))) == 48))
								newBin[i - 1] = 0;
							// Char code for 1 is 49
							else if(((int)(binaryInput.charAt(i - 1))) == 49)
								newBin[i - 1] = 1;
						}
					}
					if(!err){	// Results only show if err is false
						newInt = conv.binaryToInt(newBin);	// Perform conversion via helper method
						System.out.println(binaryInput + " as an integer: " + newInt);
					}
				}
			}	// Ends selection 2 menu

			if(selection < 0 || selection > 3){	// Statement for selections that are out-of-bounds
				/*
				   Note: 0 is not considered out-of-bounds because it's the
				   selection value for the main menu, and would otherwise show
				   up after quitting each sub-menu.
				 */
				System.out.println("Please enter a number between 1 and 5.\n");
				selection = 0;
			}
			if(selection == 3){		// Statement to quit the program
				System.out.println("Quitting...");
				run = false;
			}

		}	// Ends main program loop

	}

}
