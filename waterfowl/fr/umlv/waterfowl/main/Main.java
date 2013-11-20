package fr.umlv.waterfowl.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import fr.umlv.waterfowl.dictionary.DictTriplesGenerator;
import fr.umlv.waterfowl.dictionary.Dictionary;
import fr.umlv.waterfowl.dictionary.TBoxHandler;
import fr.umlv.waterfowl.queryProcessor.Optimizer;
import fr.umlv.waterfowl.queryProcessor.Parser;
import fr.umlv.waterfowl.queryProcessor.ParserFactory;
import fr.umlv.waterfowl.queryProcessor.Plan;
import fr.umlv.waterfowl.queryProcessor.QueryProcessing;
import fr.umlv.waterfowl.queryProcessor.Rewrite;

public class Main {

	/**
	 * @param args
	 */
	
	private static Dictionary dic = null;
	private static TBoxHandler tbox = null;
	public static void main(String[] args) {
		do {
			System.out.print(">");
			Scanner sc = new Scanner(System.in);
			String cmd = sc.nextLine();
			if("\\h".equals(cmd)) {
				menu();
			}
			else
				if(cmd.startsWith("\\c")) {
					String par[] = cmd.split(" ");
					if(par.length<4 || par.length>5)
						menu();
					else 
						dicoGen(par);
				}
				else
					if(cmd.startsWith("\\l")) {
						String par[] = cmd.split(" ");
						if(par.length<2 || par.length>3)
							menu();
						else {
							if(par.length==3)
								if(tbox!=null)
									if(!par[2].equals(tbox.getTbox())) {
										tbox.clear();
										load(par[1], par[2]);
									}
							    if(dic!=null) {
							    	if(!par[1].equals(dic.getFilename())) {
							    		dic.clear();
							    	}
								}
					    	load(par[1], null);
						}
					}
					else
						if(cmd.startsWith("\\q")) {
							String par[] = cmd.split(" ");
							if(par.length!=2)
								menu();
							else 
								queryProcessing(par[1], false);
						}
						else
							if(cmd.startsWith("\\e")) {
								String par[] = cmd.split(" ");
								if(par.length!=2)
									menu();
								else {
									queryProcessing(par[1], true);
								}
							}
							else
								if("\\x".equals(cmd)) {
									System.out.println("Bye");
									break;
								}
								else
									menu();
		}
		while(true);
	}
	public static void menu() {
		System.out.println("\\c periodicity sourcePath prefixOutputs : to Create a new source ");
	    System.out.println("\\l dbName [ontology] : to Load a database and optionally an ontology");
	    System.out.println("\\q queryFileName : to Query the current db");
	    System.out.println(("\\e qyeryFileName : to query the current db with explanations"));
	    System.out.println("\\x : to eXit");
	}
	public static void dicoGen(String par[]) {
		TBoxHandler tbox = null;
		if(par.length==5) {
			tbox = new TBoxHandler(par[4]);
			tbox.process();
		}
		DictTriplesGenerator dg = new DictTriplesGenerator(Integer.parseInt(par[1]), par[2],par[3], tbox);
		dg.startProcessing();
		if(tbox!=null) {
			tbox.saveHashMaps();
			tbox.displayConcepts();
			tbox.displayProperties();
		}
	}
	public static void load(String db, String tb) {
		dic = Dictionary.getInstance(db);
		dic.openFile();
		if(tbox!=null)
			tbox = new TBoxHandler(tb);
		dic.load();		
	}
	
	public static void queryProcessing(String queryFile, boolean explanation) {
		if(dic==null)
			System.out.println("Load a db first");
		else {
			InputStream in = null;
			try {
				in = new FileInputStream(queryFile); 
			} catch ( FileNotFoundException ex) {
				System.out.println("File: "+queryFile+" not found" + ex.getMessage());
			}
			String line =null;
			ArrayList<StringBuffer>queries = new ArrayList<StringBuffer>();
			int queryCounter = -1;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			try {
				while ((line = br.readLine()) != null) {
					if(line.startsWith("select") || line.startsWith("SELECT")) {
						queries.add(new StringBuffer(line));
						queryCounter++;
					}
					else
						queries.get(queryCounter).append("\n"+line.trim());
				}
			} catch(IOException ex) {
				System.out.println("Erreur à la lecture de "+queryFile);
			}
			for(StringBuffer sb : queries) {
				QueryProcessing qrp = QueryProcessing.getInstance(dic, tbox);
				qrp.process(sb.toString());
				if(explanation) {
					System.out.println("Explanations :");
					qrp.getExplanation().display();
				}
/*				System.out.println(sb);
				Parser parser = ParserFactory.getParser(sb.toString());
				parser.parse();
				Rewrite rw = new Rewrite(parser.getParsedQuery(), dic);
				rw.rewrite();
				rw.displaySAPs();
				Optimizer opt = new Optimizer(rw.getRewrittenQuery(),dic);
				opt.init();
				opt.orderByType();
				opt.triplePlanner();
				Plan plan = new Plan(opt.getRewrittenQuery(), opt.getTriplePlan());
				plan.prettyPrint();
*/				}			
		}
	}
}
