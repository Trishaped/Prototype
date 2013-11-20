#include <libcdsBasics.h>
#include <BitSequence.h>
#include <Mapper.h>
#include <Sequence.h>

using namespace std;
using namespace cds_static;

uint size_of_heavy_bit(uint n){
	uint pos=0;
	if(n==0)
		return 0;
	for(;;){
		pos++;
		n=n/2;
		if(n==0)
			return pos;
	}
}

void test(WaveletTree & bs){
	uint w;
	uint b;
	uint i;
	uint j;
	for(;;){
		cout << "Seeked Sequence ?" <<endl;
 		cin >> w;
		cout << w << "?"<<endl;
		if(w==0) return;
    	b=size_of_heavy_bit(w)-1;
    	cout << b <<" " << w-pow(2,b) << endl;
    		cout << "Rank " << bs.rank(w,bs.getLength()-1) << endl;
		cout << "Rank onto " << bs.rank_prefix(w-pow(2,b),bs.getLength()-1,b) << endl;
		cout << "occ ?" <<endl;
		cin >> i;
		cout << i << "?"<<endl;
		j=bs.select(w,i);
		cout << "Select ";
		if(j==(uint)-1)
			cout << "none";
		else
			cout << j;
		cout << endl;
		j=bs.select_prefix(w-pow(2,b),i,b);
		cout << "Select onto"; 
                if(j==(uint)-1)
                        cout << "none";
                else
                        cout << j;
                cout << endl;	
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
		Array b(v,8);

//		Mapper * mapper = new MapperCont(b, BitSequenceBuilderRG(20));
//		mapper->use();
		WaveletTree wt1(b, new wt_coder_binary(b, new MapperCont(b, BitSequenceBuilderRG(20))), new BitSequenceBuilderRRR(32), new MapperNone());
		test(wt1);		

  return 0;
}

