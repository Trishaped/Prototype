
#include "TriData.h"

namespace cds_static
{

/** Builds a Wavelet Tree with an array
 *  @param array to create the waveletTree
 * */
WaveletTree TriData::makeTree(Array array) const{

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

	return WaveletTree(array, coder, bitSequenceBuilder, new MapperNone());
}

}
