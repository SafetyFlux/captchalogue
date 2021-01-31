import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class DigitValues {

	private char digits[] = new char[64];
	private BinarySearch bs = new BinarySearch();
	
	public DigitValues() throws FileNotFoundException{

		Scanner digScan = new Scanner(new File("res/Digits.txt"));
		
		int count = 0;
		
		while(count < digits.length && digScan.hasNext()){
			char character = digScan.nextLine().charAt(0);
			digits[count] = character;
			count++;
		}
		
		if(digScan.hasNext())
			System.out.println("Some data lost during digit input");
		
		//for (int i = 0; i < digits.length; i++)
			//System.out.println(digits[i]);
		
	}
	
	public char getDigit(int d){
		return digits[d];
	}
	
	public int getDigitValue(char d){
		int charAt = bs.charSearch(digits, d);
		return charAt;
	}

}
