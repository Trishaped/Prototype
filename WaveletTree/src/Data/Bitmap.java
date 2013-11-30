package Data;

import java.util.BitSet;

/*
 * Comme un bitset mais comporte la taille de celui-ci ainsi que les fonction de base (select, rank, access)
 * permet de gérér les exception (pas de trou dans le bitset et on ne dépasse pas sa taille en lecture)
 */
public class Bitmap extends BitSet{
	private int size;

	public Bitmap(int size) {
		super(size);
		this.size = 0;
	}
	
	public Bitmap(){
		super();
		this.size = 0;
	}
	
	/**
	 * Pour les test on peut créer un bitmap depuis une chaine
	 */
	public Bitmap(String values){
		this.size = values.length();
		for(int i=0; i<size; i++){
			super.set(i, values.charAt(i)=='1'?true:false);
		}
	}
	
	public int getSize(){
		return size;
	}
	
	public void print(){
		for(int i=0; i<size; i++){
			System.out.print(get(i)==true?"1":"0");
		}
	}
	
	/*
	 * On essaye de setter des bits trop loin (de laisser un trou dans le bitset)
	 * On est censer remplir le bitset dans l'ordre
	 */
	@Override
	public void set(int bitIndex, boolean value) {
		if(bitIndex>size+1)
			throw new RuntimeException();
		super.set(bitIndex, value);
		size++;
	}
	
	@Override
	public boolean get(int bitIndex) {
		if(bitIndex>size)
			throw new RuntimeException();
		return super.get(bitIndex);
	}
	
	/**
	 * Return the value of the nth bit
	 */
	public boolean access(int bitIndex){
		return get(bitIndex);
	}
	
	/**
	 * return the number of 1 until the index i
	 */
	public int rank1(int i){
		int nb=0;
		for(int j=0;j<i;j++){
			if(get(j)==true)
				nb ++;
		}
		return nb;
	}
	
	/**
	 * return the pos of the ith 1
	 */
	public int select1(int i){
		int nb = 0;
		int length = size;
		for(int j=0; j<length; j++){
			if(get(j)==true)
				nb++;
			if(nb==i)
				return j;
		}
		throw new RuntimeException("Unknown exception for the moment..."); //Error?
	}
	
}
