package com.vcmy.license.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.vcmy.license.License;
import com.vcmy.license.LicenseManager;

@WebServlet("/licenses/add")
@MultipartConfig
public class AddLicense extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/license.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part licenseFile = request.getPart("license-file");
		InputStream inputStream = licenseFile.getInputStream();
		byte[] licenseContent = new byte[(int)licenseFile.getSize()];
		inputStream.read(licenseContent);
		inputStream.close();
		
		try {
			String projectPath = getClass().getClassLoader().getResource(".").getPath();
			LicenseManager manager = new LicenseManager(projectPath);
			boolean valid = manager.verify(licenseContent, true);
			if (valid) {
				X509Certificate client_cert = LicenseManager.loadPemX509Certificate(licenseContent);
				String content = new String(licenseContent);
				Date importedAt = new Date();
				License license = new License(licenseFile.getName(), content,
						client_cert.getNotBefore(), client_cert.getNotAfter(), importedAt, false); 
				// TODO: persist license object into database
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("Added");
	}
}
