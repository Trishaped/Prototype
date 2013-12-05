
#include "Factory.h"

namespace cds_static
{

/**
 * 	Create the factory for TriData
 */
Factory::Factory(uint firstLayerSize, uint secondLayerSize, uint maxValue){
	Bp = new BitString(firstLayerSize);
	Bo = new BitString(secondLayerSize);
	Bc = new BitString(secondLayerSize);

	WTp = new Array(firstLayerSize, maxValue);
	WToi = new Array(secondLayerSize, maxValue);
	WToc = new Array(secondLayerSize, maxValue);

	BpCurrentIndex = 0;
	BoCurrentIndex = 0;
	BcCurrentIndex = 0;

	WTpCurrentIndex = 0;
	WToiCurrentIndex = 0;
	WTocCurrentIndex = 0;

	previousSubject = 0;
	previousPredicat = 0;
	previousObject = 0;
}

/**
 * Add a triplet to the factory
 */
void Factory::addTriplet(uint subject, uint predicat, uint object, bool isConcept){
	testParameters(subject, predicat, object);

	/**
	 * Diff with previous values
	 */
	bool isDifferentSubject = (previousSubject!=subject || BpCurrentIndex==0);
	if(isDifferentSubject)
		previousSubject = subject;

	bool isDifferentPredicat = (previousPredicat!=predicat || BoCurrentIndex==0);
	if(isDifferentSubject)
		previousPredicat = predicat;

	bool isDifferentObject = (previousObject!=predicat || BcCurrentIndex==0);
	if(isDifferentObject)
		previousObject = object;


	/**
	 * Only if its a different predicat, we modify the first layer
	 */
	if(isDifferentPredicat){
		// Subjects Bitmap
		Bp->setBit(BpCurrentIndex++, isDifferentSubject);

		// Predicats Tree
		WTp->setField(WTpCurrentIndex++, predicat);
	}

	/**
	 * For each triplet an object is added
	 */
	// Objects Bitmap
	Bo->setBit(BoCurrentIndex++, isDifferentPredicat);
	// Concept Bitmap
	Bc->setBit(BcCurrentIndex++, isConcept);

	// Concept or Instance Tree
	if(isConcept)
		WToc->setField(WTocCurrentIndex++, object);
	else
		WToi->setField(WToiCurrentIndex++, object);

}

/**
 * Renvoi true si tout les paramètres sont bon
 */
void Factory::testParameters(uint s, uint p, uint o){

	// Good sort tests
	if(s<previousSubject)
		throw new Exception(Stream() << "Sort error on subject (Triplet: " << s << ", " << p << ", " << o << ")");

	if(s<=previousSubject && p<previousPredicat)
		throw new Exception(Stream() << "Sort error on subject or predicat (Triplet: " << s << ", " << p << ", " << o << ")");

	if(s<=previousSubject && p<=previousPredicat && o<=previousObject && BpCurrentIndex>0)
		throw new Exception(Stream() << "Sort error on subject or predicat or object (Triplet: " << s << ", " << p << ", " << o << ")");


	// Doublon
	if(s==previousSubject && p==previousPredicat && o==previousObject && BpCurrentIndex>0)
		throw new Exception(Stream() << "Doublon (Triplet: " << s << ", " << p << ", " << o << ")");

	// Hole in subjects list
	if(s>previousSubject+1)
		throw new Exception(Stream() << "Hole (Triplet: " << s << ", " << p << ", " << o << ")");


	/*
	 * TODO other tests
	 */
}

/**
 * Get the core TriData
 * return a well construct TriData
 */
TriData* Factory::get(){
	return new TriData(makeBitSequence(*Bp), makeBitSequence(*Bo), makeBitSequence(*Bc), makeTree(*WTp), makeTree(*WToi), makeTree(*WToc));
}


/** Builds a Wavelet Tree with an array
 *  @param array to create the waveletTree
 */
WaveletTree* Factory::makeTree(Array array){

	/* BitSequence builder for BitSequenceRG */
	BitSequenceBuilder *bitSequenceBuilder = new BitSequenceBuilderRG(RGFactor);

	/* Mapper that makes the values in the set contiguous */
	Mapper *mapper = new MapperCont(array, *bitSequenceBuilder);

	/* Considers the binary representation of the symbols as the code */
	wt_coder *coder = new wt_coder_binary(array, mapper);

	/* BitSequence builder for RRR BitSequences */
	bitSequenceBuilder = new BitSequenceBuilderRRR(RRRsample_Rate);


	/* 		Wavelet tree implementation using pointers.
	 * 		array : each values to set in the waveletTree
	 * 		coder : to compress (huffman)
	 * 		bitSequence : builder for a sequence
	 * 		mapper : mapper for the alphabet
	 */
	return new WaveletTree(array, coder, bitSequenceBuilder, new MapperNone());
}

/**
 *	Make a bitSequence with a bitString
 */
BitSequence* Factory::makeBitSequence(BitString bitString){
	return new BitSequenceRG(bitString, BitSequenceSampleRate);
}

}

