package utility;


public class Conversion {

	public static int[] digitToBinary(char d) {
		int i = 0;
		if (d == '?')
			i = 62;
		else if (d == '!')
			i = 63;
		else
			i = DigitValues.getDigitValue(d);
		return BinaryConverter.intToBinary(i);
	}

	public static char binaryToDigit(int[] b) {
		int i = BinaryConverter.binaryToInt(b);
		return DigitValues.getDigit(i);
	}

	public static boolean checkCode(String code) {
		for (int i = 0; i < code.length(); i++) {
			if (!DigitValues.checkDigit(code.charAt(i)))
				return false;
			else
				continue;
		}
		return true;
	}

}
