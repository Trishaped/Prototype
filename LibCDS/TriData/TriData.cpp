
#include "TriData.h"

namespace cds_static
{

TriData::TriData(BitSequence* Bp, BitSequence* Bo, BitSequence* Bc, WaveletTree* WTp, WaveletTree* WToi, WaveletTree* WToc){
	this->Bp = Bp;
	this->Bo = Bo;
	this->Bc = Bc;

	this->WTp = WTp;
	this->WToi = WToi;
	this->WToc = WToc;
}

Triplet* TriData::access(int pos){
	Triplet* triplet = new Triplet();

	triplet->subject = Bp->rank1(pos);

	triplet->predicat = WTp->access(pos);

	bool isConcept = Bc->access(pos);
	uint nbConcept = Bc->rank1(pos);
	if(isConcept)
		triplet->object = WToc->access(nbConcept - 1);
	else
		triplet->object = WToi->access(pos - nbConcept);

	return triplet;

}


}
