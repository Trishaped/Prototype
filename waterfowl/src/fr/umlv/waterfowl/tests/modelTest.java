package fr.umlv.waterfowl.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;

public class modelTest {
	private Model model = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		modelTest md = new modelTest();
		md.start();

	}
	public void start() {
		
		model = ModelFactory.createDefaultModel();
		model.read("file:/home/oliv/projets/waterFowl/lubm_uba/univ1/University0_1.owl", null);
		int count=0;
		NodeIterator nit = model.listObjects();
		while(nit.hasNext()) {
			count++;
		    nit.next();
		}
		System.out.println("#obj :"+count++);
		count=0;
		ResIterator rit = model.listSubjects();
		while(rit.hasNext()) {
			count++;
		    rit.next();
		}
		System.out.println("# subjs :"+count++);
		
	}

}
