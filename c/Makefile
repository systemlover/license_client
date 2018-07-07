LIB=liblicense.so
EX_LIBS=-llicense -lcrypto

.PHONY: test clean

all: $(LIB) test_save_req test_verify_cert

$(LIB):
	gcc -Wall -Werror -fPIC -shared -I. vcmy/license.c -o $@

test_save_req: $(LIB)
	gcc -Wall -Werror -I. -L. test/$@.c -o $@ $(EX_LIBS)

test_verify_cert: $(LIB)
	gcc -Wall -Werror -I. -L. test/$@.c -o $@ $(EX_LIBS)

test: test_save_req test_verify_cert
	LD_LIBRARY_PATH=. ./test_save_req client.key client.req
	LD_LIBRARY_PATH=. ./test_verify_cert ca.pem client.lic

cleanreq:
	rm -f client.key client.req

cleancert:
	rm -f client.lic

clean: cleanreq cleancert
	rm -f $(LIB) test_save_req test_verify_cert
