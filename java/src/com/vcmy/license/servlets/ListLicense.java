package com.vcmy.license.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vcmy.license.models.License;

@WebServlet("/licenses")
public class ListLicense extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<License> licenses = new ArrayList<License>();
		licenses.add(new License(1, "test.lic", new Date(), new Date(), new Date()));
		
		request.setAttribute("licenses", licenses);
		request.getRequestDispatcher("/WEB-INF/licenses.jsp").forward(request, response);
	}

}
