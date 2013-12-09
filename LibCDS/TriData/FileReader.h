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
	class FileReader
	{

	public:
		TriData *readFile(const char* fileName);

	};
}
