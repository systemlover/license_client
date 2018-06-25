package com.vcmy.license.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.vcmy.license.LicenseManager;

@WebServlet("/licenses/add")
@MultipartConfig
public class AddLicense extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/license.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		File file = new File(getClass().getClassLoader().getResource(".").getPath(), filename);
//		InputStream is = new FileInputStream(file);
		Part filePart = request.getPart("license-file");
//		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		InputStream fileContent = filePart.getInputStream();
		byte[] licenseContent = new byte[(int)filePart.getSize()];
		fileContent.read(licenseContent);
		fileContent.close();
		try {
			boolean licenseValid = LicenseManager.verify(licenseContent);
			System.out.println(licenseValid);
			if (licenseValid) {
				// TODO: persist license name, content into database
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("Added");
	}
}
