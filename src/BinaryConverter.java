
public class BinaryConverter {

		public int[] intToBinary(int num){
			int[] bin = new int[6];
			for (int i = 5; i >= 0; i--) {
				if((num - (Math.pow(2, i)) >= 0)){
					bin[5 - i] = 1;
					num -= (Math.pow(2, i));
				}
				else
					bin[5 - i] = 0;
			}
			return bin;
		}

		public int binaryToInt(int[] nums){
			int inty = 0;
			for(int i = 5; i >= 0; i--){
				if(nums[5 - i] == 0)
					inty += 0;
				else
					inty += (Math.pow(2, i));
			}
			return inty;
		}
	
}
