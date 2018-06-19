from django.db import models


class License(models.Model):
    valid_from = models.DateTimeField()
    valid_till = models.DateTimeField()
    file_name = models.CharField(max_length=255)
    file_content = models.CharField(max_length=4096)
    imported_at = models.DateTimeField(auto_now_add=True)
    deleted = models.BooleanField(default=False)
