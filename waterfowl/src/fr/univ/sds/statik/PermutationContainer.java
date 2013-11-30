package fr.univ.sds.statik;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class PermutationContainer {
	private Map<Integer,BitSet> toBitSet;
	private Map<BitSet, Integer> toId;
	
	public PermutationContainer() {
		toBitSet = new HashMap<Integer,BitSet>();
		toId = new HashMap<BitSet,Integer>();
	}

	public Map<Integer, BitSet> getToBitSet() {
		return toBitSet;
	}

	public Map<BitSet, Integer> getToId() {
		return toId;
	}
	
}
