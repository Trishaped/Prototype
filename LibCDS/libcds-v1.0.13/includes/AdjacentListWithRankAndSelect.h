#ifndef _ALWRAS_H
#define _ALWRAS_H

#include <libcdsBasics.h>
#include <Sequence.h>
#include <list>

using namespace std;

namespace cds_static
{
	class AdjacentListWithRankAndSelect : public Sequence
	{
   		public:
   			AdjacentListWithRankAndSelect(const Array &a);

   			AdjacentListWithRankAndSelect();
    		
            virtual ~AdjacentListWithRankAndSelect();

            virtual size_t rank(uint symbol, size_t pos) const;
            
            virtual size_t select(uint symbol, size_t j) const;
            
            virtual uint access(size_t pos) const;
            
            virtual size_t getSize() const;

            virtual size_t count(uint symbol) const;
            
            virtual void save(ofstream & fp) const;
            
            static AdjacentListWithRankAndSelect * load(ifstream & fp);              

   		protected:
      		list<uint> * l;
      		bool* firstSelect;
      		uint* last_seeked_symbol;
      		size_t* last_seeked_j;
      		size_t* last_seeked_cur;
      		list<uint>::iterator* last_seeked_li;
	};
};

#endif
