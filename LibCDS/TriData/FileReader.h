#include <libcdsBasics.h>
#include <BitSequence.h>
#include <BitSequenceBuilder.h>
#include <BitSequenceBuilderRG.h>
#include <wt_node_internal.h>
#include <wt_coder_binary.h>
#include <Mapper.h>
#include <Sequence.h>


#include "RDFStore.h"
#include "RDFStoreFactory.h"
#include "Exception.h"


using namespace std;

namespace cds_static
{
	class FileReader
	{

	public:
		RDFStore *readFile(const char* fileName);

	};
}
