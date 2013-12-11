
#include "FileReader.h"

namespace cds_static {

TriData *FileReader::readFile(const char* fileName) {
	TriData *triData = NULL;
	string line;
	ifstream file(fileName);

	if (file.is_open()) {
		int tripletCount, coupleSPCount, tripletConceptCount, maxPredicat, maxInstance, maxConcepts;
		file >> tripletCount >> coupleSPCount  >> tripletConceptCount >> maxPredicat >> maxInstance >> maxConcepts;

		RDFStoreFactory factory(tripletCount, coupleSPCount, tripletConceptCount, maxPredicat, maxInstance, maxConcepts);

		uint subject, predicate, object, i=0;
		while(file.good()){
			file >> subject;
			file >> predicate;
			file >> object;
			i++;
			try{
				factory.addTriplet(subject, predicate, object);
			}catch(exception *e){
				throw new Exception(Stream() << " reading file, line " << (i+1) << ": " << e->what());
			}
		}

		if(i!=tripletCount)
			throw new Exception(Stream() << "Unexpected end of file: " << fileName);

		triData = factory.get();

		file.close();
	} else {
		throw new Exception(Stream() << "Error opening the file: " << fileName);
	}

	return triData;
}

}

