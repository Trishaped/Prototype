
#include "TriData.h"

namespace cds_static
{

/* Used to print a triplet */
ostream& operator<<(std::ostream& stream, const cds_static::Triplet& triplet) {
	stream << "(" << triplet.subject << ", " << triplet.predicat << ", " << triplet.object << ")";
    return stream;
 }

/**
 * 	Constructor for TriData core
 * 	See Factory for construction details
 */
TriData::TriData(BitSequence* Bp, BitSequence* Bo, BitSequence* Bc, WaveletTree* WTp, WaveletTree* WToi, WaveletTree* WToc){
	this->Bp = Bp;
	this->Bo = Bo;
	this->Bc = Bc;

	this->WTp = WTp;
	this->WToi = WToi;
	this->WToc = WToc;
}


/**
 * access method on the structure
 * @param pos: triplet position in the structure
 */
Triplet& TriData::access(uint pos){
	Triplet *triplet = new Triplet();

	/*	We begin by the bottom of the data structure */
	bool isConcept = Bc->access(pos);
	uint nbConcept = Bc->rank1(pos);
	if(isConcept)
		triplet->object = WToc->access(nbConcept - 1);
	else
		triplet->object = WToi->access(pos - nbConcept);


	/*	Then we find the predicat and the subject associated */
	uint nbPre = Bo->rank1(pos);
	triplet->predicat = WTp->access(nbPre-1);
	triplet->subject = Bp->rank1(nbPre-1);

	return *triplet;
}


}
