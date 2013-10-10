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
		// quelques définitions
		String personURI    = "http://somewhere/JohnSmith";
		String givenName    = "John";
		String familyName   = "Smith";
		String fullName     = givenName + " " + familyName;

		// créer un modèle vide
		Model model = ModelFactory.createDefaultModel();

		// créer la ressource
		//   et ajouter des propriétés en cascade
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
		// liste des déclarations dans le modèle
		StmtIterator iter = model.listStatements();

		// affiche l'objet, le prédicat et le sujet de chaque déclaration
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // obtenir la prochaine déclaration
		    Resource  subject   = stmt.getSubject();     // obtenir le sujet
		    Property  predicate = stmt.getPredicate();   // obtenir le prédicat
		    RDFNode   object    = stmt.getObject();      // obtenir l'objet

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // l'objet est un littéral
		        System.out.print(" \"" + object.toString() + "\"");
		    }

		    System.out.println(" .");
		}
	}
	
}
