package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.core.Var;

import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.dictionary.TBoxHandler;
import fr.umlv.waterfowl.sharedComponent.Triple;
import fr.umlv.waterfowl.sharedComponent.TripleComponent;

public class Rewrite {
	private ParsedQuery parsedQuery ;
	private Dictionary dictionary;
	private TBoxHandler tbox = null;
    private Explanation explanation;
	private RewrittenQuery rewrittenQuery = new RewrittenQuery(); 
	
	public Rewrite(ParsedQuery parsedQuery, Dictionary dictionary, Explanation explanation) {
		this.parsedQuery = parsedQuery;
		this.dictionary = dictionary;
		this.explanation = explanation;
	}
	public Rewrite(ParsedQuery parsedQuery, TBoxHandler tbox, Explanation explanation) {
		this.parsedQuery = parsedQuery;
		this.tbox = tbox; 
		this.dictionary = null;
		this.explanation = explanation;
	}
	public void rewrite() {
	    if(tbox!=null)
	    		tbox.readHashMaps();
		for(com.hp.hpl.jena.graph.Triple triple : parsedQuery.getTriples() ) {
			rewrittenQuery.addTriple(new Triple(processNode(triple.getSubject(),TripleComponent.SUBJECT),processNode(triple.getPredicate(),TripleComponent.PREDICATE),processNode(triple.getObject(),TripleComponent.OBJECT)));				
		}
		for(Var var : parsedQuery.getDistinguishedVars()) {
			rewrittenQuery.addDistinguishedVar(var.getName());
		}
	}
	
	public String processNode(Node node, TripleComponent tc) {
		if(node.isVariable()) {
			return node.getName();
		}
		else {
			switch (tc) {
			case SUBJECT : 	return dictionary.getSubjectValue(node.toString())+"" ;
			case PREDICATE: 
					if(tbox==null)
						return dictionary.getPredicateValue(node.getURI())+"" ;
					else
						return tbox.getPropertiesURL2Id().get(node.getURI())+"";
			case OBJECT : 
				if(tbox==null)
					return dictionary.getObjectValue(node.toString())+"" ;
				else
					return  tbox.getConceptsURL2Id().get(node.getURI())+"";
			}
		}
		return "";
	}
	public boolean isVariable(Node node) {
		return node.isVariable();
	}
	public void displaySAPs() {
		for(Triple triple : rewrittenQuery.getTriples()) {
			System.out.println(triple.getSubject()+ "\t"+triple.getPredicate()+"\t"+triple.getObject());
//			System.out.println("\t["+ dictionary.getStatS(triple.getSubject())+","+dictionary.getStatP(triple.getPredicate())+","+dictionary.getStatO(triple.getObject())+"]");
		}
	}

	public RewrittenQuery getRewrittenQuery() {
		return rewrittenQuery;
	}
	public void inferenceCheck() {

		ArrayList<Triple> triples = rewrittenQuery.getTriples();
		Iterator<Triple> it = triples.iterator();
		while(it.hasNext()) {
			Triple triple = it.next();
			System.out.println(triple.display());
			int id;
			if((id=retrieveId(triple.getPredicate()))!=-1)
				if(tbox.getPropertiesId2URL().get(id).getNbInstance()==0) {
					int newPred = getSelf(id,tbox.getPropertiesId2URL().get(id).getSelfEncoding());
					explanation.addExplanation("Inference needed on "+ triple.getPredicate()+" self property id is  : "+newPred);
					triple.setPredicate(newPred+"");
				}
			if((id=retrieveId(triple.getObject()))!=-1)
				if(tbox.getConceptsId2URL().get(id).getNbInstance()==0) {
					int newObject = getSelf(id,tbox.getConceptsId2URL().get(id).getSelfEncoding());
					explanation.addExplanation("Inference needed on "+ triple.getObject()+" self concept is  "+newObject);
					triple.setObject(newObject+"");
				}
		}		
	}
	public int getSuperElement(int val, int bits4Encoding) {
		for(int cpt=0; cpt<bits4Encoding; cpt++)
			val = (int) val/2;
		return val;
	}
	public int getSelf(int val, int selfEncoding) {
		for(int cpt=0; cpt<selfEncoding; cpt++)
			val *= 2;
		return val;
	}
	public int retrieveId(String source) {
		try {
			return Integer.parseInt(source);
		}
		catch(NumberFormatException e) {
			return -1;
		}
	}
	public Explanation getExplanation() {
		return explanation;
	}
	
}
