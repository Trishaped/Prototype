
#include "RDFStoreFactory.h"

namespace cds_static
{

/**
 * 	Create the factory for TriData
 */
RDFStoreFactory::RDFStoreFactory(uint coupleSPCount, uint tripletCount, uint tripletConceptCount, uint maxPredicat, uint maxInstance, uint maxConcepts){
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

	testParameters(subject, predicat, object, isConcept);

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
void RDFStoreFactory::testParameters(uint s, uint p, uint o, bool isConcept){

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
	if(BpCurrentIndex > Bp->getLength())
		throw new Exception(Stream() <<  "Overflow on predicates bitmap (Triplet: " << s << ", " << p << ", " << o << ")");
	if(BoCurrentIndex > Bo->getLength())
		throw new Exception(Stream() <<  "Overflow on objects bitmap (Triplet: " << s << ", " << p << ", " << o << ")");
	if(BcCurrentIndex > Bc->getLength())
		throw new Exception(Stream() <<  "Overflow on concepts bitmap (Triplet: " << s << ", " << p << ", " << o << ")");

	//Overflow on trees
	if(WTpCurrentIndex > WTp->getLength())
		throw new Exception(Stream() <<  "Overflow on predicates tree (Triplet: " << s << ", " << p << ", " << o << ")");
	if(WToiCurrentIndex > WToi->getLength())
			throw new Exception(Stream() <<  "Overflow on instances tree (Triplet: " << s << ", " << p << ", " << o << ")");
	if(WTocCurrentIndex > WToc->getLength())
			throw new Exception(Stream() <<  "Overflow on concepts tree (Triplet: " << s << ", " << p << ", " << o << ")");

	//value superior to max value on trees
	if(p > maxPredicat)
		throw new Exception(Stream() <<  "Predicate (" << p << ") superior to maxValue (" << maxPredicat << ") on predicates tree (Triplet: " << s << ", " << p << ", " << o << ")");
	if(isConcept && o > maxConcepts)
		throw new Exception(Stream() <<  "Concept (" << o << ") superior to maxValue (" << maxConcepts << ") on concepts tree (Triplet: " << s << ", " << p << ", " << o << ")");
	else if(!isConcept && o > maxInstance)
		throw new Exception(Stream() <<  "Instance (" << o << ") superior to maxValue (" << maxInstance << ") on instances tree (Triplet: " << s << ", " << p << ", " << o << ")");

	/*
	 * TODO other tests
	 */
}

/**
 * Get the core TriData
 * return a well construct TriData
 */
TriData* RDFStoreFactory::get(){
	return new TriData(makeBitSequence(*Bp), makeBitSequence(*Bo), makeBitSequence(*Bc), makeTree(*WTp), makeTree(*WToi), makeTree(*WToc));
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

