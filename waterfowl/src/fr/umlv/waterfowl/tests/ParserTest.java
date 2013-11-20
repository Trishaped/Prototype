package fr.umlv.waterfowl.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.queryProcessor.Optimizer;
import fr.umlv.waterfowl.queryProcessor.Parser;
import fr.umlv.waterfowl.queryProcessor.ParserFactory;
import fr.umlv.waterfowl.queryProcessor.Rewrite;

public class ParserTest {

	private Parser parser = null;
	@Test
	public void test1()  {
		Dictionary dic = Dictionary.getInstance("univ100.wdk");
		dic.openFile();
		dic.load();
		
		String[] qrs = {"SELECT ?x ?y1 ?y2 ?y3 WHERE {	?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#FullProfessor> ." +
				"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#worksFor> <http://www.Department0.University0.edu> ." +
				"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#name> ?y1 ." +
				"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#emailAddress> ?y2 ." +
				"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#telephone> ?y3.}",
				"SELECT ?x ?y ?z WHERE { " +
						"	?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#GraduateStudent> ." +
						"	?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#University> ." +
						"	?z <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Department> ." +
						"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#memberOf> ?z ." +
						"	?z <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#subOrganizationOf> ?y ." +
						"	?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#undergraduateDegreeFrom> ?y}",
				"SELECT ?x ?y WHERE { " +
								"	?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#University> ." +
								"?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#GraduateStudent> .}"};
		for(int count = 0;count<qrs.length;count++) {
			System.out.println("Query # "+(count+1));
			parser = ParserFactory.getParser(qrs[count]);
		
//			assertTrue(parser.isSelect()==true);
			parser.parse();
			parser.displayDistinguishedVar();
			parser.displayTriples();
			Rewrite rw = new Rewrite(parser.getParsedQuery(), dic);
			rw.rewrite();
			rw.displaySAPs();
			Optimizer opt = new Optimizer(rw.getRewrittenQuery(),dic);
			opt.init();
			opt.orderByType();
			opt.triplePlanner();
			opt.displayOptimizedSAPs();
			opt.displayProcessedVars();
			System.out.println(opt.getTriplePlan());
		}
	}
	@Test
	public void test2()  {
		parser = ParserFactory.getParser("PREFIX ub:</home/oliv/lubm/univ-bench.owl#>"+ //http://www.univ-mlv.fr/~ocure/lubm.owl#> " +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"SELECT ?x WHERE { ?x rdf:type  }");
		assertTrue(parser.isSelect()==false);
	}

}
