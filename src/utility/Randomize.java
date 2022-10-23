package utility;

import java.util.Random;


public class Randomize {

	Random gen = new Random();
	DigitValues dv = new DigitValues();
	private char[] digits = dv.getDigitArray();
	
	public Randomize() { }
	
	public char getChar() {
		
		char ch = '0';
		int num = gen.nextInt(64);
		ch = digits[num];
		
		return ch;
		
	}

}
