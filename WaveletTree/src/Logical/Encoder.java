package Logical;
import java.util.Dictionary;
import java.util.List;

import Data.Bitmap;
import Data.Triplet;


/**
 * Encoder/Decoder
 */
public class Encoder {
	/*
	 * Les dictionnaires d'encodage
	 */
	private Dictionary<String, Bitmap> concepts;
	private Dictionary<String, Bitmap> predicats;
	private Dictionary<String, Bitmap> instances;

	/*
	 * Les dictionnaires de décodage
	 */
	private Dictionary<Bitmap, String> conceptsInv;
	private Dictionary<Bitmap, String> predicatsInv;
	private Dictionary<Bitmap, String> instancesInv;

	/*
	 * Encode depuis une liste de triplets triés
	 */
	public Encoder(List<Triplet> nodes) {

	}

}
