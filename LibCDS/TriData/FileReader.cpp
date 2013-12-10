
#include "FileReader.h"

namespace cds_static {

TriData *FileReader::readFile(const char* fileName) {
	TriData *triData = NULL;

	string line;
	ifstream file(fileName);

	if (file.is_open()) {
		uint tripletCount, coupleSPCount, tripletConceptCount, maxPredicat, maxInstance, maxConcepts;
		file >> tripletCount >> coupleSPCount  >> tripletConceptCount >> maxPredicat >> maxInstance >> maxConcepts;

		RDFStoreFactory factory(tripletCount, coupleSPCount, tripletConceptCount, maxPredicat, maxInstance, maxConcepts);

		uint subject, predicate, object;
		while(file >> subject >> predicate >> object){
			factory.addTriplet(subject, predicate, object);
		}

		triData = factory.get();

		file.close();
	} else {
		throw new Exception(Stream() << "Error opening the file: " << fileName);
	}

	return triData;
}

}

