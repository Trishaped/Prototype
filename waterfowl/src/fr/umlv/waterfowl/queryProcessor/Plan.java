package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Plan {
	private RewrittenQuery rewrittenQuery;
	private ArrayList<Integer> triplePlan;
	private HashMap<String,String> processedVars = new HashMap<String, String>();
	
	public Plan(RewrittenQuery rewrittenQuery, ArrayList<Integer> triplePlan) {
		this.rewrittenQuery = rewrittenQuery;
		this.triplePlan = triplePlan;
	}
	public void prettyPrint() {
		rewrittenQuery.displayDistinguishedVars();
		for(Integer i : triplePlan) {
			System.out.println(processEntry(rewrittenQuery.getTriple(i).getSubject())+" "+processEntry(rewrittenQuery.getTriple(i).getPredicate())+" "+processEntry(rewrittenQuery.getTriple(i).getObject()));
		}
	}
	public String processEntry(String entry) {
		try {
			Long val = Long.parseLong(entry);
			return entry;
		}catch (NumberFormatException e) {
			if(processedVars.containsKey(entry))
				return "!"+entry;
			else {
				processedVars.put(entry, null);
				return "?"+entry;
			}
		}
	}	
}


