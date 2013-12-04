#ifndef STATIC_DATA_H
#define STATIC_DATA_H

#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
#include <BitSequenceBuilderRG.h>
#include <wt_node_internal.h>
#include <wt_coder_binary.h>
#include <Mapper.h>
#include <Sequence.h>

using namespace std;

namespace cds_static
{
	class Triplet
	{
		public:
		uint subject;
		uint predicat;
		uint object;
	};

	class TriData
	{
		public:

		TriData(BitSequence* Bp, BitSequence* Bo, BitSequence* Bc, WaveletTree* Wtp, WaveletTree* WToi, WaveletTree* WToc);
		Triplet* access(int pos);

		protected:

		/**
		 * BitSequence
		 */
		BitSequence *Bp;
		BitSequence *Bo;
		BitSequence *Bc;

		/**
		 * WaveletTree
		 */
		WaveletTree *WTp;
		WaveletTree *WToi;
		WaveletTree *WToc;
	};


};


#endif
