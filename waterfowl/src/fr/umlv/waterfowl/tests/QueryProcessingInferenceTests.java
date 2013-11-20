package fr.umlv.waterfowl.tests;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.dictionary.TBoxHandler;
import fr.umlv.waterfowl.queryProcessor.Explanation;
import fr.umlv.waterfowl.queryProcessor.Parser;
import fr.umlv.waterfowl.queryProcessor.ParserFactory;
import fr.umlv.waterfowl.queryProcessor.Rewrite;

public class QueryProcessingInferenceTests {

	private Parser parser = null;
	private static TBoxHandler tbox;
	private static Dictionary dic;
	private Explanation explanation = new Explanation();
	@BeforeClass
	public static void init() {
		   tbox =   new TBoxHandler("univ-bench.owl");
		    dic = Dictionary.getInstance("univ1");
			dic.openFile();
			dic.load();
	}
	
	@Test
	public void test() {
		String[] qrs = {	"SELECT ?x ?y WHERE { " +
								"	?y <http://swat.cse.lehigh.edu/onto/univ-bench.owl#officeNumber> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#University> ." +
								"?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Professor> .}"};
		for(int count = 0;count<qrs.length;count++) {
			System.out.println("Query # "+(count+1));
			parser = ParserFactory.getParser(qrs[count]);

			parser.parse();
			parser.displayDistinguishedVar();
			parser.displayTriples();
			Rewrite rw = new Rewrite(parser.getParsedQuery(),tbox, explanation);
			rw.rewrite();
			rw.inferenceCheck();
			System.out.println("SAP !");
			rw.displaySAPs();
			System.out.println("Exp :");
			rw.getExplanation().display();
		}
	}
}