
#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
#include <BitSequenceBuilderRG.h>
#include <wt_node_internal.h>
#include <wt_coder_binary.h>
#include <Mapper.h>
#include <Sequence.h>

#include "TriData.h"

using namespace std;

namespace cds_static
{
	class Factory
	{

		public:


		/**
		 * 	Create the factory for TriData
		 */
		Factory(uint size, uint maxValue);

		/**
		 * Add a triplet to the factory
		 */
		void addTriplet(uint s, uint p, uint o, bool isConcept);

		/**
		 * Get the core TriData
		 */
		TriData *get();





		protected:


		/**
		 * BitString used to map BitSequence
		 */
		BitString *Bp;
		BitString *Bo;
		BitString *Bc;

		uint BpCurrentIndex;
		uint BoCurrentIndex;
		uint BcCurrentIndex;

		/**
		 * Arrays used to makes the WaveletTrees
		 */
		Array *WTp;
		Array *WToi;
		Array *WToc;

		uint WTpCurrentIndex;
		uint WToiCurrentIndex;
		uint WTocCurrentIndex;


		/**
		 * temporary variable
		 */
		uint previousSubject;
		uint previousPredicat;


		/**
		 * Factor for the conversion in BitSequence
		 */
		const static uint BitSequenceSampleRate = 20;

		/*
		 * Factor for the conversion in WaveletTree
		 */
		const static uint RGFactor = 20;
		const static uint RRRsample_Rate= 32;



		/**
		 *	Make a bitSequence with a bitString
		 */
		BitSequence* makeBitSequence(BitString bitString);

		/**
		 * Make a tree with an Array
		 */
		WaveletTree* makeTree(Array array);

	};
}
