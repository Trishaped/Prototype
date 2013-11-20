package fr.umlv.waterfowl.tests;

import org.junit.Test;

import fr.umlv.waterfowl.dictionary.Dictionary;

public class DicoLoader {

	@Test
	public void test() {
		Dictionary dic = Dictionary.getInstance("univ1.wdk");
		dic.openFile();
		dic.load();
		System.out.println("? "+dic.getPredicateValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		System.out.println("? "+dic.getPredicateKey(new Long(6)));
		
		
		System.out.println("? "+dic.getSubjectValue("http://www.Department0.University0.edu/UndergraduateStudent476"));
		System.out.println("? "+dic.getSubjectKey(new Long(6)));
	}
}
