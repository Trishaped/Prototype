package fr.umlv.waterfowl.queryProcessor;

import java.util.Comparator;


public class OptimTripleTypeComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		Integer type1 = ((OptimTriple) o1).getType();
		Integer type2 = ((OptimTriple) o2).getType();
		return type1.compareTo(type2);
	}
}
