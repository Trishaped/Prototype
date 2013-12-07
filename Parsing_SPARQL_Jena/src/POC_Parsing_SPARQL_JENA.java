import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;

public class POC_Parsing_SPARQL_JENA {

	public static void main(String args[]){

		String sparqlQueryString= "SELECT DISTINCT ?y ?x"+
				"{"+
				"?x <http://www.w3.org/2001/vcard-rdf/3.0#FN> 'John Smith'." +
				"?y <http://www.w3.org/2001/vcard-rdf/3.0#Given> 'John'" +
				"}";

		Query query = null;
		
		try {
			query = QueryFactory.create(sparqlQueryString);
		} catch (QueryException e) {
			// TODO: send the error to the client application explaining it's not a regular SPARQL query 
		}

		System.out.println("Request:");
		System.out.println(query);

		System.out.println("Ordered List of distinguished variables :");
		System.out.println(query.getResultVars()+"\n"); //list of distinguished variables

		System.out.print("Is the query using DISTINCT ?: "); //can be used for the others modifiers
		System.out.println(query.isDistinct()+"\n");

		// Retrieve all raw instructions in this list
		final ArrayList<Triple> rawInstructions = new ArrayList<Triple>();

		//Visitor used to be executed during the walk
		ElementVisitorBase visitor = new ElementVisitorBase(){
			public void visit(ElementPathBlock el) {
				// ...go through all the triples...
				Iterator<TriplePath> triples = el.patternElts();
				while (triples.hasNext()) {
					// ...and grab the triple
					rawInstructions.add(triples.next().asTriple());
				}
			}
		};

		// This will walk through all parts of the query
		ElementWalker.walk(query.getQueryPattern(),visitor);

		for (Triple triple : rawInstructions) {
			System.out.println("Subject: "+triple.getSubject()+" , Predicat: "+triple.getPredicate()+" , Object: "+triple.getObject());
		}

	}

}