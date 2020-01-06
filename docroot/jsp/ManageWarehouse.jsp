<%@page
	import="com.dalrada.dashboard.application.module.controller.DalradaDashboardConstant"%>
<%@page
	import="com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil"%>
<%@page
	import="com.prakat.dashboard.application.model.WarehouseRegistration"%>
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

<liferay-ui:success key="success-key" message="Added Successfully." />
<liferay-ui:error key="error-key" message="Already Registered." />

<liferay-ui:success key="edit-success-key"
	message="Updated Successfully." />
<liferay-ui:error key="edit-error-key" message="Already Registered." />


<style>
body {
	margin-top: 100px;
}

h2 {
	color: #337ab7;
}

.nav>li>a:focus, .nav>li>a:hover {
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

.panel-heading {
	padding: 1px 15px;
	border-bottom: 1px solid transparent;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
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
    $('#exampleWarehouse').DataTable( {
        responsive: true
    } );
} );
</script>

<script type="text/javascript">
 
 $(document).on('input','#warehouseCode',function(){
	    var warehouseCode = $('#warehouseCode').val();
	   if(warehouseCode.indexOf('0')==0){
	     alert('First digit should not be 0.lease Enter valide Code.');
	     $('#warehouseCode').val('');
	   }
	});
 
    </script>

<div class="container" style="margin-left: -96px;">
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<portlet:actionURL var="warehouseRegistration"
					name="warehouseRegistration"></portlet:actionURL>
				<form action="<%= warehouseRegistration %>" method="post">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4>
								<b>Create Warehouse</b>
							</h4>
						</div>
						<div class="panel-body">
							<div class="col-sm-5">
								<div class="form-group">
									<label for="Warehouse_Name"
										style="display: inline-block; max-width: 100%; margin-bottom: 5px; font-weight: 700;">Warehouse
										Name:</label> <input type="text"
										title="Format: Letter, Digit ,Length(2-35)"
										style="display: block; width: 100%; height: 34px; padding: 6px 12px; font-size: 14px; line-height: 1.42857143; color: #555; background-color: #fff; background-image: none; border: 1px solid #ccc; border-radius: 4px; box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075); transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;"
										id="full_name" placeholder="e.g. Warehouse1"
										name="<portlet:namespace/>warehouseName"
										pattern="^[a-zA-Z][a-zA-Z0-9-_ \.]{1,35}$" required>
								</div>
								<div class="form-group">
									<label for="Warehouse_Code"
										style="display: inline-block; max-width: 100%; margin-bottom: 5px; font-weight: 700;">Warehouse
										Code:</label> <input maxlength="10" minlength="5" type="text"
										title="Format: Numeric Only, Length(5-10)" pattern="^[0-9]*$"
										style="display: block; width: 100%; height: 34px; padding: 6px 12px; font-size: 14px; line-height: 1.42857143; color: #555; background-color: #fff; background-image: none; border: 1px solid #ccc; border-radius: 4px; box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075); transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;"
										id="warehouseCode" placeholder="e.g. 12345"
										name="<portlet:namespace/>warehouseCode" onblur="" required>
									</textarea>
								</div>



							</div>
							<div class="col-sm-1"></div>
							<div class="col-sm-5">
								<div class="form-group">
									<label for="Warehouse_Address"
										style="display: inline-block; max-width: 100%; margin-bottom: 5px; font-weight: 700;">Warehouse
										Address:</label>
									<%-- <textarea type="text" style="display: block; width: 100%; height: 94px; padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name" placeholder="Enter Warehouse Address" name="<portlet:namespace/>warehouseAddress" onblur="validateEmail(this);" required> --%>
									<textarea maxlength="70" required rows="4" cols="50"
										name="<portlet:namespace/>warehouseAddress"
										style="display: block; width: 100%; height: 111px; padding: 6px 12px; font-size: 14px; line-height: 1.42857143; color: #555; background-color: #fff; background-image: none; border: 1px solid #ccc; border-radius: 4px; box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075); transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;"
										id="full_name"
										placeholder="e.g. 247, White Town, San Diego, CA, USA"></textarea>
								</div>
							</div>

							<div class="col-sm-1"></div>

						</div>


						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-12">
									<div class="col-sm-8"></div>
									<div class="col-sm-4" style="margin-left: 704px;">

										<button type="submit" class="btn btn-md btn-primary">
											<span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Create
										</button>
										<button type="reset" class="btn btn-md btn-danger">
											<span class="glyphicon glyphicon-remove"></span>&nbsp;Reset
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
	<!--row 2 close-->
	<br />
	<br />

	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">

				<div class="panel panel-default">
					<div class="panel-heading">
						<h4>
							<b>Manage Warehouses</b>
						</h4>
					</div>

					<table id="exampleWarehouse" class="display" cellspacing="0"
						width="97%"
						style="margin-left: 23px; margin-top: 26px; margin-bottom: 23px;">
						<thead>
							<tr>
								<th>Warehouse Name</th>
								<th>Warehouse Code</th>
								<th>Action1</th>
								<th>Warehouse Address</th>
								<th>Status</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<% 
                                        List<WarehouseRegistration> listWarehouses=WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrations(0, WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrationsCount());
                            			for (WarehouseRegistration warehouseDetails : listWarehouses) {
                            			%>

							<portlet:actionURL var="ActiveInactiveWarehouseURL"
								name="ActiveInactiveWarehouse">
								<portlet:param name="warehouseId"
									value="<%= String.valueOf(warehouseDetails.getWarehouseID()) %>" />
							</portlet:actionURL>

							<portlet:renderURL var="EditWarehouseURL" windowState="normal">
								<portlet:param name="warehouseId"
									value="<%= String.valueOf(warehouseDetails.getWarehouseID()) %>" />
								<portlet:param name="jspPage"
									value="/jsp/Edit/EditWarehouse.jsp" />
							</portlet:renderURL>

							<tr>
								<td><%= warehouseDetails.getWarehouseName() %></td>
								<td><%= warehouseDetails.getWarehouseCode() %></td>
								<td></td>
								<td><%= warehouseDetails.getWarehouseAddress() %></td>
								<% if(warehouseDetails.getStatus() == DalradaDashboardConstant.WAREHOUSE_STATUS_ACTIVE){ %>
								<td><b><a href="<%= ActiveInactiveWarehouseURL %>"><%= DalradaDashboardConstant.WAREHOUSE_STATUS_INACTIVE_TEXT %></a></b></td>
								<% } %>
								<% if(warehouseDetails.getStatus() == DalradaDashboardConstant.WAREHOUSE_STATUS_INACTIVE){ %>
								<td><b><a href="<%= ActiveInactiveWarehouseURL %>"><%= DalradaDashboardConstant.WAREHOUSE_STATUS_ACTIVE_TEXT %></a></b></td>
								<% } %>

								<td><a href="<%= EditWarehouseURL %>"> <span
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
