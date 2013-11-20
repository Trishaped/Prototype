package fr.umlv.waterfowl.queryProcessor;

import java.util.ArrayList;
import java.util.Iterator;

public class Explanation {
	private ArrayList<String> explanations = new ArrayList<String>();

	public ArrayList<String> getExplanations() {
		return explanations;
	}

	public void setExplanations(ArrayList<String> explanations) {
		this.explanations = explanations;
	}
	public void clear() {
		explanations.clear();
	}
	public void addExplanation(String explanation) {
		explanations.add(explanation);
	}
	public void display() {
		for(String exp : explanations)
			System.out.println(" - "+exp);
	}
}