

#include "FileReader.h"

namespace cds_static
{

TriData *FileReader::readFile(const char* fileName){
	string line;
	ifstream file(fileName);
	if (file.is_open())
	  {
	    while ( getline (file,line) )
	    {
	      cout << line << '\n';
	    }
	    file.close();
	  }
	  else{
		throw new Exception(Stream() << "Error opening the file " << fileName);
	  }

	  return 0;
	}
}

