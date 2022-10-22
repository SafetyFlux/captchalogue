package utility;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class Randomize {

	Random gen = new Random();
	private char digits[] = new char[64];
	
	public Randomize() throws FileNotFoundException{
		
		Scanner digScan = new Scanner(new File("res/Digits.txt"));
		
		int count = 0;
		
		while(count < digits.length && digScan.hasNext()){
			char character = digScan.nextLine().charAt(0);
			digits[count] = character;
			count++;
		}
		
		digScan.close();
		
	}
	
	public char getChar(){
		
		char ch = '0';
		int num = gen.nextInt(64);
		ch = digits[num];
		
		return ch;
		
	}

}
