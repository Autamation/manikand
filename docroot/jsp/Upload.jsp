<%@page import="java.util.concurrent.TimeUnit"%>
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
<script src="//code.jquery.com/jquery.min.js"></script>

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
	
</style>

<liferay-ui:success key="success" message="Downloaded file Sent to your email." />
<liferay-ui:error key="no-date-match-exception" message="No Data in particular Date you selected." />
<liferay-ui:error key="no-date" message="Please select valid date." />
 
<script>
/* document.getElementById('_Reconcile_WAR_dalradareconcilefileuploadportlet_dateStartOrder').value = new Date(StartDate).toDateInputValue(); */

	function startDateValidateOrder() {
		var dateStart = $("#_Reconcile_WAR_dalradareconcilefileuploadportlet_dateStartOrder").val();
		var startdate_2 = new Date(dateStart);
		var today = new Date();
		if ((Date.parse(startdate_2) >= Date.parse(today))) {
			  alert("Start date cannot be more than todays date.");
		      document.getElementById("_Reconcile_WAR_dalradareconcilefileuploadportlet_dateStartOrder").value = "";
		  }
	}
	
	function verifyDateOrder() {
		
		var dateStart = $("#_Reconcile_WAR_dalradareconcilefileuploadportlet_dateStartOrder").val();
		var dateTo = $("#_Reconcile_WAR_dalradareconcilefileuploadportlet_dateToOrder").val();
		var startdate_2 = new Date(dateStart);
	    var enddate_2 = new Date(dateTo);
	    
	    var today = new Date();
		  if ((Date.parse(enddate_2) >= Date.parse(today))) {
			  alert("End date cannot be more than todays date.");
		      document.getElementById("_Reconcile_WAR_dalradareconcilefileuploadportlet_dateToOrder").value = "";
		  }
	
	    if ((Date.parse(startdate_2) > Date.parse(enddate_2))) {
	        alert("End date should be greater than Start date");
	        document.getElementById("_Reconcile_WAR_dalradareconcilefileuploadportlet_dateToOrder").value = "";
	    }
	}
</script>

<div class="container"  style="margin-left: -96px;">
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<portlet:actionURL var="orderCsvFileUploadURL" name="orderReportCsvFileUpload"></portlet:actionURL>
                <form action = "<%= orderCsvFileUploadURL %>" method = "post" enctype = "multipart/form-data" >
					<div class="panel panel-default">
						<div class="panel-heading">
						    <h4><b>Order Details</b></h4>
						</div>
						<div class="panel-body">
							<div class="col-sm-6">
								<div class="form-group">
									<label for="From_Date" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">From Date:</label>
										<aui:input type="date"  name="fromDate" label=""  id="dateStartOrder" placeholder="Select From date" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" />
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="To_Date" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">To Date:</label>
									<aui:input type="date" name="toDate" onChange="verifyDateOrder()" label="" id="dateToOrder" placeholder="Select To date" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" />
								</div>
							</div>
						</div>
						<div class="panel-footer">
							<div class="row">
								<div class="col-sm-12">
									<div class="col-sm-8">
											
									</div>
									<div class="col-sm-4" style="margin-left:704px;">
										<button type="submit" class="btn btn-md btn-primary" onclick="<%= orderCsvFileUploadURL %>"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Run</button>
										<button type="reset" class="btn btn-md btn-danger"><span class="	glyphicon glyphicon-remove"></span>&nbsp;Reset</button>
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
