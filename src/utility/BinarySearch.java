package utility;

public class BinarySearch {

	public BinarySearch() {}
	
	public int intSearch(int[] a, int searchValue) {
		int left = 0;
		int right = a.length - 1;
		while (left <= right) {
			int midpoint = (left + right) / 2;
			if(a[midpoint] == searchValue)
				return midpoint;
			else if(a[midpoint] < searchValue)
				left = midpoint + 1;
			else
				right = midpoint - 1;
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public int objectSearch(Object[] a, Object searchValue) {
		int left = 0;
		int right = a.length - 1;
		while (left <= right) {
			int midpoint = (left + right) / 2;
			@SuppressWarnings("rawtypes")
			int result = ((Comparable)a[midpoint]).compareTo(searchValue);
			if(result == 0)
				return midpoint;
			else if(result < 0)
				left = midpoint + 1;
			else
				right = midpoint - 1;
		}
		return -1;
	}
	
	public int charSearch(char[] a, char searchValue) {
		int left = 0;
		int right = a.length - 1;
		while (left <= right) {
			int midpoint = (left + right) / 2;
			if(a[midpoint] == searchValue)
				return midpoint;
			else if(a[midpoint] < searchValue)
				left = midpoint + 1;
			else
				right = midpoint - 1;
		}
		return -1;
	}

	
	
	
	
}
