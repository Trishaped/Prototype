package fr.univ.sds.statik;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BitPermutation {
	private int seqLen;
	private ArrayList<PermutationContainer> permutations = new ArrayList<PermutationContainer>();
	PermutationContainer permContainer; 

	public BitPermutation(int seqLen) {
		this.seqLen = seqLen;
	}
	public void build() {
		for(int i = 1; i < seqLen; i++) { 
			permContainer = new PermutationContainer();
			BitSet perm = getFirst(i);
			storePermutation(0,perm);
			int binomial = factorial(seqLen) / (factorial(i)*factorial(seqLen-i));

			for(int j=1; j<binomial;j++) {
				perm = nextPermutation(convert(perm));
				storePermutation(j,perm);
			}
			permutations.add(permContainer);
		}
	}
	public void storePermutation(int index, BitSet perm) {
		permContainer.getToBitSet().put(index, perm);
		permContainer.getToId().put(perm, index);		
	}
	
	public BitSet getFirst(int ones) {
		return convert((1 << ones) -1);
	}
	
	public BitSet nextPermutation (int ones) {
		int t = ( ones | ones -1) +1;
		int w = t | ((((t & -t) / (ones & -ones)) >> 1)  - 1);
		return convert(w);
	}
	public BitSet convert(int val) {
		BitSet bitSet = new BitSet(seqLen);
		int index = 0;
	    while (val != 0L) {
	      if (val % 2L != 0) {
	        bitSet.set(index);
	      }
	      ++index;
	      val = val >>> 1;
	    }
	    return bitSet;
	}
	public int convert(BitSet bits) {
	    int value = 0;
	    for (int i = 0; i < bits.length(); ++i) {
	      value += bits.get(i) ? (1 << i) : 0;
	    }
	    return value;
	  }
	public static int factorial(int n) {
        int fact = 1; 
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
	public int getId(int rank, BitSet bitSet) {
		if(rank==0 || rank==(seqLen-1))
			return 0;
		else {
                Integer index = permutations.get(rank-1).getToId().get(bitSet);
				if(index==null)
					return -1;
				else
					return index;
		}
	}

	public void display() {
		Iterator<PermutationContainer> it = permutations.iterator();
		int index=0;
		while(it.hasNext()) {
			Set<Map.Entry<Integer, BitSet>> set = it.next().getToBitSet().entrySet();;
			for(Map.Entry<Integer, BitSet> map : set) {
				System.out.println(map.getKey() + " -> "+map.getValue());
			}
			++index;
		}
	}
	public void displayId() {
		Iterator<PermutationContainer> it = permutations.iterator();
		int index=0;
		while(it.hasNext()) {
			System.out.println(index);
			Set<Map.Entry<BitSet,Integer>> set = it.next().getToId().entrySet();;
			for(Map.Entry<BitSet,Integer> map : set) {
				System.out.println(map.getKey() + " -> "+map.getValue());
			}
			++index;
		}
	}
	
	public int getSeqLen() {
		return seqLen;
	}
	public void setSeqLen(int seqLen) {
		this.seqLen = seqLen;
	}
	public ArrayList<PermutationContainer> getPermutations() {
		return permutations;
	}
	public PermutationContainer getPermContainer() {
		return permContainer;
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		BitPermutation bp = new BitPermutation(5);
		bp.build();
		long end= System.currentTimeMillis();
		System.out.println("Done in "+(end-start)+" ms");
		bp.displayId();
	}

}
