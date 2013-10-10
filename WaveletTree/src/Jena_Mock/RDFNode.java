package Jena_Mock;


/**
 * ONLY FOR TEST! Simule JENA! 
 * Classe hérité par tout les composants d'un triplet
 */
public class RDFNode {

	String name;
	
	/*
	 * Tout va se jouer ici et dans ses sous classe
	 * Savoir quelles noeuds sont egaux? (selon leur type, ...)
	 * Pour l'instant on test juste leur nom et tout peut être equals (sujet, objet, predicat)
	 * du moment qu'ils on le meme nom
	 */
	@Override
	public boolean equals(Object obj){
		return name.equals(((RDFNode)obj).name);
	}
}
