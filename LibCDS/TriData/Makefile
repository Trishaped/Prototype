CPP=g++

CPPFLAGS=-g3 -Wall -O0 
INCS=-I../libcds-v1.0.13/includes/
LIB=../libcds-v1.0.13/lib/libcds.a

# To debug in LibCDS we have to compile object by ourself 
DEBUG_LIB = src/static/sequence/WaveletTree.o src/static/sequence/wt_coder_binary.o src/static/sequence/wt_node_internal.o 

OBJECTS= TriData.o Factory.o testFactory.o 
BIN= testFactory 


%.o: %.cpp
	@echo " [C++] Compiling $<"
	@$(CPP) $(CPPFLAGS) $(INCS) -c $< -o $@


all: clean $(DEBUG_LIB) $(OBJECTS) $(BIN)
	@echo " [MSG] Done compiling tests"
	@echo " [FLG] $(CPPFLAGS)"



testFactory:
	@echo " [LNK] Building testFactory"
	@$(CPP) $(CPPFLAGS) -o testFactory $(OBJECTS) $(DEBUG_LIB) $(LIB)
	
	


	
	
	
	
clean:
	@echo " [CLN] Cleaning object and binary files"
	@rm -f $(OBJECTS) $(BIN)