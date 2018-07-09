import os.path
from datetime import datetime

from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography import x509
from cryptography.x509.oid import NameOID


def _get_private_key(private_key_path):
    if os.path.isfile(private_key_path):
        with open(private_key_path, 'rb') as f:
            private_key = serialization.load_pem_private_key(f.read(),
                password=None, backend=default_backend())
    else:
        private_key = rsa.generate_private_key(public_exponent=65537,
            key_size=2048, backend=default_backend())
        with open(private_key_path, 'wb') as f:
            f.write(private_key.private_bytes(
                encoding=serialization.Encoding.PEM,
                format=serialization.PrivateFormat.TraditionalOpenSSL,
                encryption_algorithm=serialization.NoEncryption()))
    return private_key

def get_request_bytes(private_key_path):
    private_key = _get_private_key(private_key_path)
    client_request = x509.CertificateSigningRequestBuilder().subject_name(
        x509.Name([
            x509.NameAttribute(NameOID.COUNTRY_NAME, u'CN'),
            x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, u'Guangdong'),
            x509.NameAttribute(NameOID.LOCALITY_NAME, u'Guangzhou'),
            x509.NameAttribute(NameOID.ORGANIZATION_NAME, u'Example'),
            x509.NameAttribute(NameOID.ORGANIZATIONAL_UNIT_NAME, u'App'),
            x509.NameAttribute(NameOID.COMMON_NAME, u'app.example.com'),
        ])
    ).sign(private_key, hashes.SHA256(), default_backend())
    return client_request.public_bytes(serialization.Encoding.PEM)

def load_certificate(license_content):
    return x509.load_pem_x509_certificate(license_content, default_backend())

def verify_certificate(ca_cert, client_cert,
        check_pubkey=True, client_key_path=None):
    valid = True

    # check if client_cert signed by ca
    ca_pem = x509.load_pem_x509_certificate(ca_cert, default_backend())
    if ca_pem.subject == client_cert.issuer:
        verifier = ca_pem.public_key().verifier(
            client_cert.signature,
            padding.PKCS1v15(),
            client_cert.signature_hash_algorithm
        )
        verifier.update(client_cert.tbs_certificate_bytes)
        try:
            verifier.verify()
            valid = True
        except e:
            logger.warning(e)
            valid = False

    # check if public key in client_cert matches client's private key
    if valid and check_pubkey:
        with open(client_key_path, 'rb') as f:
            client_key = serialization.load_pem_private_key(f.read(),
                password=None, backend=default_backend())
        key_numbers = client_key.public_key().public_numbers()
        lic_numbers = client_cert.public_key().public_numbers()
        valid = key_numbers == lic_numbers

    # check if client_cert not expired
    if valid:
        now = datetime.now()
        valid = client_cert.not_valid_after > now

    return valid
