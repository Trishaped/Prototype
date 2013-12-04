#include <libcdsBasics.h>
#include <BitSequence.h>
#include <Mapper.h>
#include <Sequence.h>

#include "TriData.h"
#include "Factory.h"

using namespace std;
using namespace cds_static;

uint size_of_heavy_bit(uint n){
	uint pos=0;
	if(n==0)
		return 0;
	do{
		pos++;
	}while((n=n/2)!=0);

	return pos;
}

void test(WaveletTree & wt){
	uint value;
	uint posFirstBit;
	uint i;
	uint j;
	for(;;){
		/* Which sequence test */
		cout << "Seeked Sequence ?" <<endl;
 		cin >> value;
		if(value==0) return;

		/* Position of most left bit */
		posFirstBit=size_of_heavy_bit(value)-1;
    	cout << "Pos first bit: " << posFirstBit << ", calc: " << value << "-2^" << posFirstBit << " = " << value-pow(2, posFirstBit) << endl; //Here i don't know why "w-2^b" ?

    	/*	Rank */
    	cout << "Rank      :" << wt.rank(value, wt.getLength()-1) << endl;
		cout << "Rank onto :" << wt.rank_prefix(value-pow(2,posFirstBit), wt.getLength()-1, posFirstBit) << endl;

    	/* Select */
    	cout << "occ ?" <<endl;
		cin >> i;

		j=wt.select(value, i);
		cout << "Select      : ";
		if(j==(uint)-1)
			cout << "none" << endl;
		else
			cout << j << endl;

		j=wt.select_prefix(value-pow(2, posFirstBit), i, posFirstBit);
		cout << "Select onto : ";
        if(j==(uint)-1)
        	cout << "none" << endl;
        else
            cout << j << endl;

	}
}


int main(int argc, char ** argv) {

	uint size = 20;
	uint maxValue = 20;

	Factory* factory = new Factory(size, maxValue);

	factory->addTriplet(1 ,2 , 3, true);
	factory->addTriplet(1 ,2 , 3, false);
	factory->addTriplet(2 ,1 , 1, true);
	factory->addTriplet(2 ,1 , 1, false);
	factory->addTriplet(3 ,1 , 1, true);
	factory->addTriplet(3 ,2 , 3, false);

	TriData* triData = factory->get();

	Triplet *triplet = triData->access(0);
	cout << "Subject: " << triplet->subject << " Predicat: " << triplet->predicat << " Object: " << triplet->object << endl;

	return 0;
}

