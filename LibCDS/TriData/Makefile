CPP=g++

CPPFLAGS=-g3 -Wall -O0 
INCS=-I../libcds-v1.0.13/includes/
LIB=../libcds-v1.0.13/lib/libcds.a


OBJECTS= TriData.o Factory.o testFactory.o 
BIN= testFactory 

%.o: %.cpp
	@echo " [C++] Compiling $<"
	@$(CPP) $(CPPFLAGS) $(INCS) -c $< -o $@


all: clean $(OBJECTS) $(BIN)
	@echo " [MSG] Done compiling tests"
	@echo " [FLG] $(CPPFLAGS)"

testFactory:
	@echo " [LNK] Building testFactory"
	@$(CPP) $(CPPFLAGS) -o testFactory $(OBJECTS) $(LIB)

clean:
	@echo " [CLN] Cleaning object and binary files"
	@rm -f $(OBJECTS) $(BIN)