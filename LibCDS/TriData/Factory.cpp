
#include "Factory.h"

namespace cds_static
{

/**
 * 	Create the factory for TriData
 */
Factory::Factory(uint size, uint maxValue){
	Bp = new BitString(size);
	Bo = new BitString(size);
	Bc = new BitString(size);

	WTp = new Array(size, maxValue);
	WToi = new Array(size, maxValue);
	WToc = new Array(size, maxValue);


	BpCurrentIndex = 0;
	BoCurrentIndex = 0;
	BcCurrentIndex = 0;

	WTpCurrentIndex = 0;
	WToiCurrentIndex = 0;
	WTocCurrentIndex = 0;


	previousSubject = 0;
	previousPredicat = 0;
}

/**
 * Add a triplet to the factory
 */
void Factory::addTriplet(uint s, uint p, uint o, bool isConcept){

	/* Subjects */
	bool isDifferentSubject = (previousSubject!=s || BpCurrentIndex==0);
	if(isDifferentSubject)
		previousSubject = s;
	Bp->setBit(BpCurrentIndex++, isDifferentSubject);


	/* Predicats */
	WTp->setField(WTpCurrentIndex++, p);
	bool isDifferentPredicat = (previousPredicat!=s || BoCurrentIndex==0);
	if(isDifferentSubject)
		previousPredicat = s;
	Bo->setBit(BoCurrentIndex++, isDifferentPredicat);


	/* Objects */
	Bc->setBit(BcCurrentIndex++, isConcept);
	if(isConcept)
		WToc->setField(WTocCurrentIndex, o);
	else
		WToi->setField(WToiCurrentIndex, o);

}

/**
 * Get the core TriData
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

