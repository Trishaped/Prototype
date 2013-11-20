package Data;
import java.util.LinkedList;
import java.util.List;

public class Main {
	
	public static void main(String args[]){
		WaveletTree tree = new WaveletTree(testData());
		tree.print();		
	}
	
	private static List<Bitmap> testData(){
		List<Bitmap> test = new LinkedList<Bitmap>();
		test.add(new Bitmap("011100110101011001"));
		test.add(new Bitmap("100101000110110100"));
		test.add(new Bitmap("110101001100110101"));
		test.add(new Bitmap("101001101010101010"));		
		return test;		
	}
}
