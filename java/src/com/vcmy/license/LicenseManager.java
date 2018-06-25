package com.vcmy.license;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class LicenseManager extends SpringBootServletInitializer {
	private static final String CLIENT_KEYSTORE = "client.jks";
	private static final String CLIENT_ALIAS = "client";
	private static final char[] CLIENT_PASSWORD = "VcmyClient".toCharArray();
	private static final String CA_PEM = "-----BEGIN CERTIFICATE-----\n" + 
		"MIIDcDCCAligAwIBAgIUIltzNXdzNq9M+5Bib15b2lUnuw8wDQYJKoZIhvcNAQEL\n" + 
		"BQAwcTELMAkGA1UEBhMCQ04xEjAQBgNVBAgMCUd1YW5nZG9uZzESMBAGA1UEBwwJ\n" + 
		"R3Vhbmd6aG91MQ0wCwYDVQQKDARWQ01ZMRAwDgYDVQQLDAdMaWNlbnNlMRkwFwYD\n" + 
		"VQQDDBBsaWNlbnNlLnZjbXkuY29tMCAXDTE4MDYxNDA0NDkwNloYDzIxMTgwNTIx\n" + 
		"MDQ0OTA2WjBxMQswCQYDVQQGEwJDTjESMBAGA1UECAwJR3Vhbmdkb25nMRIwEAYD\n" + 
		"VQQHDAlHdWFuZ3pob3UxDTALBgNVBAoMBFZDTVkxEDAOBgNVBAsMB0xpY2Vuc2Ux\n" + 
		"GTAXBgNVBAMMEGxpY2Vuc2UudmNteS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IB\n" + 
		"DwAwggEKAoIBAQDKYMEZffiwr4++ui84HBF+TLkM0VtkYSOj3p6Rg2PBfLp7GvjD\n" + 
		"EDY70aTRJAYOcXPH4INRU4l7TzrxLJxqKkE/e2KzJpJeZLX6JKUmthyIyLv943Tc\n" + 
		"yu4gILxrU77NwHfLTXbB7ZpHzu5tDmilJhMZUTnTAyv1J52tNLnLLNA2Na5goftg\n" + 
		"O/e1dEsDFq+REslgtmJwGDgxayUD5aNcBiAaMmTDYA1w/OJboCNB3NqWODHaGBwn\n" + 
		"XWGpDFSOp1cZDQxffbIlGuQwJdEHOxCq+MwCtqBbthwGOkEpHkwN3VwEors7xvAR\n" + 
		"IctBbx9HzuMRmfOy/wRn0pm7C4bMgjol20CtAgMBAAEwDQYJKoZIhvcNAQELBQAD\n" + 
		"ggEBAMi/neu9lyamNR79VdyuIFnA80vWodNw74NxjEUvBIBbYX6M+fX99drq9VND\n" + 
		"Mi1/pIqRVHQBh316ME6cO00tWV1i/xyJ+nfwz3Fdpl532+H/ljEORoY65khoaDUL\n" + 
		"QXHOG2RDHXhIjO+OfbShgnFk9f05m6c8ZZp18hOo3yBmrUJHgAlznkHmwy44Pg7r\n" + 
		"lMZbGkGeb2F74L+3pyF73hrgfS2ttEXsxBaMv9RHUFKClhSI375AUfQ0t/xi7Apd\n" + 
		"ivsi74XILvOaqUOnARFbwlGBrL7RPhY7ZLI1gTWkZOakJEr7H5aRN+J/8EWCLsi0\n" + 
		"YwuOUtWoezdck4D45acPSJ2/cew=\n" + 
		"-----END CERTIFICATE-----";
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		try {
			initKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return builder.sources(LicenseManager.class);
    }
	
	private KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048, new SecureRandom());
		return generator.generateKeyPair();
	}
	
	private Certificate generateCertificate(KeyPair keyPair) throws Exception {
		X500Name issuer = new X500Name("CN=app.vcmy.com,OU=App,O=VCMY,L=Guangzhou,ST=Guangdong,C=CN");
		X500Name subject = issuer;
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
		Date notBefore = new Date();
		Date notAfter = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 365L * 10L);
		X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serialNumber, notBefore, notAfter, subject, SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = builder.build(keyPair.getPrivate());

		byte[] certBytes = certBuilder.build(signer).getEncoded();
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
		return certificate;
	}
	
	private void initKeyPair() throws Exception {
		String projectPath = getClass().getClassLoader().getResource(".").getPath();
		System.out.println(projectPath);
		File file = new File(projectPath, CLIENT_KEYSTORE);
		if (!file.exists()) {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			PasswordProtection keyPassword = new PasswordProtection(CLIENT_PASSWORD);
			KeyPair keyPair = generateKeyPair();
			Certificate cert = generateCertificate(keyPair);
			PrivateKeyEntry skEntry = new PrivateKeyEntry(keyPair.getPrivate(), new Certificate[] { cert });
			keyStore.load(null);
			keyStore.setEntry(CLIENT_ALIAS, skEntry, keyPassword);
			OutputStream outputStream = new FileOutputStream(file);
			keyStore.store(outputStream, CLIENT_PASSWORD);
			outputStream.close();
		}
	}
	
	private static X509Certificate loadCACertificate() throws Exception {
		PemReader reader = new PemReader(new StringReader(CA_PEM));
		PemObject obj = reader.readPemObject();
		reader.close();
		ByteArrayInputStream is = new ByteArrayInputStream(obj.getContent());
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		return (X509Certificate)fact.generateCertificate(is);
	}
	
	private static X509Certificate loadPemX509Certificate(byte[] license_content) throws Exception {
	    CertificateFactory fact = CertificateFactory.getInstance("X.509");
	    ByteArrayInputStream is = new ByteArrayInputStream(license_content);
	    X509Certificate certificate = (X509Certificate) fact.generateCertificate(is);
	    return certificate;
	}
	
	public static KeyPair getKeyPair() throws Exception {
		String projectPath = LicenseManager.class.getClassLoader().getResource(".").getPath();
		File file = new File(projectPath, CLIENT_KEYSTORE);
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		PasswordProtection keyPassword = new PasswordProtection(CLIENT_PASSWORD);
		
		InputStream inputStream = new FileInputStream(file);
		keyStore.load(inputStream, CLIENT_PASSWORD);
		inputStream.close();
		
	    PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry)keyStore.getEntry(CLIENT_ALIAS, keyPassword);
	    Certificate cert = keyStore.getCertificate(CLIENT_ALIAS);
	    PublicKey publicKey = cert.getPublicKey();
	    PrivateKey privateKey = privateKeyEntry.getPrivateKey();
	    return new KeyPair(publicKey, privateKey);
	}
	
	public static boolean verify(byte[] license_content) throws Exception {
		X509Certificate ca_pem = loadCACertificate();
		X509Certificate client_license = loadPemX509Certificate(license_content);
		
		if (ca_pem.getSubjectX500Principal().equals(client_license.getIssuerX500Principal())) {
			try {
				client_license.verify(ca_pem.getPublicKey());
				return true;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return false;
	}
}
