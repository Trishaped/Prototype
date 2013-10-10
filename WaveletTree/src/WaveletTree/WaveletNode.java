package WaveletTree;
import java.util.BitSet;


/***
 * Suite de bit
 * @author Ludo
 */
public class WaveletNode {
	private BitSet bitmap;
	private WaveletNode ln, rn;
	
	/**
	 * Return the bit at the pos i
	 */
	public Boolean access(int i){
		return bitmap.get(i);
	}
	
	/**
	 * return the number of 1 until the index i
	 */
	public int rank1(int i){
		int nb=0;
		for(int j=0;j<i;j++){
			if(bitmap.get(j)==true)
				nb ++;
		}
		return nb;
	}
	
	/**
	 * return the pos of the ith 1
	 */
	public int select1(int i){
		int nb = 0;
		int length = bitmap.length();
		for(int j=0; j<length; j++){
			if(bitmap.get(j)==true)
				nb++;
			if(nb==i)
				return j;
		}
		return -1; //Error?
	}
}
