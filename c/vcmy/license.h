#ifndef VCMY_LICENSE_H
#define VCMY_LICENSE_H

#ifdef  __cplusplus
extern "C" {
#endif

/*
 * Check if the file exists
 */
extern int vcmy_exists(const char *filename);

/*
 * Generate RSA private key
 */
extern EVP_PKEY *vcmy_generate_key(const char *szPath);

/*
 * Load RSA private key from a file path
 */
extern EVP_PKEY *vcmy_load_key(const char *szPath);

/*
 * Save X509 request file to disk
 */
extern int vcmy_save_x509_req(EVP_PKEY *pkey, const char *szPath);

/*
 * Load X509 certificate from a file path
 */
extern X509 *vcmy_load_x509_cert(const char *szPath);

/*
 * Verify X509 certificate
 */
extern int vcmy_verify_x509_cert(X509 *ca_cert, X509 *client_cert,
        const int check_pubkey, const EVP_PKEY *pkey);

#ifdef  __cplusplus
}
#endif

#endif
