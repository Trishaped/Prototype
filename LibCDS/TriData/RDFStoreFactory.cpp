
#include "RDFStoreFactory.h"

namespace cds_static
{

/**
 * 	Create the factory for TriData
 */
RDFStoreFactory::RDFStoreFactory(uint tripletCount, uint coupleSPCount, uint tripletConceptCount, uint maxPredicat, uint maxInstance, uint maxConcepts){
	this->Bp = new BitString(coupleSPCount);
	this->Bo = new BitString(tripletCount);
	this->Bc = new BitString(tripletCount);

	this->WTp = new Array(coupleSPCount, maxPredicat);
	this->WToi = new Array(tripletCount - tripletConceptCount, maxInstance);
	this->WToc = new Array(tripletConceptCount, maxConcepts);

	this->coupleSPCount = coupleSPCount;
	this->tripletCount = tripletCount;
	this->tripletConceptCount = tripletConceptCount;
	this->maxPredicat = maxPredicat;
	this->maxInstance = maxInstance;
	this->maxConcepts = maxConcepts;

	this->BpCurrentIndex = 0;
	this->BoCurrentIndex = 0;
	this->BcCurrentIndex = 0;

	this->WTpCurrentIndex = 0;
	this->WToiCurrentIndex = 0;
	this->WTocCurrentIndex = 0;

	this->previousSubject = 0;
	this->previousPredicat = 0;
	this->previousObject = 0;
}

/**
 * Add a triplet to the factory
 */
void RDFStoreFactory::addTriplet(uint subject, uint predicat, uint object){
	bool isConcept = false;
	if(predicat==0)
		isConcept = true;

	/**
	 * Diff with previous values
	 */
	bool isDifferentSubject = (previousSubject!=subject || BpCurrentIndex==0);
	bool isDifferentPredicat = (previousPredicat!=predicat || BpCurrentIndex==0);
	bool isDifferentObject = (previousObject!=predicat || BcCurrentIndex==0);

	testParameters(subject, predicat, object, isConcept, isDifferentSubject, isDifferentPredicat, isDifferentObject);

	/**
	 * Update the previous value
	 */
	if(isDifferentSubject)
		previousSubject = subject;
	if(isDifferentPredicat)
		previousPredicat = predicat;
	if(isDifferentObject)
		previousObject = object;

	/**
	 * Only if its a different predicat, we add it to the first layer
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
void RDFStoreFactory::testParameters(uint s, uint p, uint o, bool isConcept, bool isDifferentSubject, bool isDifferentPredicat, bool isDifferentObject){

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


	//Overflow on bitmaps
	if(isDifferentPredicat && BpCurrentIndex >= coupleSPCount)
		throw new Exception(Stream() <<  "Overflow on predicates bitmap, problem with coupleSPCount? (Triplet: " << s << ", " << p << ", " << o << ")");
	if(BoCurrentIndex >= tripletCount)
		throw new Exception(Stream() <<  "Overflow on objects bitmap, problem with tripletCount? (Triplet: " << s << ", " << p << ", " << o << ")");
	if(BcCurrentIndex >= tripletCount)
		throw new Exception(Stream() <<  "Overflow on concepts bitmap, problem with tripletCount? (Triplet: " << s << ", " << p << ", " << o << ")");

	//Overflow on trees
	if(isDifferentPredicat && WTpCurrentIndex > coupleSPCount)
		throw new Exception(Stream() <<  "Overflow on predicates tree, problem with coupleSPCount? (Triplet: " << s << ", " << p << ", " << o << ")");
	if(!isConcept && WToiCurrentIndex >= tripletCount - tripletConceptCount)
		throw new Exception(Stream() <<  "Overflow on instances tree, problem with tripletCount or tripletConceptCount? (Triplet: " << s << ", " << p << ", " << o << ")");
	if(isConcept && WTocCurrentIndex >= tripletConceptCount)
		throw new Exception(Stream() <<  "Overflow on concepts tree, problem with tripletConceptCount? (Triplet: " << s << ", " << p << ", " << o << ")");

	//value superior to max value on trees
	if(p > maxPredicat)
		throw new Exception(Stream() <<  "Predicate (" << p << ") superior to maxPredicate (" << maxPredicat << ") on predicates tree (Triplet: " << s << ", " << p << ", " << o << ")");
	else if(!isConcept && o > maxInstance)
		throw new Exception(Stream() <<  "Instance (" << o << ") superior to maxInstance (" << maxInstance << ") on instances tree (Triplet: " << s << ", " << p << ", " << o << ")");
	if(isConcept && o > maxConcepts)
		throw new Exception(Stream() <<  "Concept (" << o << ") superior to maxConcept (" << maxConcepts << ") on concepts tree (Triplet: " << s << ", " << p << ", " << o << ")");


	/*
	 * TODO other tests
	 */
}

/**
 * Get the core TriData
 * return a well construct TriData
 */
RDFStore* RDFStoreFactory::get(){
	return new RDFStore(makeBitSequence(*Bp), makeBitSequence(*Bo), makeBitSequence(*Bc), makeTree(*WTp), makeTree(*WToi), makeTree(*WToc));
}


/**
 * 	Builds a Wavelet Tree with an array
 *  @param array to create the waveletTree
 */
WaveletTree* RDFStoreFactory::makeTree(Array array){

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
BitSequence* RDFStoreFactory::makeBitSequence(BitString bitString){
	return new BitSequenceRG(bitString, BitSequenceSampleRate);
}

}

