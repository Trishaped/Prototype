package fr.umlv.waterfowl.queryProcessor;

import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.dictionary.TBoxHandler;

public class QueryProcessing {
	private Explanation explanation = new Explanation();
	private static QueryProcessing instance = null;
	private Dictionary dic = null;
	private TBoxHandler tbox = null;
	
	private QueryProcessing(Dictionary dic, TBoxHandler tbox) {
		this.dic = dic;
		this.tbox = tbox;
	}
	
	public static QueryProcessing getInstance( Dictionary dic, TBoxHandler tbox) {
		if(instance==null)
			instance = new QueryProcessing(dic, tbox);
		return instance;
	}
   public void process(String query) {
	   Parser parser = ParserFactory.getParser(query.toString());
		parser.parse();
		Rewrite rw = new Rewrite(parser.getParsedQuery(), dic, explanation);
		rw.rewrite();
		rw.inferenceCheck();
		rw.displaySAPs();
		Optimizer opt = new Optimizer(rw.getRewrittenQuery(),dic);
		opt.init();
		opt.orderByType();
		opt.triplePlanner();
		Plan plan = new Plan(opt.getRewrittenQuery(), opt.getTriplePlan());
		plan.prettyPrint();
   }
	public Explanation getExplanation() {
		return explanation;
	}
	public void setExplanation(Explanation explanation) {
		this.explanation = explanation;
	}
   public void clearExplanation() {
	   explanation.clear();
   }
   public static QueryProcessing getInstance() {
		return instance;
   }
}
