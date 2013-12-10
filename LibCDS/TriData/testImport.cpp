#include <libcdsBasics.h>
#include <BitSequence.h>
#include <Mapper.h>
#include <Sequence.h>

#include "TriData.h"
#include "RDFStoreFactory.h"
#include "FileReader.h"

using namespace std;
using namespace cds_static;


int main(int argc, char ** argv) {
	try{

		FileReader fr;
		fr.readFile("triplets.txt");



	}catch(exception* e){
		cout << "Error: " << e->what();	// An error occurred (wrong params)
		return 1;
	}
	return 0;
}
