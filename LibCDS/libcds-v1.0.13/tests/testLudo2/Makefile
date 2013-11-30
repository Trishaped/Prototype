CPP=g++

CPPFLAGS=-g3 -Wall -O0 -I../includes/ 
INCS=-I../../includes/
LIB=../../lib/libcds.a


OBJECTS= TriData.o testLudo.o 
BIN= testLudo 

%.o: %.cpp
	@echo " [C++] Compiling $<"
	@$(CPP) $(CPPFLAGS) $(INCS) -c $< -o $@


all: clean $(OBJECTS) $(BIN)
	@echo " [MSG] Done compiling tests"
	@echo " [FLG] $(CPPFLAGS)"

testLudo:
	@echo " [LNK] Building testLudo"
	@$(CPP) $(CPPFLAGS) -o testLudo testLudo.o $(LIB)
	
	

clean:
	@echo " [CLN] Cleaning object and binary files"
	@rm -f $(OBJECTS) $(BIN)