<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<script
	src="<%= renderRequest.getContextPath()%>/js/jquery-1.11.1.min.js"></script>
<script
	src="<%=renderRequest.getContextPath()%>/js/jquery.dataTables.min-1.10.1.js"></script>
	
<link rel="stylesheet"
	href="<%=renderRequest.getContextPath()%>/css/jquery.dataTables.css">
	

<portlet:resourceURL var="serveResource">
	<portlet:param name="cmd" value="flatDataSource" />
</portlet:resourceURL>

<%-- <portlet:resourceURL var="downloadURL">
	<portlet:param name="cmd" value="FileReconcileReportExcle" />
</portlet:resourceURL> --%>


<%-- <portlet:actionURL var="downloadURL" name="reconcileSendMail"></portlet:actionURL> --%>

<portlet:resourceURL var="downloadMorethanOneURL">
	<portlet:param name="cmd" value="flatDataSourceMorethanOne" />
</portlet:resourceURL>


<portlet:resourceURL var="downloadDocumentURL">
	<portlet:param name="cmd" value="downloadDocument" />
</portlet:resourceURL>

<style>
.message-container {
	padding: 10px;
	margin: 2px;
	display: none;
	background: rgba(128, 128, 128, 0.33);
	border: 1px solid #0A0A0C;
}

.portlet-title {
	visibility: hidden;
}

table#userTable thead tr,table#userSecoundTable thead tr{
display: none !important;
}

.aui .dataTable th {
    margin: 0px;
    padding: 4px;
}
.aui .dataTable td {
    width: 130px;
    text-align: center;
   /*  padding: 8px 18px !important;  */
}
.aui .dataTable td:nth-child(6),.aui .dataTable td:nth-child(9) {
    width: 300px;
}
table.dataTable td.dataTables_empty{

text-align:left !important;

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


<div id="loader" class="hidden"></div>



<h2><center><b>Reconcile Report</b></center></h2>

<div class="row">
    <button class="btn btn-md btn-primary" style="float:right; margin-bottom: 10px;" id="download_all">Download All</button>
</div>
			
<%-- <br>
<aui:button type="button" style="width: 300px;" id="download"
	class="btn btn-md btn-primary" onClick="<%= downloadDocumentURL %>"
	value="Download Reconcile Report" />
<br>
<br> --%>

<table id="userTable" class="display" cellspacing="0" width="100%">
	<thead>
		<tr style="background: #0088cc; color: white;">
			<th>Order Date</th>
			<th>PoNumber</th>
			<th>Channel Name</th>
			<th>Order Status</th>
			<th>sku</th>
			<th>Product Name</th>
			<th>Quantity</th>
			<th>Order Total</th>
			<th>Supplier Cost Per Sku</th>
			<th>Sales Price</th>
			<th>Amazon Amount</th>
			<th>Ext Net Unit</th>
			<th>Profit</th>
			<th>ROI</th>
		</tr>
	</thead>

</table>

<table id="userSecoundTable" class="display" cellspacing="0" width="100%">
	<thead>
		<tr style="background: #0088cc; color: white;">
			<th>Order Date</th>
			<th>PoNumber</th>
			<th>Channel Name</th>
			<th>Order Status</th>
			<th>sku</th>
			<th>Product Name</th>
			<th>Quantity</th>
			<th>Order Total</th>
			<th>Supplier Cost Per Sku</th>
			<th>Sales Price</th>
			<th>Amazon Amount</th>
			<th>ExtNetUnit</th>
			<th>Profit</th>
			<th>ROI</th>
		</tr>
	</thead>

</table>

<script>
	var serverResourceURL = "<%=serveResource%>";
	var downloadMoreThanOneURL = "<%=downloadMorethanOneURL%>";
</script>

<script	src="<%=renderRequest.getContextPath()%>/js/reconcile-report.js"></script>
