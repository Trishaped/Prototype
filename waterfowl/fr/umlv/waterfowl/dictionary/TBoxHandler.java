package fr.umlv.waterfowl.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import fr.umlv.waterfowl.ontology.OntElementURLContainer;
import fr.umlv.waterfowl.utils.BitSetConverter;

public class TBoxHandler {
	private Map<Integer,OntElementURLContainer> conceptsId2URL = new HashMap<Integer,OntElementURLContainer>();
	private Map<String, Integer> conceptsURL2Id = new HashMap<String,Integer>();
	private Map<Integer,OntElementURLContainer> propertiesId2URL = new HashMap<Integer,OntElementURLContainer>();
	private Map<String, Integer> propertiesURL2Id  = new HashMap<String, Integer>();
	
	private String tbox;
	BitSetConverter bitSetConverter = new BitSetConverter();
	private OntModel ontModel;
	private String nothing = "http://www.w3.org/2002/07/owl#Nothing";
	private String thing = "http://www.w3.org/2002/07/owl#Thing";
	
	public TBoxHandler(String tbox) {
		this.tbox = tbox;
	}
	public void loadModel() {
		ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC );
		InputStream in = FileManager.get().open(tbox); 
		if (in == null) {
		    throw new IllegalArgumentException("File: "+tbox+" not found");
		}
		ontModel.read(in, null);
	}
	public void processConcepts(BitSet prefix, String concept) {
		OntClass cls = ontModel.createClass(concept);
		int count = countConcreteSubClasses(cls);
		if(count>0) {
			int nbBits = (int) Math.ceil( Math.log(++count)/Math.log(2) );
			if(!thing.equals(concept)) {
//				System.out.println(concept+" has self :"+nbBits+ " for "+ prefix+ " = " +bitSetConverter.convert(prefix) );
				conceptsId2URL.get(bitSetConverter.convert(prefix)).setSelfEncoding(nbBits);
			}
				

			ExtendedIterator<OntClass> it = cls.listSubClasses(true);
			int id = 1;
			while(it.hasNext()) {
				OntClass tmpCls = it.next();
				if(!tmpCls.isAnon() && !tmpCls.getLocalName().equals("Nothing")) {
					BitSet bs = bitSetConverter.convert(id);
					bs = bitSetConverter.concat(prefix,bs,nbBits);
					if(prefix.cardinality()==0)
						bs.set(nbBits);
	
 			System.out.println(tmpCls.toString()+"==> "+bs.toString()+"/ "+id+ " bs = "+bitSetConverter.convert(bs));
						conceptsId2URL.put(bitSetConverter.convert(bs),new OntElementURLContainer(tmpCls.toString(),nbBits));
						conceptsURL2Id.put(tmpCls.toString(), bitSetConverter.convert(bs));
						processConcepts(bs, tmpCls.toString());
						id++;
				}
			}
		}
	}	
	public void processProperties(int prefixSize, BitSet prefix, OntProperty property) {
		ExtendedIterator<? extends OntProperty> it = property.listSubProperties(true);
    	int count = countPropertySubClasses(property);
     	if(count>0) {
    		int nbBits = (int) Math.ceil( Math.log(++count)/Math.log(2) );
    		
    		if(property!= ontModel.getOntProperty("http://www.w3.org/2002/07/owl#topDataProperty") && property != ontModel.getOntProperty("http://www.w3.org/2002/07/owl#topObjectProperty")) {
				propertiesId2URL.get(bitSetConverter.convert(prefix)).setSelfEncoding(nbBits);
			}
    		
    		int id=0;
    		while(it.hasNext()) {

    			OntProperty p = it.next();
    			if(candidateProperty(p.getURI()))	{
    				if((property==null && !hasSuperProperty(p)) || (property != null)) {
    					BitSet bs = bitSetConverter.convert(id);
    					bs = bitSetConverter.concat(prefix,bs,nbBits);
   	  		 		    if(prefix.cardinality()==0)
    						bs.set(nbBits);
 					System.out.println(p+"==> "+bs.toString()+ " / id: "+id+ " p:"+prefix+" nbbits ="+nbBits+ " bs = "+bitSetConverter.convert(bs));
    					propertiesId2URL.put(bitSetConverter.convert(bs),new OntElementURLContainer(p.toString(),nbBits));
						propertiesURL2Id.put(p.toString(), bitSetConverter.convert(bs));
    					processProperties(prefixSize+nbBits, bs, p);	
    					id++;
    				}
    			}
		}
		}	
	}
	public void initProperty() {
		propertiesURL2Id.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type",0);
		propertiesId2URL.put(0,new OntElementURLContainer("http://www.w3.org/1999/02/22-rdf-syntax-ns#type",1));
	}
	public boolean candidateProperty(String uri) {
		if("http://www.w3".equals(uri.subSequence(0,13)))
			return false;
		else
			return true;
	}
	public String getTbox() {
		return tbox;
	}
	public void setTbox(String tbox) {
		this.tbox = tbox;
	}
	public void clear() {
		ontModel = null;
		tbox = null;
		conceptsURL2Id.clear();
		conceptsId2URL.clear();
		propertiesURL2Id.clear();
		propertiesId2URL.clear();
	}
	public boolean hasSuperProperty(OntProperty p) {
		ExtendedIterator<? extends OntProperty> it= p.listSuperProperties();
		int occur = 0;
		while (it.hasNext()) {
			occur++;
			break;
		}
		if(occur==0)
			return false;
		else 
			return true;
	}
    public int countPropertySubClasses(OntProperty property) {
    	int count=0;
    	ExtendedIterator<? extends OntProperty> it = property.listSubProperties(true);
    	while(it.hasNext()) {
    		OntProperty p = it.next();
    		if(candidateProperty(p.getURI())) 
    			if((property==null && !hasSuperProperty(p)) || (property != null)) {
    				count++;
    			}
    	}
    	return count;
    }
 	public int countConcreteSubClasses(OntClass cls) {
		int count=0;
		ExtendedIterator<OntClass> it = cls.listSubClasses(true);
		while(it.hasNext()) {
			OntClass tmpCls = it.next();
			if(!tmpCls.isAnon() && !tmpCls.getLocalName().equals("Nothing"))
				count++;
		}
		return count;
	}

	public void displayConcepts() {
		System.out.println("------------------------------"+conceptsURL2Id.size()+ " "+ conceptsId2URL.size());
		Set<Integer> idSet = conceptsId2URL.keySet();
		for(Integer id : idSet) {
			System.out.println(id+", "+ bitSetConverter.convert(id)+"  => "+ conceptsId2URL.get(id).prettyPrint() );
		}
	}
	public void displayProperties() {
		System.out.println("------------------------------"+propertiesURL2Id.size()+" " +propertiesId2URL.size());
		Set<Integer> idSet = propertiesId2URL.keySet();
		for(Integer id : idSet) {
			System.out.println(id+", "+ bitSetConverter.convert(id)+" ::> "+ propertiesId2URL.get(id).prettyPrint() );
		}
	}
	public void saveHashMaps() {
		String tboxPrefix = simplifyTboxName();
		save(tboxPrefix,"conceptsId2URL");
		save(tboxPrefix,"conceptsURL2Id");
		save(tboxPrefix,"propertiesId2URL");
		save(tboxPrefix,"propertiesURL2Id");
	}
	
	public void save(String tboxPrefix,  String target) {
		File file = new File(tboxPrefix+"_"+target);  
		try {
			FileOutputStream f = new FileOutputStream(file); 
			ObjectOutputStream s = new ObjectOutputStream(f);
			if("conceptsId2URL".equals(target))
					s.writeObject(conceptsId2URL);
			else
				if("conceptsURL2Id".equals(target))
					s.writeObject(conceptsURL2Id);
				else
					if("propertiesId2URL".equals(target))
						s.writeObject(propertiesId2URL);
					else
						if("propertiesURL2Id".equals(target))
							s.writeObject(propertiesURL2Id);
			 s.close();
		 }
		 catch(IOException ioe) {
				System.err.println("Probleme ouverture de "+target);
				return;
			}
	}
	public void readHashMaps() {
		String tboxPrefix = simplifyTboxName();
		read(tboxPrefix,"conceptsId2URL");
		read(tboxPrefix,"conceptsURL2Id");
		read(tboxPrefix,"propertiesId2URL");
		read(tboxPrefix,"propertiesURL2Id");
	}
	public void read(String tboxPrefix,  String target) {
		try {
			File file = new File(tboxPrefix+"_"+target);
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			if("conceptsId2URL".equals(target))
				conceptsId2URL = (HashMap<Integer,OntElementURLContainer>) s.readObject();
		    else
			  if("conceptsURL2Id".equals(target))
				 conceptsURL2Id = (HashMap<String, Integer>) s.readObject();
			  else
				if("propertiesId2URL".equals(target))
					propertiesId2URL = (HashMap<Integer,OntElementURLContainer>) s.readObject();
				else
					if("propertiesURL2Id".equals(target))
						propertiesURL2Id = (HashMap<String, Integer>) s.readObject();
			s.close();
		} catch(IOException ioe) {
					System.err.println("Probleme ouverture de "+target);
					return;
		}catch(ClassNotFoundException cnf) {
			System.err.println("Probleme " + cnf);
			return;
		}
	}
	
	public void process() {
		loadModel();
	    processConcepts(new BitSet(), thing);
		BitSet bs = new BitSet();
		// init with rdf:type to 0
		initProperty();
		// processing datatypeprop starting with bit pattern '1..'
		bs.set(0);
//		bs.set(2);
		processProperties(2,bs, ontModel.getOntProperty("http://www.w3.org/2002/07/owl#topDataProperty"));
		//processing objectprop starting with bit pattern '01..'
		bs.set(1);
		bs.clear(0);
		processProperties(2,bs, ontModel.getOntProperty("http://www.w3.org/2002/07/owl#topObjectProperty"));
	}
	public String simplifyTboxName() {
		if(tbox.contains(".owl")) {
			return tbox.replace(".owl","");
		}
		else
			return tbox;		
	}
	public Map<Integer, OntElementURLContainer> getConceptsId2URL() {
		return conceptsId2URL;
	}
	public Map<String, Integer> getConceptsURL2Id() {
		return conceptsURL2Id;
	}
	public Map<Integer, OntElementURLContainer> getPropertiesId2URL() {
		return propertiesId2URL;
	}
	public Map<String, Integer> getPropertiesURL2Id() {
		return propertiesURL2Id;
	}
}