#ifndef STATIC_DATA_H
#define STATIC_DATA_H

#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
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
		virtual WaveletTree makeTree(Array array) const;
		virtual ~TriData();

		protected:
		/* ?? A quoi servent ces facteurs... ?? */
		const static uint RGFactor = 20;
		const static uint RRRsample_Rate= 32;
	};
};


#endif
