package Data;
import java.util.LinkedList;
import java.util.List;


/*
 * Un WaveletTree
 */
public class WaveletTree {
	/*
	 * La racine
	 */
	private WaveletNode tree;
	
	public WaveletTree(List<Bitmap> values){
		tree = new WaveletNode(0);
		for(Bitmap bp : values){
			bp.print();
			System.out.println();
			tree.set(bp, 0);
		}
	}
	
	public int depth(){
		return tree.depth();
	}
	
	public void print(){
		List<WaveletNode> list = new LinkedList<WaveletNode>();
		list.add(tree);
		int depth = -1;
		
		while(!list.isEmpty()){
			WaveletNode node = list.remove(0);
			int depthTmp = node.getDepth();
			if(depthTmp!=depth){
				System.out.println();
				depth=depthTmp;
			}
			node.print();
			if(node.ln!=null)
				list.add(node.ln);
			if(node.rn!=null)
				list.add(node.rn);
			//System.out.println();
		}
	}
}
