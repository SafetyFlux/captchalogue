import java.io.FileNotFoundException;


public class Conversion {

	private DigitValues dv = new DigitValues();
	private BinaryConverter bc = new BinaryConverter();
	
	public Conversion() throws FileNotFoundException{
		
	}
	
	public int[] digitToBinary(char d){
		int i = 0;
		if(d == '!')
			i = 62;
		else if(d == '?')
			i = 63;
		else
			i = dv.getDigitValue(d);
		return bc.intToBinary(i);
	}
	
	public char binaryToDigit(int[] b){
		int i = bc.binaryToInt(b);
		return dv.getDigit(i);
	}
	
	public int[][] functionAND(int[][] a, int[][] b){
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if(a[i][j] == 1 && b[i][j] == 1)
					c[i][j] = 1;
				else
					c[i][j] = 0;
			}
		}
		return c;
	}
	
	public int[][] functionOR(int[][] a, int[][] b){
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if(a[i][j] == 0 && b[i][j] == 0)
					c[i][j] = 0;
				else
					c[i][j] = 1;
			}
		}
		return c;
	}

}
