import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;


public class HelloJena {

	public static void main(String args[]){
		HelloJena jena = new HelloJena();
		Model model = jena.createModel();		
		jena.printModel(model);
	}
	
	public Model createModel(){
		// quelques d�finitions
		String personURI    = "http://somewhere/JohnSmith";
		String givenName    = "John";
		String familyName   = "Smith";
		String fullName     = givenName + " " + familyName;

		// cr�er un mod�le vide
		Model model = ModelFactory.createDefaultModel();

		// cr�er la ressource
		//   et ajouter des propri�t�s en cascade
		Resource johnSmith
		  = model.createResource(personURI)
		         .addProperty(VCARD.FN, fullName)
		         .addProperty(VCARD.N,
		                      model.createResource()
		                           .addProperty(VCARD.Given, givenName)
		                           .addProperty(VCARD.Family, familyName));
		
		return model;		
	}
	
	
	public void printModel(Model model){
		// liste des d�clarations dans le mod�le
		StmtIterator iter = model.listStatements();

		// affiche l'objet, le pr�dicat et le sujet de chaque d�claration
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // obtenir la prochaine d�claration
		    Resource  subject   = stmt.getSubject();     // obtenir le sujet
		    Property  predicate = stmt.getPredicate();   // obtenir le pr�dicat
		    RDFNode   object    = stmt.getObject();      // obtenir l'objet

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // l'objet est un litt�ral
		        System.out.print(" \"" + object.toString() + "\"");
		    }

		    System.out.println(" .");
		}
	}
	
}
