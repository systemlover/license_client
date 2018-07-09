#include <stdio.h>
#include <openssl/pem.h>
#include <openssl/x509.h>
#include "vcmy/license.h"

int main(int argc, char *argv[])
{
    if (argc < 4 || argc > 5) {
        const char *szProgram = argv[0];
        printf("Usage:\t%s <ca_pem> <client_lic> <check_pubkey> [client_key]\n",
                szProgram);
        printf("eg.\t%s  ca.pem   client.lic   1              client.key\n",
                szProgram);
        printf("eg.\t%s  ca.pem   client.lic   0\n", szProgram);
        exit(1);
    }

    const char *szCACertificatePath = argv[1];
    const char *szClientCertificatePath = argv[2];
    const int  check_pubkey = atoi(argv[3]);
    const char *szClientPrivateKeyPath = argv[4];

    X509       *ca_cert = NULL;
    X509       *client_cert = NULL;
    EVP_PKEY   *pkey = NULL;

    if (!vcmy_exists(szCACertificatePath)) {
        printf("CA certificate not found: %s\n", szCACertificatePath);
        exit(1);
    }
    if (!vcmy_exists(szClientCertificatePath)) {
        printf("Client certificate not found: %s\n", szClientCertificatePath);
        exit(1);
    }
    if (check_pubkey && !vcmy_exists(szClientPrivateKeyPath)) {
        printf("Client private key not found: %s\n", szClientPrivateKeyPath);
        exit(1);
    }

    ca_cert = vcmy_load_x509_cert(szCACertificatePath);
    client_cert = vcmy_load_x509_cert(szClientCertificatePath);

    if (check_pubkey) {
        pkey = vcmy_load_key(szClientPrivateKeyPath);
    }
    if (vcmy_verify_x509_cert(ca_cert, client_cert, check_pubkey, pkey)) {
        printf("Client certificate is valid!\n");
    } else {
        printf("Client certificate is NOT valid!\n");
    }

    X509_free(ca_cert);
    X509_free(client_cert);
    EVP_PKEY_free(pkey);
    return 0;
}
