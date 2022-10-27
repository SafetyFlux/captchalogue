package utility;

import java.util.Random;


public class Randomize {

	static Random gen = new Random();
	private static char[] digits = DigitValues.getDigitArray();
	
	public static char getChar() {
		
		char ch = '0';
		int num = gen.nextInt(64);
		ch = digits[num];
		
		return ch;
		
	}

}
