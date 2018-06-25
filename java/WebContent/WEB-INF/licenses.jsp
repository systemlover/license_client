<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

	<base href="/license_client/" />
	
    <link rel="stylesheet" href="node_modules/bootstrap/dist/css/bootstrap.min.css">
	<link href="assets/css/dashboard.css" rel="stylesheet">
	<link href="assets/css/dataTables.bootstrap4.min.css" rel="stylesheet">

    <title>Licenses</title>
  </head>
  <body>
    <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
	    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">VCMY License Client</a>
	</nav>
	
	<div class="container-fluid">
	    <div class="row">
	        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
	            <div class="sidebar-sticky">
	                <ul class="nav flex-column">
	                    <li class="nav-item">
						    <a class="nav-link" href=".">
						        <span data-feather="home"></span>
						        Dashboard
						    </a>
						</li>
						<li class="nav-item">
						    <a class="nav-link active" href="licenses">
						        <span data-feather="file-text"></span>
						        Licenses <span class="sr-only">(current)</span>
						    </a>
						</li>
	                </ul>
	            </div>
	        </nav>
	
	        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
	            <h1 class="mt-3">Licenses</h1>
				<div class="mb-2">
				    <a class="btn btn-primary" href="licenses/add" role="button">Add</a>
				</div>
				<div class="row">
				    <table class="table table-striped table-bordered datatable">
				        <thead>
				            <tr>
				                <th>ID</th>
				                <th>File Name</th>
				                <th>Valid from</th>
				                <th>Valid till</th>
				                <th>Imported at</th>
				            </tr>
				        </thead>
				        <tbody>
				        	<c:forEach items="${licenses}" var="license">
				            <tr>
				                <td>${license.id}</td>
				                <td>${license.file_name}</td>
				                <td>${license.valid_from}</td>
				                <td>${license.valid_till}</td>
				                <td>${license.imported_at}</td>
				            </tr>
				            </c:forEach>
				        </tbody>
				    </table>
				</div>
	        </main>
	    </div>
	</div>

    <script src="node_modules/jquery/dist/jquery.slim.min.js"></script>
    <script src="node_modules/popper.js/dist/umd/popper.min.js"></script>
    <script src="node_modules/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="node_modules/feather-icons/dist/feather.min.js"></script>

    <script src="assets/js/jquery.dataTables.min.js"></script>
	<script src="assets/js/dataTables.bootstrap4.min.js"></script>
	<script src="assets/js/common.js"></script>

    <script>
    $(document).ready( function () {
        feather.replace()
    });
    </script>
  </body>
</html>
