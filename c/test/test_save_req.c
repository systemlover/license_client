#include <openssl/pem.h>
#include "vcmy/license.h"

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        const char *szProgramName = argv[0];
        printf("Usage:\t%s <key_path> <req_path>\neg.\t%s client.key client.req\n",
            szProgramName, szProgramName);
        exit(1);
    }

    const char *szClientPrivateKeyPath = argv[1];
    const char *szCertificateRequestPath = argv[2];
    EVP_PKEY   *pKey = NULL;

    if (vcmy_exists(szClientPrivateKeyPath))
    {
        pKey = vcmy_load_key(szClientPrivateKeyPath);

        if (!vcmy_exists(szCertificateRequestPath))
        {
            // Generate certificate request file if it does not exist
            vcmy_save_x509_req(pKey, szCertificateRequestPath);
        }
    }
    else
    {
        pKey = vcmy_generate_key(szClientPrivateKeyPath);

        // Regenerate certificate request file after private key generated
        vcmy_save_x509_req(pKey, szCertificateRequestPath);
    }

    EVP_PKEY_free(pKey);
    return 0;
}
