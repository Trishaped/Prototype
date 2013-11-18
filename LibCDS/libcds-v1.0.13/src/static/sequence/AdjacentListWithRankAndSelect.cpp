#include "AdjacentListWithRankAndSelect.h"

namespace cds_static
{

  			AdjacentListWithRankAndSelect::AdjacentListWithRankAndSelect(const Array &a): Sequence(0){
   				l=new list<uint>;
				length = a.getLength();
   				for(size_t i=0;i<a.getLength();i++)
        			(*l).push_back(a.getField(i));
        		firstSelect = new bool(true);
        		last_seeked_symbol = new uint(0);
      			last_seeked_j = new size_t(0);
      			last_seeked_cur = new size_t(0);;
      			last_seeked_li = new list<uint>::iterator();
    		}
    
		    AdjacentListWithRankAndSelect::AdjacentListWithRankAndSelect():Sequence(0) {
				l = NULL;
			}
			
            AdjacentListWithRankAndSelect::~AdjacentListWithRankAndSelect(){
            }

            size_t AdjacentListWithRankAndSelect::rank(uint symbol, size_t pos) const{
            	list<uint>::iterator li;
            	size_t cpt=0;
            	size_t cur=0;
            	for(li=(*l).begin(); li != (*l).end(); ++li){
            		if(cur>pos)
            			break;
            		if((*li)==symbol)
            			cpt++;
            		cur++;
            	}
				return cpt;
            }
            
            
            size_t AdjacentListWithRankAndSelect::select(uint symbol, size_t j) const{
				list<uint>::iterator li;            
            	li=(*l).begin();
            	size_t cpt=j;
            	size_t cur=0;
            	if((!(*firstSelect))&&(symbol==(*last_seeked_symbol))&&((*last_seeked_j)<j)){
            		li=(*last_seeked_li);
            		cpt=2;
            		cur=(*last_seeked_cur);
            	}
            	(*firstSelect)=false;
            	for(; li != (*l).end(); ++li){
            		if((*li)==symbol)
            			cpt--;
            		if(cpt==0){
            			(*last_seeked_symbol)=symbol;
            			(*last_seeked_li)=li;
            			(*last_seeked_j)=j;
            			(*last_seeked_cur)=cur;
            			return cur;
            		}
            		cur++;
            	}
				return -1;
            }
            
            uint AdjacentListWithRankAndSelect::access(size_t pos) const {
            	list<uint>::iterator li;
            	size_t cur=0;
            	for(li=(*l).begin(); li != (*l).end(); ++li){
            		if(cur==pos)
            			return (*li);
            		cur++;
            	}
            	return cur;
            }
            
            size_t AdjacentListWithRankAndSelect::getSize() const{
            	return (*l).size()*sizeof(uint);
            }

            size_t AdjacentListWithRankAndSelect::count(uint symbol) const{
				return -1;
            }

            void AdjacentListWithRankAndSelect::save(ofstream & fp) const{
            	uint wr = ADJLIST_HDR;
				saveValue(fp,wr);
                saveValue<int>(fp,(*l).size());
                list<uint>::iterator li;
            	for(li=(*l).begin(); li != (*l).end(); ++li){
            		saveValue<uint>(fp,(*li));
            	}
            }
            
            AdjacentListWithRankAndSelect * AdjacentListWithRankAndSelect::load(ifstream & fp){
				uint rd = loadValue<uint>(fp);
				if(rd!=ADJLIST_HDR) return NULL;
				AdjacentListWithRankAndSelect * ret = new AdjacentListWithRankAndSelect();
            	ret->l=new list<uint>;
            	ret->firstSelect = new bool(true);
        		ret->last_seeked_symbol = new uint(0);
      			ret->last_seeked_j = new size_t(0);
      			ret->last_seeked_cur = new size_t(0);;
      			ret->last_seeked_li = new list<uint>::iterator();
            	int size=loadValue<int>(fp);
            	ret->length = size;
   				for(int i=0;i<size;i++){
   					uint j=loadValue<uint>(fp);
        			(*ret->l).push_back(j);
        		}
            	return ret;
            }
};
