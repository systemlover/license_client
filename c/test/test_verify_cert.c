#include <openssl/pem.h>
#include <openssl/x509.h>
#include "vcmy/license.h"

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        const char *szProgramName = argv[0];
        printf("Usage:\t%s <ca_pem_path> <license_path>\n", szProgramName);
        printf("eg.\t%s ca.pem        client.lic\n", szProgramName);
        exit(1);
    }

    const char *szCACertificatePath = argv[1];
    const char *szClientCertificatePath = argv[2];

    if (!vcmy_exists(szCACertificatePath))
    {
        printf("CA certificate not found: %s\n", szCACertificatePath);
        exit(1);
    }
    if (!vcmy_exists(szClientCertificatePath))
    {
        printf("Client certificate not found: %s\n", szClientCertificatePath);
        exit(1);
    }

    X509 *ca_cert = vcmy_load_x509_cert(szCACertificatePath);
    X509 *client_cert = vcmy_load_x509_cert(szClientCertificatePath);
    if (vcmy_verify_x509_cert(ca_cert, client_cert))
    {
        printf("Client certificate is valid!\n");
    }
    else
    {
        printf("Client certificate is NOT valid!\n");
    }

    X509_free(ca_cert);
    X509_free(client_cert);
    return 0;
}
