
#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
#include <BitSequenceBuilderRG.h>
#include <wt_node_internal.h>
#include <wt_coder_binary.h>
#include <Mapper.h>
#include <Sequence.h>

#include "TriData.h"
#include "Exception.h"

using namespace std;

namespace cds_static
{
	class Factory
	{

		public:


		/**
		 * 	Create the factory for TriData
		 */
		Factory(uint size, uint secondLayerSize, uint maxValue);

		/**
		 * Add a triplet to the factory
		 */
		void addTriplet(uint s, uint p, uint o, bool isConcept);

		/**
		 * Get the core TriData
		 * return a well construct TriData
		 */
		TriData *get();





		protected:


		/**
		 * BitString used to construct the bitmaps
		 */
		BitString *Bp;
		BitString *Bo;
		BitString *Bc;

		//their current index
		uint BpCurrentIndex;
		uint BoCurrentIndex;
		uint BcCurrentIndex;


		/**
		 * Arrays used to constructs the WaveletTrees
		 */
		Array *WTp;
		Array *WToi;
		Array *WToc;

		//their current index
		uint WTpCurrentIndex;
		uint WToiCurrentIndex;
		uint WTocCurrentIndex;


		/**
		 * temporary variable (used to make some verifications)
		 */
		uint previousSubject;
		uint previousPredicat;
		uint previousObject;



		/**
		 * Factor for the conversion in BitSequence
		 */
		const static uint BitSequenceSampleRate = 20;

		/**
		 *	Make a bitSequence with a bitString
		 */
		BitSequence* makeBitSequence(BitString bitString);



		/*
		 * Factor for the conversion in WaveletTree
		 */
		const static uint RGFactor = 20;
		const static uint RRRsample_Rate= 32;

		/**
		 * Make a tree with an Array
		 */
		WaveletTree* makeTree(Array array);




		/**
		 * Test the added triplet
		 */
		void testParameters(uint s, uint p, uint o);
	};
}
