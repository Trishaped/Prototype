package fr.umlv.waterfowl.utils;

import java.util.BitSet;


public class BitSetConverter {
	public BitSet concat(BitSet prefix, BitSet bs, int length) {
		if(prefix.cardinality()>0) {
			for(int i=0; i<prefix.size();i++) {
				if(prefix.get(i))
					bs.set(length+i);
			}
		}
		return bs;
	}
	public BitSet convert(int val) {
		BitSet bitSet = new BitSet();
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
}
