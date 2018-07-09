#include <stdio.h>
#include <openssl/pem.h>
#include "vcmy/license.h"

int main(int argc, char *argv[])
{
    if (argc != 3) {
        const char *szProgram = argv[0];
        printf("Usage:\t%s <client_key> <client_req>\n", szProgram);
        printf("eg.\t%s  client.key   client.req\n", szProgram);
        exit(1);
    }

    const char *szClientPrivateKeyPath = argv[1];
    const char *szCertificateRequestPath = argv[2];
    EVP_PKEY   *pkey = NULL;

    if (vcmy_exists(szClientPrivateKeyPath)) {
        pkey = vcmy_load_key(szClientPrivateKeyPath);

        if (!vcmy_exists(szCertificateRequestPath)) {
            // Generate certificate request file if it does not exist
            vcmy_save_x509_req(pkey, szCertificateRequestPath);
        }
    } else {
        pkey = vcmy_generate_key(szClientPrivateKeyPath);

        // Regenerate certificate request file after private key generated
        vcmy_save_x509_req(pkey, szCertificateRequestPath);
    }

    EVP_PKEY_free(pkey);
    return 0;
}
