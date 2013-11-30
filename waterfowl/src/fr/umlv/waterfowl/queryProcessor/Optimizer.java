package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.sharedComponent.Triple;

public class Optimizer {
	private Set<String> processedVars;
	private RewrittenQuery rewrittenQuery;
	private Dictionary dictionary;
	private ArrayList<OptimTriple> optimizedSAPs = new ArrayList<OptimTriple>();
	private ArrayList<Integer> triplePlan;
	private int tripleOrder=0;
	
	public Optimizer(RewrittenQuery rewrittenQuery, Dictionary dictionary) {
		this.dictionary = dictionary;
		this.rewrittenQuery  = rewrittenQuery;
	}
	public void init() {
		int counter = 0;
		for(Triple triple : rewrittenQuery.getTriples())
			optimizedSAPs.add(new OptimTriple(counter++));
		processedVars = new TreeSet<String>();
		triplePlan = new ArrayList<Integer>();
	}
	
	// heuristic 1 based on triple's selectivity : spo < s?o < ?po < sp? < ??o < s?? < ?p? < ???
	public void orderByType() {
		int counter=0;
		int varCounter = 0;
		int type = 0;
		for(Triple triple : rewrittenQuery.getTriples()) {
			varCounter = variableCounter(triple);
			switch(varCounter) {
			case 0 : type=0; break;
			case 1 : type = computeOneVarType(triple); break;
			case 2 : type =  computeTwoVarType(triple); break;
			case 3 : type=7; break;
			}
			optimizedSAPs.get(counter).setType(type);
			counter++;
		}
		Collections.sort(optimizedSAPs, new OptimTripleTypeComparator());
	}
	public int variableCounter(Triple triple) {
		int varCounter = 0;
		if(isVariable(triple.getSubject()))
			varCounter++;
		if(isVariable(triple.getPredicate()))
			varCounter++;
		if(isVariable(triple.getObject()))
			varCounter++;
		return varCounter;
	}
	public int computeOneVarType(Triple triple) {
		if(isVariable(triple.getPredicate()))
			return 1;
		else
			if(isVariable(triple.getSubject()))
				return 2;
			else
				return 3;
	}
	public int computeTwoVarType(Triple triple) {
		if(isVariable(triple.getSubject()) && isVariable(triple.getPredicate()))
			return 4;
		else
			if(isVariable(triple.getObject()) && isVariable(triple.getPredicate()))
				return 5;
			else
				return 6;
	}
	public void triplePlanner() {
		while(optimizedSAPs.size()>0) {
			ArrayList<Integer> tripleCandidates = new ArrayList<Integer>();
			int counter = 0;
			if(triplePlan.size()==0) {
				while(counter< optimizedSAPs.size()) {
					tripleCandidates.add(counter);
					counter++;
				}
			} else { 
				for(OptimTriple optimTriple : optimizedSAPs) {
					if(isJoinable(rewrittenQuery.getTriples().get(optimTriple.getQueryNumber()))) {
						tripleCandidates.add(counter);
					}
					counter++;
				}
			}

			int candidate = 0;
			// heuristic 1, retain lowest optimTriple's type
			if(tripleCandidates.size()<=1)
				candidate = 0;
			else {
				tripleCandidates = retainLowestTypeCandidates(tripleCandidates);
				Long minSelectivity = null;
				for(Integer index : tripleCandidates ) {
					if(minSelectivity==null) {
						minSelectivity = getSelectivity(rewrittenQuery.getTriples().get(optimizedSAPs.get(index).getQueryNumber()));
						candidate = index;
					}
					else
						if(minSelectivity > getSelectivity(rewrittenQuery.getTriples().get(optimizedSAPs.get(index).getQueryNumber()))) {
							minSelectivity = getSelectivity(rewrittenQuery.getTriples().get(optimizedSAPs.get(index).getQueryNumber()));
							candidate = index;
						}
				}
			}
			triplePlan.add(optimizedSAPs.get(candidate).getQueryNumber());
			addToProcessedVars(rewrittenQuery.getTriples().get(optimizedSAPs.get(candidate).getQueryNumber()));
			optimizedSAPs.remove(candidate);
		}
	}
	public ArrayList<Integer> retainLowestTypeCandidates(ArrayList<Integer> tripleCandidates) {
		ArrayList<Integer> tmpCandidates = new ArrayList<Integer>();
		int type = optimizedSAPs.get(tripleCandidates.get(0)).getType();
		tmpCandidates.add(tripleCandidates.get(0));
		int counter = 1;
		while(counter<tripleCandidates.size()) {
			if(optimizedSAPs.get(tripleCandidates.get(counter)).getType()==type)
				tmpCandidates.add(tripleCandidates.get(counter));
			else 
				return tmpCandidates;
			counter++;
		}
		return tmpCandidates;		
	}
	public boolean isJoinable(Triple triple) {
		for(String var : processedVars) {
			if(var.equals(triple.getSubject()) || var.equals(triple.getPredicate()) || var.equals(triple.getObject()))
				return true;
		}
		return false;
	}
	public boolean isVariable(String element) {
		try {
			Long val = Long.parseLong(element);
			return false;
		}catch (NumberFormatException e) {
			return true;
		}
	}
	public void addToProcessedVars(Triple triple) {
		if(isVariable(triple.getSubject()))
			processedVars.add(triple.getSubject());
		if(isVariable(triple.getPredicate()))
			processedVars.add(triple.getPredicate());
		if(isVariable(triple.getObject()))
			processedVars.add(triple.getObject());
	}
	public Long getSelectivity(Triple triple) {
		// returned selectivity is the first positive value found from object to subject
		if(dictionary.getStatO(triple.getObject())!=-1)
			return dictionary.getStatO(triple.getObject());
		else
			if(dictionary.getStatP(triple.getObject())!=-1)
				return dictionary.getStatP(triple.getObject());
			else
				return dictionary.getStatS(triple.getObject());
	}

	public void displayOptimizedSAPs() {
		for(OptimTriple optimTriple : optimizedSAPs)
			optimTriple.prettyPrint();
	}
	public void displayProcessedVars() {
		for(String s : processedVars) {
			System.out.println(s);
		}
	}
	public ArrayList<Integer> getTriplePlan() {
		return triplePlan;
	}
	public RewrittenQuery getRewrittenQuery() {
		return rewrittenQuery;
	}	
}
