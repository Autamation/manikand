<%@page
	import="com.dalrada.dashboard.application.module.controller.DalradaDashboardConstant"%>
<%@page
	import="com.prakat.dashboard.application.service.RoleRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.RoleRegistration"%>
<%@page import="com.liferay.portal.service.RoleLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
<script src="<%=request.getContextPath()%>/js/dataTables.responsive.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>

<portlet:defineObjects />

<style>
body {
	margin-top: 150px;
}

h2 {
	color: #337ab7;
}

.nav>li>a:focus, thead {
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

.panel-heading {
	padding: 1px 15px;
	border-bottom: 1px solid transparent;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
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

table th:nth-child(3), td:nth-child(3) {
	display: none;
}

th {
	padding: 9px;
	border: 1px solid #ddd;
}

.dataTables_wrapper {
	position: relative;
	clear: both;
	margin-top: 19px;
	zoom: 1;
}
</style>

<script type="text/javascript">

$(document).ready(function() {
    $('#roleDatatable').DataTable( {
        responsive: true
    } );
} );

</script>

<liferay-ui:success key="success-key" message="Added Successfully." />
<liferay-ui:error key="error-key" message="Already Registered." />
<liferay-ui:success key="edit-success-key"
	message="Updated Successfully." />
<liferay-ui:error key="edit-error-key" message="Already Registered." />
<liferay-ui:error key="already-error-key"
	message="Already Assign to user." />

<div class="container" style="margin-left: -96px;">
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<portlet:actionURL var="roleRegistration" name="roleRegistration"></portlet:actionURL>
				<form action="<%= roleRegistration %>" method="post">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4>
								<b>Create Role</b>
							</h4>
						</div>


						<div class="panel-body">
							<div class="col-sm-5">
								<div class="form-group">
									<label for="full_name"
										style="display: inline-block; max-width: 100%; margin-bottom: 5px; font-weight: 700;">Role
										Name:</label> <input maxlength="35" type="text" pattern="[A-Za-z ]+$"
										title="Format: Letters only"
										style="display: block; width: 100%; height: 34px; padding: 6px 12px; font-size: 14px; line-height: 1.42857143; color: #555; background-color: #fff; background-image: none; border: 1px solid #ccc; border-radius: 4px; box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075); transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;"
										id="roleName" placeholder="e.g. Manager, Executive "
										name="<portlet:namespace/>roleName" required>
								</div>
							</div>
							<div class="col-sm-1"></div>
							<div class="col-sm-5"></div>
							<div class="col-sm-1"></div>

						</div>


						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-12">
									<div class="col-sm-8"></div>
									<div class="col-sm-4" style="margin-left: 704px;">

										<button type="submit" onclick="<%= roleRegistration %>"
											class="btn btn-md btn-primary">
											<span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Create
										</button>
										<button type="reset" class="btn btn-md btn-danger">
											<span class="	glyphicon glyphicon-remove"></span>&nbsp;Reset
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-sm-1"></div>
		</div>
	</div>
	<br />
	<br />
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-sm-12">
								<div class="col-sm-10">
									<h4>
										<b>Manage Roles</b>
									</h4>
								</div>
								<div class="col-sm-2"></div>
							</div>
						</div>
					</div>
					<table id="roleDatatable" class="display" cellspacing="0"
						width="97%"
						style="margin-left: 23px; margin-top: 26px; margin-bottom: 23px;">
						<thead>
							<tr>
								<th>Role Name</th>
								<th>Status</th>
								<th>Action1</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<% 
         List<RoleRegistration> listRoles=RoleRegistrationLocalServiceUtil.getRoleRegistrations(0, RoleRegistrationLocalServiceUtil.getRoleRegistrationsCount());
for (RoleRegistration roleDetails : listRoles) {
%>

							<portlet:actionURL var="ActiveInactiveURL"
								name="activeInactiveRole">
								<portlet:param name="roleID"
									value="<%= String.valueOf(roleDetails.getRoleID()) %>" />
							</portlet:actionURL>

							<portlet:renderURL var="EditRoleURL" windowState="normal">
								<portlet:param name="roleId"
									value="<%= String.valueOf(roleDetails.getRoleID()) %>" />
								<portlet:param name="jspPage" value="/jsp/Edit/EditRole.jsp" />
							</portlet:renderURL>

							<tr>
								<td><%= roleDetails.getRoleName() %></td>

								<% if(roleDetails.getStatus() == DalradaDashboardConstant.ROLE_STATUS_ACTIVE){ %>
								<td><b><a href="<%= ActiveInactiveURL %>"><%= DalradaDashboardConstant.ROLE_STATUS_INACTIVE_TEXT %></a></b></td>
								<% } %>
								<% if(roleDetails.getStatus() == DalradaDashboardConstant.ROLE_STATUS_INACTIVE){ %>
								<td><b><a href="<%= ActiveInactiveURL %>"><%= DalradaDashboardConstant.ROLE_STATUS_ACTIVE_TEXT %></a></b></td>
								<% } %>
								<td></td>
								<td><a href="<%= EditRoleURL %>"> <span
										class="glyphicon glyphicon-pencil"></span>
								</a></td>
							</tr>
							<%
}
%>

						</tbody>
					</table>
					<div class="panel-footer"></div>
				</div>
			</div>
			<div class="col-sm-1"></div>
		</div>
	</div>
</div>