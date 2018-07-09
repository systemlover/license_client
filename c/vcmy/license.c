#include <unistd.h>
#include <time.h>
#include <openssl/bn.h>
#include <openssl/rsa.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/pem.h>
#include <openssl/asn1t.h>
#include <openssl/x509.h>
#include <openssl/x509_vfy.h>

#include "vcmy/license.h"

int vcmy_exists(const char *filename)
{
    return access(filename, F_OK) != -1;
}

EVP_PKEY *vcmy_generate_key(const char *szPath)
{
    int         ret = 0;
    const int   bits = 2048;
    EVP_PKEY    *pkey = NULL;
    RSA         *rsa = NULL;
    BIO         *out = NULL;

    BIGNUM *bignum = BN_new();
    ret = BN_set_word(bignum, RSA_F4);
    if (ret != 1) goto end;

    rsa = RSA_new();
    ret = RSA_generate_key_ex(rsa, bits, bignum, NULL);
    if (ret != 1) goto end;

    pkey = EVP_PKEY_new();
    ret = EVP_PKEY_assign_RSA(pkey, rsa);
    if (ret != 1) goto end;

    // set rsa to NULL according to the man page of EVP_PKEY_assign_RSA
    // See: man 3 EVP_PKEY_assign_RSA
    // "these use the supplied key internally and so key will be freed
    // when the parent pkey is freed."
    rsa = NULL;

    out = BIO_new_file(szPath, "w");
    ret = PEM_write_bio_PKCS8PrivateKey(out, pkey, NULL, NULL, 0, 0, NULL);
    if (ret != 1) goto end;

end:
    BIO_free_all(out);
    RSA_free(rsa);
    BN_free(bignum);

    return pkey;
}

EVP_PKEY *vcmy_load_key(const char *szPath)
{
    BIO *in = BIO_new_file(szPath, "r");
    EVP_PKEY *pkey = PEM_read_bio_PrivateKey(in, NULL, NULL, NULL);
    BIO_free_all(in);
    return pkey;
}

int vcmy_save_x509_req(EVP_PKEY *pkey, const char *szPath)
{
    int                 ret = 0;
    const unsigned char *szCountry = (const unsigned char*)"CN";
    const unsigned char *szProvince = (const unsigned char*)"Guangdong";
    const unsigned char *szCity = (const unsigned char*)"Guangzhou";
    const unsigned char *szOrganization = (const unsigned char*)"Example";
    const unsigned char *szOrganizationUnit = (const unsigned char*)"App";
    const unsigned char *szCommonName = (const unsigned char*)"app.example.com";
    X509_REQ            *x509_req = NULL;
    X509_NAME           *x509_name = NULL;
    BIO                 *out = NULL;

    x509_req = X509_REQ_new();
    ret = X509_REQ_set_version(x509_req, 0);
    if (ret != 1) goto end;

    x509_name = X509_REQ_get_subject_name(x509_req);
    ret = X509_NAME_add_entry_by_txt(x509_name, "C",
            MBSTRING_ASC, szCountry, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_NAME_add_entry_by_txt(x509_name, "ST",
            MBSTRING_ASC, szProvince, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_NAME_add_entry_by_txt(x509_name, "L",
            MBSTRING_ASC, szCity, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_NAME_add_entry_by_txt(x509_name, "O",
            MBSTRING_ASC, szOrganization, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_NAME_add_entry_by_txt(x509_name, "OU",
            MBSTRING_ASC, szOrganizationUnit, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_NAME_add_entry_by_txt(x509_name, "CN",
            MBSTRING_ASC, szCommonName, -1, -1, 0);
    if (ret != 1) goto end;

    ret = X509_REQ_set_pubkey(x509_req, pkey);
    if (ret != 1) goto end;

    ret = X509_REQ_sign(x509_req, pkey, EVP_sha256());
    if (ret == 0) goto end;

    out = BIO_new_file(szPath, "w");
    ret = PEM_write_bio_X509_REQ(out, x509_req);
    if (ret != 1) goto end;

end:
    BIO_free_all(out);
    X509_REQ_free(x509_req);

    return ret;
}

X509 *vcmy_load_x509_cert(const char *szPath)
{
    BIO *in = BIO_new_file(szPath, "r");
    X509 *x509_cert = PEM_read_bio_X509(in, NULL, 0, NULL);
    BIO_free_all(in);

    return x509_cert;
}

int vcmy_verify_x509_cert(X509 *ca_cert, X509 *client_cert,
        const int check_pubkey, const EVP_PKEY *pkey)
{
    int ret = 0;
    X509_STORE *store = NULL;
    X509_STORE_CTX *ctx = NULL;

    // check if client_cert signed by ca
    store = X509_STORE_new();
    ret = X509_STORE_add_cert(store, ca_cert);
    if (ret != 1) goto end;

    ctx = X509_STORE_CTX_new();
    ret = X509_STORE_CTX_init(ctx, store, client_cert, NULL);
    if (ret != 1) goto end;
    ret = X509_verify_cert(ctx);

    // check if public key in client_cert matches client's private key
    if ((ret == 1) && check_pubkey) {
        EVP_PKEY *client_pkey = X509_get0_pubkey(client_cert);
        ret = EVP_PKEY_cmp(pkey, client_pkey);
    }

    // check if client_cert not expired
    if (ret == 1) {
        time_t now = time(NULL);
        const ASN1_TIME *notAfter = X509_get0_notAfter(client_cert);
        ret = ASN1_UTCTIME_cmp_time_t(notAfter, now) == 1;
    }

end:
    X509_STORE_CTX_free(ctx);
    X509_STORE_free(store);

    return ret;
}
