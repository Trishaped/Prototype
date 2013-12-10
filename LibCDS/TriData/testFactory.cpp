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

		/*  Some variables, must correspond to inserted triplets  */
		uint coupleSP = 3;
		uint tripletsCount = 8;
		uint tripletsConceptCount = 6;
		uint maxPredicate = 2;
		uint maxInstance = 4;
		uint maxConcept = 5;

		/*	Create the Factory */
		RDFStoreFactory* factory = new RDFStoreFactory(coupleSP, tripletsCount, tripletsConceptCount, maxPredicate, maxInstance, maxConcept);

		/*	Add some triplets (s,p,o,isConcept) */
		factory->addTriplet(1 ,2 , 2);
		factory->addTriplet(1 ,2 , 3);
		factory->addTriplet(1 ,2 , 4);
		factory->addTriplet(1 ,2 , 5);
		factory->addTriplet(2 ,1 , 1);
		factory->addTriplet(2 ,1 , 2);
		factory->addTriplet(3 ,2 , 1);
		factory->addTriplet(3 ,2 , 4);

		/*	Make the Core */
		TriData* triData = factory->get();

		/*	Some tests	*/
		for(uint i=0; i<tripletsCount; i++){
			Triplet *triplet = triData->access(i);
			cout << "access(" << i << ") => (" << triplet->subject << ", " << triplet->predicat << ", " << triplet->object << ")" << endl;
		}


	}catch(exception* e){
		cout << "Error: " << e->what();	// An error occurred (wrong params)
		return 1;
	}

	return 0;
}

