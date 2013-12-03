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
	class TriData
	{
		public:
		TriData();


		protected:
		/* ?? A quoi servent ces facteurs... ?? */
		const static uint RGFactor = 20;
		const static uint RRRsample_Rate= 32;




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
