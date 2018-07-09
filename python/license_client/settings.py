"""
Django settings for license_client project.

Generated by 'django-admin startproject' using Django 2.0.6.

For more information on this file, see
https://docs.djangoproject.com/en/2.0/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/2.0/ref/settings/
"""

import os

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/2.0/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'f8sz5my!m8$s_^(4&k%$(_nf@*@y-zb__8@8=aapj+to0+as4#'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ['*']


# Application definition

INSTALLED_APPS = [
    'licenses.apps.LicensesConfig',
    'vcmy_license.apps.VcmyLicenseConfig',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'license_client.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [os.path.join(BASE_DIR, 'templates')],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'license_client.wsgi.application'


# Database
# https://docs.djangoproject.com/en/2.0/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}


# Password validation
# https://docs.djangoproject.com/en/2.0/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]


# Internationalization
# https://docs.djangoproject.com/en/2.0/topics/i18n/

LANGUAGE_CODE = 'zh-hans'

TIME_ZONE = 'Asia/Shanghai'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/2.0/howto/static-files/

STATIC_URL = '/static/'

STATICFILES_DIRS = [
    os.path.join(BASE_DIR, 'assets'),
    os.path.join(BASE_DIR, 'node_modules'),
]


CA_PEM = b'''-----BEGIN CERTIFICATE-----
MIIDujCCAqKgAwIBAgIJANgmQv4T819uMA0GCSqGSIb3DQEBCwUAMHExCzAJBgNV
BAYTAkNOMRIwEAYDVQQIDAlHdWFuZ2RvbmcxEjAQBgNVBAcMCUd1YW5nemhvdTEN
MAsGA1UECgwEVkNNWTEQMA4GA1UECwwHTGljZW5zZTEZMBcGA1UEAwwQbGljZW5z
ZS52Y215LmNvbTAgFw0xODA3MDkwMjAxNTNaGA8yMTE4MDYxNTAyMDE1M1owcTEL
MAkGA1UEBhMCQ04xEjAQBgNVBAgMCUd1YW5nZG9uZzESMBAGA1UEBwwJR3Vhbmd6
aG91MQ0wCwYDVQQKDARWQ01ZMRAwDgYDVQQLDAdMaWNlbnNlMRkwFwYDVQQDDBBs
aWNlbnNlLnZjbXkuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA
vSi0w0z2trC+L6V2bzw2GRossHTyKJ3BGHlySZzKJTF1J+UjfWxaN528zEaVJXuw
LcScM8v900G0WGA78PV3g16/8IGi3oQG4dtanZyVBwgeucPCYRDQcO1HF+FoXip2
2G4mhhfyIH6G0pPZ8oKPkonCj0Bp/vX2ioxLzwu3aTigoTxIw7t3rIynLixYWh2x
qHPtk/1b5OluH6IGcj2LG3e/io9+YjjZXvH09crV7A6Jv+qbYpr3cDQqcO0SFE27
f+rF0wqQPnnz3a2skfYYA/K96kfq3uptJOUZH+ci2aeJZUCDzEYmuPJKLeNubGFI
92ErBYhTHkOgIFqSSC+zHQIDAQABo1MwUTAdBgNVHQ4EFgQU0Kuiecq8SZdXKf6k
dxdvuCmse6MwHwYDVR0jBBgwFoAU0Kuiecq8SZdXKf6kdxdvuCmse6MwDwYDVR0T
AQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAs3673utUXv9btkOPg0WZ3TIu
nTfuL/fli/JH8GDmIGHbBfk7DZ+6ouNKVEroPUOtRll1c2bKCE4LcokVG4s1AYvG
JuaIIbZtvzoYirmLiAf8p+exHNIiOUsadr4RCjJOsFk3aGeZQySFL6CNUrzhFDnL
jjibbYMgCDwVTYd+/HORfwD073wkuL+YxoQZpPWa8DMBD9S3ZmAr/hV5aqGXbu9D
l5y1kupmr5d1SMQtXWEHrMmhX+H88kUH36g8Wd/wcNM8N0i8NbF+nUr1VxJC5UBt
uP/lCrvr3hLgW02pECDdJ4mdxe3xFyuPz8kL+ysxxT6yc/LoZQmtIj8GaOXD9g==
-----END CERTIFICATE-----
'''

CLIENT_KEY_PATH = os.path.join(BASE_DIR, 'client.key')
CLIENT_REQUEST_PATH = os.path.join(BASE_DIR, 'client.req')
CLIENT_LICENSE_PATH = os.path.join(BASE_DIR, 'client.lic')
