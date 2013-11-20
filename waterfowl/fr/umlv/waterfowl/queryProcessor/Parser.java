package fr.umlv.waterfowl.queryProcessor;

import java.util.Iterator;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.Var;

public abstract class Parser {
	protected String query =null;
	protected ParsedQuery parsedQuery = new ParsedQuery();
	
	public Parser(String query) {
		this.query = query;
	}
	public abstract boolean isSelect();
	public abstract void parse();
	
	public void displayTriples() {
		Iterator<Triple> it = parsedQuery.getTriples().iterator();
		while(it.hasNext()) {
			Triple triple = it.next();
			System.out.println(" = "+triple.getSubject()+ " \t "+triple.getPredicate()+" \t "+triple.getObject());
		}
	}
	public void displayDistinguishedVar() {
		for(Var var : parsedQuery.getDistinguishedVars()) {
			System.out.println(var.getName());
			
		}
	}
	public ParsedQuery getParsedQuery() {
		return parsedQuery;
	}

	
}