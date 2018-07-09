package com.vcmy.license.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vcmy.license.LicenseManager;

@WebServlet("/licenses/request")
public class RequestLicense extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CLIENT_REQUEST = "client.req";


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename=" + CLIENT_REQUEST);
		
		String projectPath = getClass().getClassLoader().getResource(".").getPath();
		LicenseManager manager = new LicenseManager(projectPath);
		OutputStream out = response.getOutputStream();
		ByteArrayOutputStream outputStream;
		try {
			outputStream = manager.getRequest();
			outputStream.writeTo(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
	}

}
