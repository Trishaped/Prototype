package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.Var;

public class ParsedQuery {
	private ArrayList<Var> distinguishedVars = new ArrayList<Var>(); 
	private ArrayList<Triple> triples = new ArrayList<Triple>();
	
	public ArrayList<Var> getDistinguishedVars() {
		return distinguishedVars;
	}
	public void addDistinguishedVar(Var var) {
		distinguishedVars.add(var);
	}
	public ArrayList<Triple> getTriples() {
		return triples;
	}
	public void addTriple(Triple triple) {
		triples.add(triple);
	}
}
