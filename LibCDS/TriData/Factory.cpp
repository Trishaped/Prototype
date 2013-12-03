
#include "Factory.h"

namespace cds_static
{

/**
 * 	Create the factory for TriData
 */
Factory::Factory(uint size){
	this->size = size;
}

/**
 * Add a triplet to the factory
 */
void Factory::addTriplet(uint s, uint p, uint o){

}

/**
 * Get the core TriData
 */
TriData* Factory::get(){
	return NULL;
}


/** Builds a Wavelet Tree with an array
 *  @param array to create the waveletTree
 */
WaveletTree* TriData::makeTree(Array array){

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
BitSequence* makeBitSequence(BitString bitString){
	return new BitSequenceRG(bitString, 20/*uint sample_rate=DEFAULT_SAMPLING*/);
}

}

