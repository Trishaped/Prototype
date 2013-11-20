package fr.umlv.waterfowl.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.umlv.waterfowl.queryProcessor.ParserJena;

public class ParserJenaTest {

	@Test
	public void test() {
			ParserJena parser = new ParserJena("PREFIX ub:</home/oliv/lubm/univ-bench.owl#>"+ //http://www.univ-mlv.fr/~ocure/lubm.owl#> " +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"SELECT ?x WHERE { ?x rdf:type ub:UndergraduateStudent }");
			parser.parse();
	}
}
