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
	vector<uint> v;
    v.push_back(1);
    v.push_back(3);
	v.push_back(4);
	v.push_back(5);
	v.push_back(8);
	v.push_back(1);
	v.push_back(3);
	v.push_back(4);
	v.push_back(5);
	v.push_back(8);
	Array array(v,8);

	Factory* factory = new Factory((uint)10);

	return 0;
}

