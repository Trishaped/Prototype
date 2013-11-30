package fr.umlv.waterfowl.tests;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.umlv.waterfowl.dictionary.TBoxHandler;

public class TBoxHandlerTests {
	private static TBoxHandler tbox;
	
	@BeforeClass
	public static void init() {
		   tbox =   new TBoxHandler("univ-bench.owl");
   	      tbox.process();
//		   tbox.saveHashMaps();
	}
	
	 @Test
	 public void volumetry() {
		assertEquals(tbox.getConceptsId2URL().size(), tbox.getConceptsURL2Id().size());
		assertEquals(tbox.getPropertiesId2URL().size(), tbox.getPropertiesId2URL().size());
		assertEquals(43,tbox.getConceptsId2URL().size());
		assertEquals(33,tbox.getPropertiesURL2Id().size());
	 }

	@Test
	public void readHashMaps() {
		tbox.readHashMaps();
		assertEquals(tbox.getConceptsId2URL().size(), tbox.getConceptsURL2Id().size());
		assertEquals(tbox.getPropertiesId2URL().size(), tbox.getPropertiesId2URL().size());
		assertEquals(43,tbox.getConceptsId2URL().size());
		assertEquals(33,tbox.getPropertiesURL2Id().size());
 	      tbox.displayConcepts();
 	      tbox.displayProperties();
	}
}
