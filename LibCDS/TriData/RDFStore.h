#ifndef STATIC_RDFStore_H
#define STATIC_RDFStore_H

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
		/* Used to print a triplet */
		friend ostream& operator<<(ostream& os, const cds_static::Triplet& triplet);
	};

	/**
	 * 	## The CORE ###
	 * 	Contains:
	 * 	3 WaveletTree and
	 * 	3 BitSequence
	 *
	 * 	Method:
	 * 	TriData() //Use Factory to do that
	 * 	access(i)
	 */
	class RDFStore
	{
		public:

		/**
		 * 	Constructor for TriData core
		 * 	See Factory for construction details
		 */
		RDFStore(BitSequence* Bp, BitSequence* Bo, BitSequence* Bc, WaveletTree* Wtp, WaveletTree* WToi, WaveletTree* WToc);

		/**
		 * access method on the structure
		 * @param pos: triplet position in the structure
		 */
		Triplet& access(uint pos);


		uint nbTriplet;

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
