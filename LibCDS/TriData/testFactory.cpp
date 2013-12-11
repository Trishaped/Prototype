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

		/*  Some variables, must correspond to inserted triplets  */
		uint tripletsCount = 11;
		uint coupleSP = 5;
		uint tripletsConceptCount = 3;
		uint maxPredicate = 2;
		uint maxInstance = 5;
		uint maxConcept = 2;

		/*	Create the Factory */
		RDFStoreFactory* factory = new RDFStoreFactory(tripletsCount, coupleSP, tripletsConceptCount, maxPredicate, maxInstance, maxConcept);
		/*	Add some triplets (s,p,o,isConcept) */
		factory->addTriplet(1 ,0 , 1);
		factory->addTriplet(1 ,0 , 2);
		factory->addTriplet(1 ,2 , 2);
		factory->addTriplet(1 ,2 , 3);
		factory->addTriplet(1 ,2 , 4);
		factory->addTriplet(1 ,2 , 5);
		factory->addTriplet(2 ,0 , 1);
		factory->addTriplet(2 ,1 , 1);
		factory->addTriplet(2 ,1 , 2);
		factory->addTriplet(3 ,2 , 1);
		factory->addTriplet(3 ,2 , 4);

		/*	Make the Core */
		RDFStore* triData = factory->get();

		/*	Some tests	*/
		for(uint i=0; i<tripletsCount; i++){
			Triplet triplet = triData->access(i);
			cout << "access(" << i << ") => (" << triplet.subject << ", " << triplet.predicat << ", " << triplet.object << ")" << endl;
		}


	}catch(exception* e){
		cout << "Error: " << e->what() << endl;	// An error occurred (wrong params)
		return 1;
	}

	return 0;
}

