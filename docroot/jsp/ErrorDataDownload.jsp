<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.WarehouseRegistration"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
   
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<liferay-theme:defineObjects />

<portlet:defineObjects />
<style>
       body {
           margin-top: 150px;
       }

       h2 {
           color: #337ab7;
       }

       .nav>li>a:focus,
       .nav>li>a:hover {
           text-decoration: none;
           background-color: #bf221d;
       }

       thead {
           background-color: #337ab7;
           color: white;
       }

       .btn-lg {
           background-color: #337ab7;
           color: white;
       }

       .dropbtn {
           background-color: #337ab7;
           color: white;
           padding: 16px;
           font-size: 16px;
           border: none;
       }

       .dropdown {
           position: relative;
           display: inline-block;
       }

       .dropdown-content {
           display: none;
           position: absolute;
           background-color: #f1f1f1;
           min-width: 160px;
           box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
           z-index: 1;
       }

       .dropdown-content a {
           color: black;
           padding: 12px 16px;
           text-decoration: none;
           display: block;
       }

       .dropdown-content a:hover {
           background-color: #337ab7;
           color: #ffffff;
       }

       .dropdown:hover .dropdown-content {
           display: block;
       }

       .dropdown:hover .dropbtn {
           background-color: #337ab7;
       }


       .label {
          
           padding: 8px;
           font-family: Arial;
       }

       .success {
          color: #4CAF50;
       }

       /* Green */
       .other {
          color: Gray;
           
       }

       /* Gray */

	.panel-heading {
	    padding: 1px 15px;
	    border-bottom: 1px solid transparent;
	    border-top-left-radius: 3px;
	    border-top-right-radius: 3px;
	}
</style>

<%
String errorDataValue = renderRequest.getParameter("ErrorDataValue");
String ErrorDataFileName = renderRequest.getParameter("ErrorDataFileName");
System.out.println("ErrorDataFileName  :: "+ErrorDataFileName);
%>
	
<liferay-ui:success key="success" message="File Downloaded SuccessFully"/>
	
<portlet:resourceURL var="downloadURL">
	<portlet:param name="ServerResourceReconcile" value="ErrorDataFileName"/>
	<portlet:param name="ErrorDataFileName" value="<%= ErrorDataFileName %>"/>
</portlet:resourceURL>

<div class="container" style="margin-left: -96px;">
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="col-sm-6">
							<div class="form-group">
								<h5>Found error in uploaded file. Please Download the file, please correct it and upload it.</h5>
								<a href="<%= downloadURL %>">Click Here for Download</a>
							</div>
						</div>
						<div class="col-sm-6">
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-1"></div>
		</div>
	</div>
</div>