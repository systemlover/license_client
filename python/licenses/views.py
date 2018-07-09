import logging
import pytz

from django.conf import settings
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.views import View

from vcmy_license.models import License
from vcmy_license.utils import (get_request_bytes,
        load_certificate, verify_certificate)


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

    def get(self, request, *args, **kwargs):
        request_bytes = get_request_bytes(settings.CLIENT_KEY_PATH)
        response = HttpResponse(content_type='application/octet-stream')
        response['Content-Disposition'] = 'attachment; filename="client.req"'
        with open(settings.CLIENT_REQUEST_PATH, 'rb') as f:
            response.write(request_bytes)
        return response


class LicenseDetailView(View):
    template_name = 'license.html'

    def get(self, request, *args, **kwargs):
        return render(request, self.template_name)

    def post(self, request, *args, **kwargs):
        license_file = request.FILES.get('license-file')
        license_content = license_file.read()
        try:
            client_license = load_certificate(license_content)
            if verify_certificate(settings.CA_PEM, client_license,
                    True, settings.CLIENT_KEY_PATH):
                License.objects.create(
                    not_before=pytz.utc.localize(client_license.not_valid_before),
                    not_after=pytz.utc.localize(client_license.not_valid_after),
                    file_name=license_file.name,
                    file_content=license_content)
                return redirect('licenses')
        except Exception as e:
            logger.warning(e)
        return redirect('add-license')
