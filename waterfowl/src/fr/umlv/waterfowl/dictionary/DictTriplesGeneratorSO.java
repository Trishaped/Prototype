package fr.umlv.waterfowl.dictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

import fr.umlv.waterfowl.sharedComponent.Triple;

public class DictTriplesGeneratorSO {

	/**
	 * @param args
	 */
	private Map<String,ContainerIdCount> mapS = new HashMap<String,ContainerIdCount>();
	private Map<String,ContainerIdCount> mapO = new HashMap<String,ContainerIdCount>();
	private Map<String,ContainerIdCount> mapP = new HashMap<String,ContainerIdCount>();
	private Map<String,ContainerIdCount> mapSO = new HashMap<String,ContainerIdCount>();
	private Model model = ModelFactory.createDefaultModel();
	private File folderToProcess;
	private Set<Triple> tripleSet = new HashSet<Triple>();
	private int maxNumberFilesBeforeStorage;
	private long cptS=0,cptO=0, cptP=0;
	private String outputFileName = null;
	private long count=0;
	private boolean onto;
	
	public DictTriplesGeneratorSO(int filesToProcessBeforeStorage, String folderToProcess, String outputFileName, boolean onto) {
		this.maxNumberFilesBeforeStorage = filesToProcessBeforeStorage;
		this.folderToProcess = new File(folderToProcess);
		this.outputFileName = outputFileName;
		this.onto = onto;
	}
	public void startProcessing() {
		long start = System.currentTimeMillis();
		processFiles();
		System.out.println("Triples encoding duration = "+ (System.currentTimeMillis()-start) +"ms");
		start = System.currentTimeMillis();
		recordDicoInFile();
		System.out.println("Dico storing duration = "+ (System.currentTimeMillis()-start) +"ms");
		clearDictionaries();
		System.out.println("Done ");
	}

	public void processFiles() {
		File[] files = folderToProcess.listFiles();
		Arrays.sort(files);
		for(File fileEntry : files) {
			if(fileEntry.isFile()) {
				count++;
				InputStream in = FileManager.get().open(fileEntry.toString()); 
				if (in == null) {
				    throw new IllegalArgumentException("File: "+fileEntry.toString()+" not found");
				}
				System.out.println("processing file #"+count + " :: "+fileEntry.getName());			
				processModel(in);
			}
			if(count%maxNumberFilesBeforeStorage==0) {
				System.out.println("Recording HashSet #"+(count/maxNumberFilesBeforeStorage));
				recordTriplesInFile(count/maxNumberFilesBeforeStorage);
				tripleSet.clear();
			}
		}
		recordTriplesInFile(1+(count/maxNumberFilesBeforeStorage));
		tripleSet.clear();
	}	
	
	public void processModel(InputStream in) {
		model.read(in, null);
		StmtIterator sit = model.listStatements();
		
		while(sit.hasNext()) {
			Statement tmp = sit.next();
			storeSOPMaps(tmp);
		}
		model.removeAll();
	}
	public void storeSOPMaps(Statement tmp) {
		String idS="0", idP="0", idO="0";
		// processing a  subject
		if(mapS.containsKey(tmp.getSubject().getURI())) {
				mapS.put(tmp.getSubject().getURI(), mapS.get(tmp.getSubject().getURI()).incCount());
				idS = mapS.get(tmp.getSubject().getURI()).getId()+"";
		}
		else {
				idS = cptS+"";
				mapS.put(tmp.getSubject().getURI(), new ContainerIdCount(cptS++));
		}
		// processing an object
		if(mapO.containsKey(tmp.getObject().toString())) {
				mapO.put(tmp.getObject().toString(), mapO.get(tmp.getObject().toString()).incCount());
				idO = mapO.get(tmp.getObject().toString()).getId()+"";
		}
		else {
				idO = cptO+"";
				mapO.put(tmp.getObject().toString(), new ContainerIdCount(cptO++));
		}
		// processing a predicate
		if(!onto) {
			if(mapP.containsKey(tmp.getPredicate().getURI())) {
				mapP.put(tmp.getPredicate().getURI(), mapP.get(tmp.getPredicate().getURI()).incCount());
				idP = mapP.get(tmp.getPredicate().getURI()).getId()+"";
			}
			else {
				idP = cptP+"";
				mapP.put(tmp.getPredicate().getURI(), new ContainerIdCount(cptP++));
			}
		}
		tripleSet.add(new Triple(idS,idP,idO));
	}
		
	public void recordTriplesInFile(long index) {
		System.out.println("Recording "+outputFileName+"_"+index+"_tmp.wtr" );
		FileOutputStream output;
		try{
			output = new FileOutputStream(outputFileName+"_"+index+"_tmp.wtr");
		}
		catch(IOException ioe) {
			System.err.println("Probleme ouverture de test1.txt");
			return;
		}
		PrintStream psOutput = new PrintStream(output);
		for(Triple triple : tripleSet) {
			psOutput.println(triple.display());
		}
	}
	public void recordDicoInFile() {
		FileOutputStream outputD;
		try{
			outputD = new FileOutputStream(outputFileName+".wdk");
		}
		catch(IOException ioe) {
			System.err.println("Probleme ouverture de test1.txt");
			return;
		}
		PrintStream psOutputD = new PrintStream(outputD);
		displayMap(psOutputD,mapS);
		psOutputD.println("-- predicate");
		displayMap(psOutputD,mapO);
		psOutputD.println("-- object");
		displayMap(psOutputD,mapP);
	}
	public void clearDictionaries() {
		mapS.clear();
		mapO.clear();
		mapP.clear();
	}
	
	public void displayMap(PrintStream psOutputD, Map<String,ContainerIdCount> map) {
		Set<String> set = map.keySet();
		for(String s : set) {
			psOutputD.println(s+"\t"+map.get(s).prettyPrint());
		}	
	}
}