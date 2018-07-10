LIB=libvcmylicense.so
LIB_VERSION=1.0.0
EX_LIBS=-lvcmylicense -lcrypto

ifneq ($(wildcard /usr/lib64),)
	LIB_DIR=/usr/lib64
else
	LIB_DIR=/usr/lib
endif

.PHONY: test clean install uninstall

all: $(LIB) test_save_req test_verify_cert

$(LIB):
	gcc -Wall -Werror -fPIC -shared -I. vcmy/license.c -o $@.$(LIB_VERSION)
	ln -s $@.$(LIB_VERSION) $@

test_save_req: $(LIB)
	gcc -Wall -Werror -I. -L. test/$@.c -o $@ $(EX_LIBS)

test_verify_cert: $(LIB)
	gcc -Wall -Werror -I. -L. test/$@.c -o $@ $(EX_LIBS)

test: test_save_req test_verify_cert
	LD_LIBRARY_PATH=. ./test_save_req client.key client.req
	LD_LIBRARY_PATH=. ./test_verify_cert ca.pem client.lic 1 client.key
	LD_LIBRARY_PATH=. ./test_verify_cert ca.pem client.lic 0

cleanreq:
	rm -f client.key client.req

cleancert:
	rm -f client.lic

clean: cleanreq cleancert
	rm -f $(LIB) $(LIB).$(LIB_VERSION) test_save_req test_verify_cert

install: all
	cp $(LIB).$(LIB_VERSION) $(LIB_DIR)
	ln -sf $(LIB_DIR)/$(LIB).$(LIB_VERSION) $(LIB_DIR)/$(LIB)

uninstall:
	rm -f $(LIB_DIR)/$(LIB)*
