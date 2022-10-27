package utility;


public class Alchemize {

	public static int[][] functionAND(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == 1 && b[i][j] == 1)
					c[i][j] = 1;
				else
					c[i][j] = 0;
			}
		}
		return c;
	}
	
	public static int[][] functionOR(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == 0 && b[i][j] == 0)
					c[i][j] = 0;
				else
					c[i][j] = 1;
			}
		}
		return c;
	}
	
	public static int[][] functionXOR(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == b[i][j])
					c[i][j] = 0;
				else
					c[i][j] = 1;
			}
		}
		return c;
	}
	
	public static int[][] functionNAND(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == 1 && b[i][j] == 1)
					c[i][j] = 0;
				else
					c[i][j] = 1;
			}
		}
		return c;
	}
	
	public static int[][] functionNOR(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == 0 && b[i][j] == 0)
					c[i][j] = 1;
				else
					c[i][j] = 0;
			}
		}
		return c;
	}
	
	public static int[][] functionXNOR(int[][] a, int[][] b) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == b[i][j])
					c[i][j] = 1;
				else
					c[i][j] = 0;
			}
		}
		return c;
	}
	
	public static int[][] functionNOT(int[][] a) {
		int[][] c = new int[8][6];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				if (a[i][j] == 0)
					c[i][j] = 1;
				else
					c[i][j] = 0;
			}
		}
		return c;
	}
	
}
