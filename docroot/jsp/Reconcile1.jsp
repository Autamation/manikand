 <%@page import="com.liferay.counter.service.CounterLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletClassLoaderUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.Criterion"%>
<%@page import="com.liferay.portal.kernel.dao.orm.DynamicQuery"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.prakat.dashboard.application.service.WarehouseFileRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.WarehouseFileRegistration"%>
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

<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="https://code.jquery.com/jquery-3.3.1.js"></script>
  <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  
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
       
       .warehouseGreen {
	    display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #4CAF50;
	    background-color: #fff;
	    background-image: none;
	    border: 0px solid #ccc;
	}
	
	.warehouseGray {
	    display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #999;
	    background-color: #fff;
	    background-image: none;
	    border: 0px solid #ccc;
	}

       /* Gray */

	.panel-heading {
	    padding: 1px 15px;
	    border-bottom: 1px solid transparent;
	    border-top-left-radius: 3px;
	    border-top-right-radius: 3px;
	}
	
	/* https://loading.io/mod/spinner/balls/sample.gif */
	#loader {  
	    position: fixed;  
	    left: 0px;  
	    top: 0px;  
	    width: 100%;  
	    height: 100%;  
	    z-index: 9999;  
	    background: url('<%= request.getContextPath() %>/spinner-icon-gif.jpg') 50% 50% #fff;
	    background-repeat: no-repeat;
	    opacity: 0.5;
	}  
	
</style>

<liferay-ui:error key="file-extension-exception" message="Upload only .csv File" />
<liferay-ui:error key="file-data-exception" message="No Data in File." />
<liferay-ui:error key="file-exception" message="This is not a correct File.Please Check the file." />
<liferay-ui:error key="warehouseDate-exception" message="Already added warehouse file.Please Check the file." />
<liferay-ui:error key="warehouseCode-exception" message="This is not a correct file for the warehouse code.Please Check the file." />
<liferay-ui:error key="no-date" message="Please select valid date." />

<liferay-ui:success key="success" message="Your file is Uploaded Successfully." />


<div id="loader" class="hidden"></div>

<div class="container" style="margin-left: -96px;">
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				
				<portlet:actionURL var="uploadWarehousrCSVDataURL" name="uploadWarehousrCSVData"></portlet:actionURL>
				
				<form action="<%= uploadWarehousrCSVDataURL %>" method="post" enctype = "multipart/form-data">
					<div class="panel panel-default">
						<div class="panel-heading">
						    <h4><b>Warehouse Invoice Details</b></h4>
						</div>
						<div class="panel-body">
							<div class="col-sm-5">
								<div class="form-group">
									<div class="dropdwon_box">
		                                <select name="<portlet:namespace/>selectedData" id="select_warehouse" required id="select-option" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;">
		                                    <option value="" disabled selected>Select Warehouse</option>
		                                    <% List<WarehouseRegistration> warehouseDataList = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrations(0, WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrationsCount()); 
		                                    	for(WarehouseRegistration warehouseRegistrationObj : warehouseDataList){ 
		                                    		if(warehouseRegistrationObj.getStatus() == 1){
		                                    	%>
			                                    		<option value="<%= warehouseRegistrationObj.getWarehouseID() %>"><%= warehouseRegistrationObj.getWarehouseName() + " (" + warehouseRegistrationObj.getWarehouseCode() + ")" %></option>
		                                   		<% }
		                                    } %>
		                                </select>
	                                </div>
								</div>
							</div>
							<div class="col-sm-1"></div>
							<div class="col-sm-5">
								<label for="Upload_Invoice_Excel_File" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Upload Invoice Excel File:</label>
								<input type="file" class="custom-file-input" id="warehouse_file" name="<portlet:namespace/>orderWarehouseCsvFile" accept=".csv" required>
							</div>
							<div class="col-sm-1"></div>
						</div>
						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-12">
									<div class="col-sm-8"></div>
									<div class="col-sm-4" style="margin-left:704px;">
										<button type="submit" id="warehrs_btn"  class="btn btn-md btn-primary" onclick="<%= uploadWarehousrCSVDataURL %>"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Upload</button>
										<button type="reset" class="btn btn-md btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;Reset</button>
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
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<portlet:actionURL var="orderCsvFileUploadURL" name="orderCsvFileUpload"></portlet:actionURL>
                <form id="order_details" action = "<%= orderCsvFileUploadURL %>" method = "post" enctype = "multipart/form-data" >
					<div class="panel panel-default">
						<div class="panel-heading">
						    <h4><b>Order Details</b></h4>
						</div>
						<div class="panel-body">
							<div class="col-sm-5">
								<div class="form-group">
									<label for="From_Date" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">From Date:</label>
										
										<aui:input type="date" name="fromDate" label="" id="dateStartOrder" onChange="startDateValidateOrder()" placeholder="Select From date" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" />
									<br><br>
	                                <div class="custom-file">
		                                <label for="Upload_Invoice_Excel_File" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Upload Order CSV File:</label>
										<input type="file" class="custom-file-input" name="<portlet:namespace/>orderCsvFile" id="<portlet:namespace/>orderCsvFile" accept=".csv" required>
									</div>
								</div>
							</div>
							<div class="col-sm-1"></div>
							<div class="col-sm-5">
								<div class="form-group">
									<label for="To_Date" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">To Date:</label>
									<aui:input type="date" name="toDate" label="" id="dateToOrder" onChange="verifyDateOrder()" placeholder="Select To date" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" />
								</div>
							</div>
							<div class="col-sm-1"></div>
						</div>
						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-12">
									<div class="col-sm-8">
											
									</div>
									<div class="col-sm-4" style="margin-left:704px;">
										<button type="submit" id="ord_btn" class="btn btn-md btn-primary" onclick="<%= orderCsvFileUploadURL %>"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Upload</button>
										<button type="reset" class="btn btn-md btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;Reset</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-sm-1">
				
			</div>
		</div>
	</div>
</div> 


<script>

$(document).ready(function(){
	$('button[type="submit"]').click(function(e){
		var formName = $(this).attr('id');
		
		if(formName == "warehrs_btn"){
			var warehouse = $("#select_warehouse").val(), 
			warehouseFile = $("#warehouse_file").val();
			if((warehouseFile !== "")  && (warehouse !== null)){
				$("#loader").removeClass("hidden");
			}
		}
		
		if(formName == "ord_btn"){
			var ord_frm_date = $('#<portlet:namespace />dateStartOrder').val(), 
			ord_to_date = $('#<portlet:namespace />dateToOrder').val(), 
			ord_file = $('#<portlet:namespace/>orderCsvFile').val();
			if((ord_frm_date !== "")  && (ord_to_date !== "") && (ord_file !== "")){
				$("#loader").removeClass("hidden");
			}
			
		}

	});
});

</script>

