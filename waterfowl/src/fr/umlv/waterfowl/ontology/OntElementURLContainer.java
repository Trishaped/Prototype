package fr.umlv.waterfowl.ontology;

import java.io.Serializable;

public class OntElementURLContainer implements Serializable {
	private String label;
	private int nbInstance;
	private int bits4Encoding;
	private int selfEncoding;
	public OntElementURLContainer(String label, int bits4Encoding) {
		this.label = label;
		this. nbInstance=0;
		this.bits4Encoding = bits4Encoding;
	}
	public int getSelfEncoding() {
		return selfEncoding;
	}
	public void setSelfEncoding(int selfEncoding) {
		this.selfEncoding = selfEncoding;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getNbInstance() {
		return nbInstance;
	}
	public void setNbInstance(int nbInstance) {
		this.nbInstance = nbInstance;
	}
	public void addNbInstance() {
		nbInstance++;
	}
	public int getBits4Encoding() {
		return bits4Encoding;
	}
	public void setBits4Encoding(int bits4Encoding) {
		this.bits4Encoding = bits4Encoding;
	}
/*	public void prettyPrint() {
		System.out.println(label + " with "+nbInstance+" instances encoding with "+bits4Encoding+" bits");
	}
	*/
	public String prettyPrint() {
		return label + " with "+nbInstance+" instances encoding with "+bits4Encoding+" bits, self encoding on "+selfEncoding+" bits ";
	}
}
