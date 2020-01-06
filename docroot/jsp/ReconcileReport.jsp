<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Date"%>
<%@page import="com.prakat.dashboard.application.NoSuchInvoiceRegistrationException"%>
<%@page import="com.prakat.dashboard.application.NoSuchInvoiceManagementException"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.prakat.dashboard.application.service.InvoiceManagementLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.InvoiceManagement"%>
<%@page import="com.prakat.dashboard.application.service.InvoiceRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.InvoiceRegistration"%>
<%@page import="com.prakat.dashboard.application.service.OrderRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.OrderRegistration"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  
<script src="https://code.jquery.com/jquery-3.3.1.js"></script> -->
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">

<link rel="stylesheet" href="https://cdn.datatables.net/v/dt/dt-1.10.15/datatables.min.css">
<script src="<%=request.getContextPath()%>/js/datatables.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.mockjax.min.js"></script>
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
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
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

.dropdown-content a:hover {background-color: #337ab7;color:#ffffff;}

.dropdown:hover .dropdown-content {display: block;}

.dropdown:hover .dropbtn {background-color: #337ab7;}
      
		th{
			padding: 9px;
			border: 1px solid #ddd;
		} 
		td{
			padding: 9px;
		}
        div.dataTables_wrapper {
   			margin: 23px auto;
    }
    
    </style>

<%
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
String fromDate1  =renderRequest.getParameter("fromDate");
String toDate1  = renderRequest.getParameter("toDate");

Date fromDate = new Date(fromDate1);
Date toDate = new Date(toDate1);

%>

<portlet:resourceURL var="downloadReconcileReportURL">
	<portlet:param name="ServerResourceReconcile" value="ReconcileReportCsv"/>
</portlet:resourceURL>

<script type="text/javascript">

$(document).ready(function (){
    var table = $('#example').DataTable({
        'processing': true,
        "scrollX": true,
   		 "stateSave" : true
    });
});
 
</script>

<div class="container" style="margin-left: -96px;">
   <div class="row" >
            <div class="col-sm-12">
            <div class="col-sm-1"></div>
                <div class="col-sm-11" >
                        <div class="panel panel-default">
	                        <span class="loading-icon loading-icon-dotted">
							    <span class="loading-icon-indicator"></span>
							</span>
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="col-sm-10">
                                            <h4><b>Reconcile Report</b></h4>
                                        </div>
                                        <div class="col-sm-2">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <aui:button type="button" style="width: 200px;" class="btn btn-md btn-primary" onClick="<%= downloadReconcileReportURL %>"  value="Download Reconcile Report"/>
                            <br><br>
                            
                            <table  id="example"  class="display nowrap" style="width: 1900px;">
						        <thead>
						            <tr>
						            	<th style="width: 114px !important; " >Order Date</th>
						                  <th style="width: 160px !important; " >Order Number</th>
                                         <th style="width: 100px !important; ">PO Number</th>
                                         <th style="width: 148px !important; ">Channel Name</th>
                                         <th>Order Status</th>
                                         <th>SKU</th>
                                         <th style="width: 400px !important; ">Product Name</th>
                                         <th style="width: 100px !important; ">Quantity</th>
                                         <th style="width: 100px !important; ">Sale Price</th>
                                         <th style="width: 100px !important; ">Order Total</th>
                                         <th style="width: 100px !important; ">Supplier Cost Per SKU</th>
                                         <th style="width: 100px !important; ">Amazon Amt</th>
                                         <th style="width: 100px !important; ">Ext Net Unit</th>
                                         <th style="width: 100px !important; ">Profit</th>
                                         <th>ROI</th>
						            </tr>
						        </thead>
						      <tbody>
                                        <% 
                                        
                                        
                                       /*  DynamicQuery dynamicQueryStartDate = DynamicQueryFactoryUtil.forClass(OrderRegistration.class, PortletClassLoaderUtil.getClassLoader());
             							 
            							Criterion criterionStartDate = null;
            							criterionStartDate = RestrictionsFactoryUtil.ge("orderDate", fromDate);
            							criterionStartDate = RestrictionsFactoryUtil.and(criterionStartDate, RestrictionsFactoryUtil.le("orderDate", toDate));
            							dynamicQueryStartDate.add(criterionStartDate);
            							List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.dynamicQuery(dynamicQueryStartDate);
            							System.out.println(listOrder);
            							Collections.reverse(listOrder); */
            							List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.findOrderDetailsBetweenQuery(fromDate, toDate);
	                       				//System.out.println("Nirav :::: " + listOrder.size());
            							
            							if(listOrder.size() > 0){
	                            			for (OrderRegistration orderDetails : listOrder) {
	                            			    long invoiceNumber = 0;
	                            				Double ExtNetUnit = 0.0 , profit = 0.0; 
	                            				try{
	                            					InvoiceRegistration invoiceManagement = InvoiceRegistrationLocalServiceUtil.findBypoNumber(orderDetails.getPoNumber());
	                            					invoiceNumber =	invoiceManagement.getInvoiceNumber();
	                            				
	                            					InvoiceManagement invoiceData = InvoiceManagementLocalServiceUtil.findByinvoiceNumber(invoiceNumber);
		                           					ExtNetUnit = invoiceData.getExtNetUnit();
		                           					
	                            				}catch(NoSuchInvoiceRegistrationException ex){
	                            				}catch(NoSuchInvoiceManagementException ex){
	                            				}
	                            			    
	                            				double salePrice = orderDetails.getSalePrice();
	                            				double amzPercentage = (salePrice < 10) ? 0.8 : 0.15;
	                            				System.out.println("MK ::::amzPercentage : " + amzPercentage);
	                            				Double amazonAmt = orderDetails.getSalePrice() - (orderDetails.getSalePrice()* 0.15) ;
	                            				DecimalFormat df2 = new DecimalFormat("#.##"); 
	                            				df2.setRoundingMode(RoundingMode.DOWN);
		                           				profit = amazonAmt - ExtNetUnit;
	                            			%>
	                            			
		                            			<tr>
		                                           <td ><%= formatter.format(orderDetails.getOrderDate()) %></td>
		                                            <td ><%= orderDetails.getOrderNumber() %></td>
		                                             <td><%= orderDetails.getPoNumber() %></td>
		                                            <td><%= orderDetails.getChannelName() %></td>
		                                            <td><%= orderDetails.getOrderStatus() %></td>
		                                            <td><%= orderDetails.getSKU() %></td>
		                                            <td><%= orderDetails.getProductName() %></td>
		                                            <td><%= orderDetails.getQuantity() %></td>
		                                            <td><%= orderDetails.getSalePrice() %></td>
		                                            <td><%= orderDetails.getOrderTotal() %></td>
		                                            <td><%= orderDetails.getSupplierCostPerSKU() %></td>
		                                            <td><%= df2.format(amazonAmt) %></td>
		                                            <td><%= ExtNetUnit %></td>
		                                            <td><%= df2.format(profit) %></td>
		                                            <% if(ExtNetUnit != 0.0)
		                                            { %>
		                                            <td><%= df2.format((profit*100)/ExtNetUnit) %>%</td>
		                                            <%}else{ %>
		                                             <td>0.0%</td>
		                                            <%} %>
		                                        </tr>
	                            			<%
	                            			}
	                       				 }
                                        %>
                                    </tbody>
                                    <tfoot>
								        <tr>
								            <th style="width: 114px !important; " >Order Date</th>
								       <th style="width: 160px !important; " >Order Number</th>
								      <th style="width: 100px !important; ">PO Number</th>
								      <th style="width: 148px !important; ">Channel Name</th>
								      <th>Order Status</th>
								      <th>SKU</th>
								      <th style="width: 400px !important; ">Product Name</th>
								      <th style="width: 100px !important; ">Quantity</th>
								      <th style="width: 100px !important; ">Sale Price</th>
								      <th style="width: 100px !important; ">Order Total</th>
								      <th style="width: 100px !important; ">Supplier Cost Per SKU</th>
								      <th style="width: 100px !important; ">Amazon Amt</th>
								      <th style="width: 100px !important; ">Ext Net Unit</th>
								      <th style="width: 100px !important; ">Profit</th>
								      <th>ROI</th>
								        </tr>
								    </tfoot>
						     </table>
             
                            <div class="panel-footer">

                            </div>
                        </div>
                </div>
            </div>
        </div>
    </div>
