package com.vcmy.license;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

public class LicenseManager {
	private String basedir = null;
	private static final String CLIENT_KEYSTORE = "client.jks";
	private static final String CLIENT_ALIAS = "client";
	private static final char[] CLIENT_PASSWORD = "VcmyClient".toCharArray();
	private static final String CA_PEM = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDujCCAqKgAwIBAgIJANgmQv4T819uMA0GCSqGSIb3DQEBCwUAMHExCzAJBgNV\n"
			+ "BAYTAkNOMRIwEAYDVQQIDAlHdWFuZ2RvbmcxEjAQBgNVBAcMCUd1YW5nemhvdTEN\n"
			+ "MAsGA1UECgwEVkNNWTEQMA4GA1UECwwHTGljZW5zZTEZMBcGA1UEAwwQbGljZW5z\n"
			+ "ZS52Y215LmNvbTAgFw0xODA3MDkwMjAxNTNaGA8yMTE4MDYxNTAyMDE1M1owcTEL\n"
			+ "MAkGA1UEBhMCQ04xEjAQBgNVBAgMCUd1YW5nZG9uZzESMBAGA1UEBwwJR3Vhbmd6\n"
			+ "aG91MQ0wCwYDVQQKDARWQ01ZMRAwDgYDVQQLDAdMaWNlbnNlMRkwFwYDVQQDDBBs\n"
			+ "aWNlbnNlLnZjbXkuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA\n"
			+ "vSi0w0z2trC+L6V2bzw2GRossHTyKJ3BGHlySZzKJTF1J+UjfWxaN528zEaVJXuw\n"
			+ "LcScM8v900G0WGA78PV3g16/8IGi3oQG4dtanZyVBwgeucPCYRDQcO1HF+FoXip2\n"
			+ "2G4mhhfyIH6G0pPZ8oKPkonCj0Bp/vX2ioxLzwu3aTigoTxIw7t3rIynLixYWh2x\n"
			+ "qHPtk/1b5OluH6IGcj2LG3e/io9+YjjZXvH09crV7A6Jv+qbYpr3cDQqcO0SFE27\n"
			+ "f+rF0wqQPnnz3a2skfYYA/K96kfq3uptJOUZH+ci2aeJZUCDzEYmuPJKLeNubGFI\n"
			+ "92ErBYhTHkOgIFqSSC+zHQIDAQABo1MwUTAdBgNVHQ4EFgQU0Kuiecq8SZdXKf6k\n"
			+ "dxdvuCmse6MwHwYDVR0jBBgwFoAU0Kuiecq8SZdXKf6kdxdvuCmse6MwDwYDVR0T\n"
			+ "AQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAs3673utUXv9btkOPg0WZ3TIu\n"
			+ "nTfuL/fli/JH8GDmIGHbBfk7DZ+6ouNKVEroPUOtRll1c2bKCE4LcokVG4s1AYvG\n"
			+ "JuaIIbZtvzoYirmLiAf8p+exHNIiOUsadr4RCjJOsFk3aGeZQySFL6CNUrzhFDnL\n"
			+ "jjibbYMgCDwVTYd+/HORfwD073wkuL+YxoQZpPWa8DMBD9S3ZmAr/hV5aqGXbu9D\n"
			+ "l5y1kupmr5d1SMQtXWEHrMmhX+H88kUH36g8Wd/wcNM8N0i8NbF+nUr1VxJC5UBt\n"
			+ "uP/lCrvr3hLgW02pECDdJ4mdxe3xFyuPz8kL+ysxxT6yc/LoZQmtIj8GaOXD9g==\n"
			+ "-----END CERTIFICATE-----";

	public LicenseManager(String basedir) {
		super();
		this.basedir = basedir;
	}

	private static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048, new SecureRandom());
		return generator.generateKeyPair();
	}

	private static Certificate generateCertificate(KeyPair keyPair) throws Exception {
		X500Name issuer = new X500Name("CN=app.vcmy.com,OU=App,O=VCMY,L=Guangzhou,ST=Guangdong,C=CN");
		X500Name subject = issuer;
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
		Date notBefore = new Date();
		Date notAfter = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 365L * 20L);
		X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serialNumber, notBefore, notAfter,
				subject, SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = builder.build(keyPair.getPrivate());

		byte[] certBytes = certBuilder.build(signer).getEncoded();
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		return (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	private static X509Certificate loadCACertificate() throws Exception {
		PemReader reader = new PemReader(new StringReader(CA_PEM));
		PemObject obj = reader.readPemObject();
		reader.close();
		ByteArrayInputStream is = new ByteArrayInputStream(obj.getContent());
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		return (X509Certificate) fact.generateCertificate(is);
	}

	public static X509Certificate loadPemX509Certificate(byte[] license_content) throws Exception {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		ByteArrayInputStream is = new ByteArrayInputStream(license_content);
		return (X509Certificate) fact.generateCertificate(is);
	}

	private KeyPair getKeyPair() throws Exception {
		KeyPair keyPair = null;
		File file = new File(this.basedir, CLIENT_KEYSTORE);
		if (file.exists()) {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			PasswordProtection keyPassword = new PasswordProtection(CLIENT_PASSWORD);

			InputStream inputStream = new FileInputStream(file);
			keyStore.load(inputStream, CLIENT_PASSWORD);
			inputStream.close();

			PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) keyStore.getEntry(CLIENT_ALIAS, keyPassword);
			Certificate cert = keyStore.getCertificate(CLIENT_ALIAS);
			PublicKey publicKey = cert.getPublicKey();
			PrivateKey privateKey = privateKeyEntry.getPrivateKey();
			keyPair = new KeyPair(publicKey, privateKey);
		} else {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			PasswordProtection keyPassword = new PasswordProtection(CLIENT_PASSWORD);
			keyPair = generateKeyPair();
			Certificate cert = generateCertificate(keyPair);
			PrivateKeyEntry skEntry = new PrivateKeyEntry(keyPair.getPrivate(), new Certificate[] { cert });
			keyStore.load(null);
			keyStore.setEntry(CLIENT_ALIAS, skEntry, keyPassword);
			OutputStream outputStream = new FileOutputStream(file);
			keyStore.store(outputStream, CLIENT_PASSWORD);
			outputStream.close();
		}
		return keyPair;
	}

	public ByteArrayOutputStream getRequest() throws Exception {
		KeyPair keyPair = this.getKeyPair();
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal("CN=app.example.com,OU=App,O=Example,L=Guangzhou,ST=Guangdong,C=CN"),
				keyPair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = csBuilder.build(keyPair.getPrivate());
		PKCS10CertificationRequest csr = p10Builder.build(signer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream));
		pemWriter.writeObject(new PemObject("CERTIFICATE REQUEST", csr.getEncoded()));
		pemWriter.close();
		return outputStream;
	}

	public boolean verify(X509Certificate clientCert, boolean checkPubkey) throws Exception {
		boolean valid = true;

		// check if client_cert signed by ca
		X509Certificate caCert = loadCACertificate();
		if (caCert.getSubjectX500Principal().equals(clientCert.getIssuerX500Principal())) {
			try {
				clientCert.verify(caCert.getPublicKey());
				valid = true;
			} catch (Exception e) {
				valid = false;
			}
		} else {
			valid = false;
		}

		// check if public key in client_cert matches client's private key
		if (valid && checkPubkey) {
			KeyPair keyPair = this.getKeyPair();
			valid = keyPair.getPublic().equals(clientCert.getPublicKey());
		}

		// check if client_cert not expired
		if (valid) {
			Date now = new Date();
			valid = clientCert.getNotAfter().compareTo(now) > 0;
		}

		return valid;
	}

	public boolean verify(byte[] licenseContent, boolean checkPubkey) throws Exception {
		X509Certificate clientCert = loadPemX509Certificate(licenseContent);
		return this.verify(clientCert, checkPubkey);
	}

	public boolean verify(String licenseContent, boolean checkPubkey) throws Exception {
		return this.verify(licenseContent.getBytes(), checkPubkey);
	}
}
