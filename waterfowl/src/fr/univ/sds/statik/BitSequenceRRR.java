package fr.univ.sds.statik;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class BitSequenceRRR {
	private static final int SIZE=1000000;
	private static final int SUPERBLOCKSIZE = 3;
	private static final int BLOCKSIZE=5;
	private BitPermutation bitPerm = new BitPermutation(BLOCKSIZE);
	private ArrayList<BlockAbstract> blocks = new ArrayList<BlockAbstract>();
	private ArrayList<Long> sumOffset = new ArrayList<Long>(SIZE/(SUPERBLOCKSIZE*BLOCKSIZE));
	
   private BitSet bitSet = new BitSet();
   
   public BitSequenceRRR() {
	   bitPerm.build();
   }
   public void init() {
	   for(int i=0; i<SIZE;i++)
		  if(i%2==0)
			  bitSet.set(i);
//	   System.out.println(bitSet.toString());
	   }
	public void build() {
	    BitSet bitSetTmp = new BitSet();
	    long sum = 0;		
		for(int h=0; h<SIZE/(SUPERBLOCKSIZE*BLOCKSIZE);h++) {
			for(int i=0; i<SUPERBLOCKSIZE;i++) {
				for(int j=0; j<BLOCKSIZE;j++) {
					if(bitSet.get(j+((h*SUPERBLOCKSIZE*BLOCKSIZE)+(BLOCKSIZE*i))))
					      bitSetTmp.set(j);
				}
				BlockAbstract ba = getBlockAbstract(bitSetTmp);
				 sum += ba.getRank();
				 blocks.add(ba);
				 bitSetTmp.clear();
			}   
		 sumOffset.add(sum);
		}
	  }
	public BlockAbstract getBlockAbstract(BitSet bitSet ) {
		int rank = bitSet.cardinality();
		int blockId = bitPerm.getId(rank, bitSet);
		return new BlockAbstract((byte) rank, (byte)blockId);
	}
	public void displayBlockAbstracts() {
		Iterator<BlockAbstract> it = blocks.iterator();
		while(it.hasNext()) {
			it.next().display();
		}
	}
	public void displaySumOffset()  {
		for(Long l : sumOffset) {
			System.out.println(" -"+l+ " ");
		}
	}
	public long rank0(int index) {
		return index-rank1(index);
	}
	public long rank1(int index) {
		long result=0;
		if (index>SIZE || index<0) throw new IllegalArgumentException("index out of range");
		int ib = index/BLOCKSIZE;
		int is = ib/SUPERBLOCKSIZE;
		if(is>0)
			result = sumOffset.get(is-1);
		for(int i=(SUPERBLOCKSIZE*is); i<ib;i++ )
			result += blocks.get(i).getRank();
		BitSet bs = bitPerm.getPermutations().get(blocks.get(ib).getRank()-1).getToBitSet().get(blocks.get(ib).getBlockId()-1);
		for(int i=(index%BLOCKSIZE);i<BLOCKSIZE;i++) {
			bs.clear(i);
		}
		result += bs.cardinality();
		return result;
		
	}
	
	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		BitSequenceRRR bp = new BitSequenceRRR();
		bp.init();
		bp.build();
		long end= System.currentTimeMillis();
		System.out.println("Done in "+(end-start)+" ms");
//		bp.displayBlockAbstracts();
		System.out.println("rank1 :"+bp.rank1(98745));
		System.out.println("rank0 :"+bp.rank0(3));
		
	}
}
