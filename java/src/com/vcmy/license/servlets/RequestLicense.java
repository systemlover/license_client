package com.vcmy.license.servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;

import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import com.vcmy.license.LicenseManager;

@WebServlet("/licenses/request")
public class RequestLicense extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CLIENT_REQUEST = "license.req";
	
	protected void initClientRequest(File file) throws Exception {
		if (!file.exists()) {
			KeyPair keyPair = LicenseManager.getKeyPair();
			PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal("CN=app.example.com,OU=App,O=Example,L=Guangzhou,ST=Guangdong,C=CN"),
				keyPair.getPublic());
			JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
			ContentSigner signer = csBuilder.build(keyPair.getPrivate());
			PKCS10CertificationRequest csr = p10Builder.build(signer);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream))) {
			    pemWriter.writeObject(new PemObject("CERTIFICATE REQUEST", csr.getEncoded()));
			} catch (IOException e) {
			    throw new RuntimeException(e);
			}
			
			FileOutputStream os = new FileOutputStream(file);
			os.write(outputStream.toByteArray());
			os.close();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename=\"license.req\"");
		
		File file = new File(getClass().getClassLoader().getResource(".").getPath(), CLIENT_REQUEST);
		try {
			initClientRequest(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0){
		    out.write(buffer, 0, length);
		}
		in.close();
		out.flush();
	}

}
