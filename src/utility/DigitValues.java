package utility;


public class DigitValues {

	private char[] digits = 
		{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
	     'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
	     'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
	     'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '?', '!'};
	private BinarySearch bs = new BinarySearch();
	
	public DigitValues() { }
	
	public char[] getDigitArray() {
		return digits;
	}
	
	public char getDigit(int d) {
		return digits[d];
	}
	
	public int getDigitValue(char d) {
		int charAt = bs.charSearch(digits, d);
		return charAt;
	}
	
	public boolean checkDigit(char d) {
		boolean good = false;
		for (int i = 0; i < digits.length; i++) {
			if (d == digits[i]) {
				good = true;
				break;
			}
			else
				good = false;
		}
		return good;
	}

}
