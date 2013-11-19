CC?=gcc
AR?=ar
BINDIR?=/usr/bin
LIBDIR?=/usr/local/lib

I18 = fr_FR

EXE = trishaped

DESTDIR=

LIB_INFINT_N=libinfint.so
LIB_INFINT_V=1.0
LIB_CORE_N=libchallengercore.so
LIB_CORE_V=1.0

CFLAGS_INCLUDE = -I./src/ -I./include -L$(LIBDIR) -Wl,-rpath,$(LIBDIR) -D_GNU_SOURCE
CFLAGS += $(CFLAGS_INCLUDE) -Wall -Wextra -Werror -pedantic
CFLAGS_DEBUG = $(CFLAGS) -O0 -g $(CFLAGS_DEBUG_EXTRA)
CFLAGS_FAST = $(CFLAGS) -Os $(CFLAGS_FAST_EXTRA) -march=native \
 -mtune=generic -pipe -fstack-protector --param=ssp-buffer-size=4  \
 -D_FORTIFY_SOURCE=2 -DNDEBUG
CFLAGS_LIB = $(CFLAGS_FAST) -fPIC
CFLAGS_SHARED = $(CFLAGS_FAST) -shared

all: trishaped

install:
	install -d -m 0755 -o root -g root $(DESTDIR)/$(BINDIR)
	install -m 0755 -o root -g root $(EXE) $(DESTDIR)/$(BINDIR)
	install -d -m 0755 -o root -g root $(DESTDIR)/$(LIBDIR)
	install -m 0755 -o root -g root ./lib/$(LIB_INFINT_N) \
 $(DESTDIR)/$(LIBDIR)/$(LIB_INFINT_N).$(LIB_INFINT_V)
	install -m 0755 -o root -g root ./lib/$(LIB_CORE_N) \
 $(DESTDIR)/$(LIBDIR)/$(LIB_CORE_N).$(LIB_CORE_V)
	ln -sf $(DESTDIR)/$(LIBDIR)/$(LIB_INFINT_N).$(LIB_INFINT_V) \
 $(DESTDIR)/$(LIBDIR)/$(LIB_INFINT_N)
	ln -sf $(DESTDIR)/$(LIBDIR)/$(LIB_CORE_N).$(LIB_CORE_V) \
 $(DESTDIR)/$(LIBDIR)/$(LIB_CORE_N)

# GROUP

test: test_byte test_mutable_infint test_infint test_io test_read test_write \
 test_dlist test_infint_factory test_interval test_infint_writer test_main_utils
	./bin/test_byte
	./bin/test_mutable_infint
	./bin/test_infint
	./bin/test_io
	./bin/test_read
	./bin/test_write
	./bin/test_interval
	./bin/test_dlist
	./bin/test_infint_factory
	./bin/test_infint_writer
	./bin/test_main_utils

test_valgrind: test
	valgrind ./bin/test_byte
	valgrind ./bin/test_mutable_infint
	valgrind ./bin/test_infint
	valgrind ./bin/test_io
	valgrind ./bin/test_read
	valgrind ./bin/test_write
	valgrind ./bin/test_interval
	valgrind ./bin/test_dlist
	valgrind ./bin/test_infint_factory
	valgrind ./bin/test_infint_writer
	valgrind ./bin/test_main_utils

library: libinfint.so libchallengercore.so
	ls -l lib/ | grep .so


# EXECUTABLE

challenger: src/main.o libmain.a libinfint.so libchallengercore.so
	@mkdir -p bin/
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_FAST) $< -lmain -linfint \
 -lchallengercore -o bin/$@
	@cp bin/$@ .
	@echo Compilation of $@ [DONE]

trishaped: src/main.o
	mkdir -p bin/
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_FAST) $< -luv -o bin/$@
	cp bin/$@ .
	@echo Compilation of $@ [DONE]

# TEST

test_common: libtest.a
	@mkdir -p bin/
	@mkdir -p tmp/

test_%: src/test/test_%.o test_common
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) $< -ltest -o bin/$@
	@echo Compilation of $@ [DONE]


# LIBRARY

libinfint.so: src/infint/lib-mutable_infint.o src/infint/lib-infint.o \
 src/infint/lib-byte.o src/io/lib-io.o src/io/lib-read.o src/io/lib-write.o \
 src/dlist/lib-dlist.o src/interval/lib-interval.o \
 src/infint_utils/lib-infint_factory.o src/infint_utils/lib-infint_writer.o
	@mkdir -p lib/
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_SHARED) $^ -o lib/$@
	@echo Creation of $@ [DONE]

libchallengercore.so: src/core/lib-core.o
	@mkdir -p lib/
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_SHARED) $^ -o lib/$@
	@echo Creation of $@ [DONE]

libtest.a: src/infint/debug-mutable_infint.o src/infint/debug-infint.o \
 src/infint/debug-byte.o src/io/debug-io.o src/io/debug-read.o \
 src/io/debug-write.o src/test/test.o src/dlist/debug-dlist.o \
 src/interval/debug-interval.o src/infint_utils/debug-infint_factory.o \
 src/infint_utils/debug-infint_writer.o src/debug-options.o \
 src/main_utils/debug-stream.o src/main_utils/debug-list.o
	@mkdir -p lib/
	$(AR) rs lib/$@ $^
	@echo Creation of $@ [DONE]

libmain.a: src/options.o src/main_utils/stream.o \
 src/main_utils/list.o src/main_utils/process.o
	@mkdir -p lib/
	$(AR) rs lib/$@ $^
	@echo Creation of $@ [DONE]


# MISCELLANEOUS

.PHONY: clean mrproper

flags:
	@echo CFLAGS: $(CFLAGS)
	@echo CFLAGS_DEBUG: $(CFLAGS_DEBUG)
	@echo CFLAGS_FAST: $(CFLAGS_FAST)
	@echo CFLAGS_DEBUG_EXTRA: $(CFLAGS_DEBUG_EXTRA)
	@echo CFLAGS_FAST_EXTRA: $(CFLAGS_FAST_EXTRA)
	@echo CFLAGS_LIB: $(CFLAGS_LIB)
	@echo CFLAGS_SHARED: $(CFLAGS_SHARED)

clean: 
	rm -f src/*.o
	rm -f src/core/*.o
	rm -f src/dlist/*.o
	rm -f src/infint/*.o
	rm -f src/infint_utils/*.o
	rm -f src/interval/*.o
	rm -f src/io/*.o
	rm -f src/main_utils/*.o
	rm -f src/test/*.o
	rm -rf bin/*
	rm -rf lib/*
	rm -rf tmp

mrproper: clean
	@touch $(EXE)
	@touch src/i18.h
	rm $(EXE)
	rm src/i18.h

i18:
	cp src/i18{-$(I18),}.h

# BINARY

src/debug-%.o: src/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/core/lib-%.o: src/core/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@

src/dlist/debug-%.o: src/dlist/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/dlist/lib-%.o: src/dlist/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@
	
src/interval/debug-%.o: src/interval/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/interval/lib-%.o: src/interval/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@

src/infint/debug-%.o: src/infint/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/infint/lib-%.o: src/infint/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@

src/infint_utils/debug-%.o: src/infint_utils/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/infint_utils/lib-%.o: src/infint_utils/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@

src/io/debug-%.o: src/io/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/io/lib-%.o: src/io/%.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_LIB) -c -ansi $< -o $@

src/main_utils/debug-%.o: src/main_utils/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

src/test/%.o: src/test/%.c
	$(CC) $(CPPFLAGS_DEBUG) $(CFLAGS_DEBUG) -c -ansi $< -o $@

%.o: %.c
	$(CC) $(CPPFLAGS_FAST) $(CFLAGS_FAST) -c -ansi $< -o $@

