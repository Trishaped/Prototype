package Data;


/***
 * (select, rank, access)
 * @author Ludo
 */
public class WaveletNode {
	private Bitmap bitmap;
	public WaveletNode ln, rn;
	private int depth; //Pour l'affichage
	
	public WaveletNode(int depth){
		bitmap = new Bitmap();
		ln = null;
		rn = null;
		this.depth = depth;
	}
	
	public void print(){
		bitmap.print();
		System.out.print(" | ");
	}
	
	public int depth(){
		if(ln==null && rn==null)
			return 1;
		else if(ln!=null && rn!=null)
			return Math.max(ln.depth()+1, rn.depth()+1);
		else if(ln!=null)
			return ln.depth()+1;
		else // rn!=null
			return rn.depth()+1;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public void set(Bitmap bm, int bmIndex){
		if(bmIndex>bm.getSize())
			return;
		boolean bit = bm.access(bmIndex);
		bitmap.set(bitmap.getSize(), bit);
		if(bit){
			if(rn==null)
				rn = new WaveletNode(this.depth+1);
			rn.set(bm, bmIndex+1);
		}
		else{
			if(ln==null)
				ln = new WaveletNode(this.depth+1);
			ln.set(bm, bmIndex+1);
		}
	}
	
	public Bitmap access(int i, Bitmap bm, int bmIndex){
		//On récupère le bit et on l'insère dans le bitmap de resultat
		boolean bit = bitmap.access(i);
		bm.set(bmIndex, bit);
		
		if(bit){ //Bit à 1, on va a droite
			int nb1 = bitmap.rank1(i); //On compte le nombre de 1 jusqu'a l'index pour savoir ou acceder à la couche du dessous
			if(rn==null)
				return bm;
			return rn.access(nb1, bm, bmIndex+1);
		}
		else{
			int nb0 = i - bitmap.rank1(i); 
			if(ln==null)
				return bm;
			return ln.access(nb0, bm, bmIndex+1);
		}

	}
	
}
