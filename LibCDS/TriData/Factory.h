
#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
#include <BitSequenceBuilderRG.h>
#include <wt_node_internal.h>
#include <wt_coder_binary.h>
#include <Mapper.h>
#include <Sequence.h>

#include <TriData.h>

using namespace std;

namespace cds_static
{
	class Factory
	{

		public:


		/**
		 * 	Create the factory for TriData
		 */
		Factory(uint size);

		/**
		 * Add a triplet to the factory
		 */
		void addTriplet(uint s, uint p, uint o);

		/**
		 * Get the core TriData
		 */
		TriData *get();





		protected:


		/**
		 * Size of the factory
		 */
		uint size;

		/**
		 * BitString used to map BitSequence
		 */
		BitString *Bp;
		BitString *Bo;
		BitString *Bc;

		/**
		 * Arrays used to makes the WaveletTrees
		 */
		Array *WTp;
		Array *WToi;
		Array *WToc;

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
