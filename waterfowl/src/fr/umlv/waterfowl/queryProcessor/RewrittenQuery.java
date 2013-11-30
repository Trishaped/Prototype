package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;

import fr.umlv.waterfowl.sharedComponent.Triple;

public class RewrittenQuery {
	private ArrayList<String> distinguishedVars = new ArrayList<String>(); 
	private ArrayList<Triple> triples = new ArrayList<Triple>();
	
	public ArrayList<String> getDistinguishedVars() {
		return distinguishedVars;
	}
	public void addDistinguishedVar(String var) {
		distinguishedVars.add(var);
	}
	public ArrayList<Triple> getTriples() {
		return triples;
	}
	public void addTriple(Triple triple) {
		triples.add(triple);
	}
	public void displayDistinguishedVars() {
		for(String v : distinguishedVars)
			System.out.print(v+" ");
		System.out.println();
	}
	public Triple getTriple(int index)  {
		try {
			return triples.get(index);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
}
