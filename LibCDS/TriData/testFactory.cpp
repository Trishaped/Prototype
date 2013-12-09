#include <libcdsBasics.h>
#include <BitSequence.h>
#include <Mapper.h>
#include <Sequence.h>

#include "TriData.h"
#include "Factory.h"
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
		Factory* factory = new Factory(coupleSP, tripletsCount, tripletsConceptCount, maxPredicate, maxInstance, maxConcept);

		/*	Add some triplets (s,p,o,isConcept) */
		factory->addTriplet(1 ,2 , 2, true);
		factory->addTriplet(1 ,2 , 3, true);
		factory->addTriplet(1 ,2 , 4, true);
		factory->addTriplet(1 ,2 , 5, true);
		factory->addTriplet(2 ,1 , 1, true);
		factory->addTriplet(2 ,1 , 2, false);
		factory->addTriplet(3 ,2 , 1, true);
		factory->addTriplet(3 ,2 , 4, false);

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

/*		 Tests for WaveletTree	*/
//uint size_of_heavy_bit(uint n){
//	uint pos=0;
//	if(n==0)
//		return 0;
//	do{
//		pos++;
//	}while((n=n/2)!=0);
//
//	return pos;
//}
//
//void test(WaveletTree & wt){
//	uint value;
//	uint posFirstBit;
//	uint i;
//	uint j;
//	for(;;){
//		/* Which sequence test */
//		cout << "Seeked Sequence ?" <<endl;
// 		cin >> value;
//		if(value==0) return;
//
//		/* Position of most left bit */
//		posFirstBit=size_of_heavy_bit(value)-1;
//    	cout << "Pos first bit: " << posFirstBit << ", calc: " << value << "-2^" << posFirstBit << " = " << value-pow(2, posFirstBit) << endl; //Here i don't know why "w-2^b" ?
//
//    	/*	Rank */
//    	cout << "Rank      :" << wt.rank(value, wt.getLength()-1) << endl;
//		cout << "Rank onto :" << wt.rank_prefix(value-pow(2,posFirstBit), wt.getLength()-1, posFirstBit) << endl;
//
//    	/* Select */
//    	cout << "occ ?" <<endl;
//		cin >> i;
//
//		j=wt.select(value, i);
//		cout << "Select      : ";
//		if(j==(uint)-1)
//			cout << "none" << endl;
//		else
//			cout << j << endl;
//
//		j=wt.select_prefix(value-pow(2, posFirstBit), i, posFirstBit);
//		cout << "Select onto : ";
//        if(j==(uint)-1)
//        	cout << "none" << endl;
//        else
//            cout << j << endl;
//
//	}
//}

