import logging
import os.path
import pytz

from django.conf import settings
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.views import View
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography import x509
from cryptography.x509.oid import NameOID

from .models import License


logger = logging.getLogger(__name__)

class IndexView(View):
    template_name = 'dashboard.html'

    def get(self, request, *args, **kwargs):
        return render(request, self.template_name)


class LicenseView(View):
    template_name = 'licenses.html'

    def get(self, request, *args, **kwargs):
        context = {
            'licenses': License.objects.filter(deleted=False),
        }
        return render(request, self.template_name, context)

    def post(self, request, *args, **kwargs):
        pass


class LicenseRequestView(View):
    def get_client_key(self):
        if os.path.isfile(settings.CLIENT_KEY_PATH):
            with open(settings.CLIENT_KEY_PATH, 'rb') as f:
                client_key = serialization.load_pem_private_key(f.read(),
                    password=None, backend=default_backend())
        else:
            client_key = rsa.generate_private_key(public_exponent=65537,
                key_size=2048, backend=default_backend())
            with open(settings.CLIENT_KEY_PATH, 'wb') as f:
                f.write(client_key.private_bytes(
                    encoding=serialization.Encoding.PEM,
                    format=serialization.PrivateFormat.TraditionalOpenSSL,
                    encryption_algorithm=serialization.NoEncryption()))
        return client_key

    def init_client_request(self):
        if not os.path.isfile(settings.CLIENT_REQUEST_PATH):
            client_key = self.get_client_key()
            client_request = x509.CertificateSigningRequestBuilder().subject_name(x509.Name([
                x509.NameAttribute(NameOID.COUNTRY_NAME, u'CN'),
                x509.NameAttribute(NameOID.STATE_OR_PROVINCE_NAME, u'Guangdong'),
                x509.NameAttribute(NameOID.LOCALITY_NAME, u'Guangzhou'),
                x509.NameAttribute(NameOID.ORGANIZATION_NAME, u'Example'),
                x509.NameAttribute(NameOID.ORGANIZATIONAL_UNIT_NAME, u'App'),
                x509.NameAttribute(NameOID.COMMON_NAME, u'app.example.com'),
            ])).sign(client_key, hashes.SHA256(), default_backend())
            with open(settings.CLIENT_REQUEST_PATH, 'wb') as f:
                f.write(client_request.public_bytes(serialization.Encoding.PEM))

    def get(self, request, *args, **kwargs):
        self.init_client_request()
        response = HttpResponse(content_type='application/octet-stream')
        response['Content-Disposition'] = 'attachment; filename="license.req"'
        with open(settings.CLIENT_REQUEST_PATH, 'rb') as f:
            response.write(f.read())
        return response


class LicenseDetailView(View):
    template_name = 'license.html'

    def get(self, request, *args, **kwargs):
        return render(request, self.template_name)

    def check_signature(self, client_license):
        ca_pem = x509.load_pem_x509_certificate(settings.CA_PEM, default_backend())
        if ca_pem.subject == client_license.issuer:
            verifier = ca_pem.public_key().verifier(
                client_license.signature,
                padding.PKCS1v15(),
                client_license.signature_hash_algorithm
            )
            verifier.update(client_license.tbs_certificate_bytes)
            try:
                verifier.verify()
                return True
            except e:
                logger.warning(e)
        return False

    def post(self, request, *args, **kwargs):
        license_file = request.FILES.get('license-file')
        license_content = license_file.read()
        try:
            client_license = x509.load_pem_x509_certificate(license_content, default_backend())
            if self.check_signature(client_license):
                License.objects.create(
                    valid_from=pytz.utc.localize(client_license.not_valid_before),
                    valid_till=pytz.utc.localize(client_license.not_valid_after),
                    file_name=license_file.name,
                    file_content=license_content)
                return redirect('licenses')
        except Exception as e:
            logger.warning(e)
        return redirect('add-license')
