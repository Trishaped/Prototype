#include <libcdsBasics.h>
#include <BitSequence.h>
#include <Mapper.h>
#include <Sequence.h>

#include "RDFStore.h"
#include "RDFStoreFactory.h"
#include "FileReader.h"

using namespace std;
using namespace cds_static;


int main(int argc, char ** argv) {
	try{
		FileReader fr;
		//fr.readFile("triplets.txt");
		RDFStore* data = fr.readFile("TriData/triplets.txt");	//FAIL: Eclipse lance l'exe depuis la racine du projet


		cout << "test: " << data->access(0) << endl;

	}catch(exception* e){
		cout << "Error: " << e->what() << endl;	// An error occurred (wrong params)
		return 1;
	}
	return 0;
}
