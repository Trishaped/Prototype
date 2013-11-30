import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Scanner;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class Convert2NT {
	private File folderName ;

	public void getFoldername() {
		Scanner sc = new Scanner(System.in);
		String folder = sc.nextLine();
		folderName = new File(folder);
	}
	public void scanFolder() {
		int count=0;
		File[] files = folderName.listFiles();
		Arrays.sort(files);
		for(File fileEntry : files) {
			if(fileEntry.isFile()) {
				count++;
				InputStream in = FileManager.get().open(fileEntry.toString()); 
				if (in == null) {
				    throw new IllegalArgumentException("File: "+fileEntry.toString()+" not found");
				}
				String outputFile = fileEntry.getName().substring(0,  fileEntry.getName().indexOf("."));
				System.out.println("processing file #"+count + " :: "+fileEntry.getName()+ " to "+outputFile );			
				process(in, outputFile);
			}
		}
	}
	public void process(InputStream in, String outputFile) {
		OntModel m = ModelFactory.createOntologyModel();
		m.read(in, null);
		FileOutputStream outputD;
		try{
			outputD = new FileOutputStream(outputFile+".nt");
		}
		catch(IOException ioe) {
			System.err.println("Probleme ouverture de "+outputFile);
			return;
		}
		PrintStream psOutputD = new PrintStream(outputD);
		m.write(psOutputD,"N-TRIPLE");	
	}
	public static void main(String args[]) {
		Convert2NT cnt = new Convert2NT();
		cnt.getFoldername();
		cnt.scanFolder();
	}
}