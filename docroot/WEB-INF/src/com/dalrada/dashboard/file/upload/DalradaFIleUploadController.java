package com.dalrada.dashboard.file.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.prakat.dashboard.application.NoSuchInvoiceManagementException;
import com.prakat.dashboard.application.NoSuchInvoiceRegistrationException;
import com.prakat.dashboard.application.model.InvoiceManagement;
import com.prakat.dashboard.application.model.InvoiceRegistration;
import com.prakat.dashboard.application.model.OrderRegistration;
import com.prakat.dashboard.application.model.TransCustomerManagement;
import com.prakat.dashboard.application.model.WarehouseFileRegistration;
import com.prakat.dashboard.application.model.WarehouseRegistration;
import com.prakat.dashboard.application.service.InvoiceManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.InvoiceRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.OrderRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.TransCustomerManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.WarehouseFileRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

class MyFileFormatException extends Exception
{
	 public MyFileFormatException(String s) 
	    { 
	        // Call constructor of parent Exception 
	        super(s); 
	    } 
}
class NoDataFileException extends Exception
{
	 public NoDataFileException(String s) 
	    { 
	        // Call constructor of parent Exception 
	        super(s); 
	    } 
}

public class DalradaFIleUploadController extends MVCPortlet{

	private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    
    
    /* This function using for uploading order details CSV file. */
    public void orderCsvFileUpload(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try {
			
			 ThemeDisplay td  =(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
	         User user = td.getUser();
	         
	         SimpleDateFormat dateFormat1=new SimpleDateFormat("MM-dd-YYYY");//using for file name creation date
	         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");//using for mysql
	         
			 UploadPortletRequest uploadPortletRequest = null;
			 uploadPortletRequest  = PortalUtil.getUploadPortletRequest(actionRequest);
			 String fileDataInsertion = null;
			 File csvFile = uploadPortletRequest.getFile("orderCsvFile"); 
			 
			 Date today = new Date();
			 String fileName = "/opt/liferay/tomcat-7.0.62/webapps/portal_content/dashboard_files/Order"+ dateFormat1.format(today) +".csv";
			 //String fileName = "F:/dalrada_kt/DashboardApplicationDataFile/CorruptedOrderRows"+ dateFormat1.format(today) +".csv";
			 
			 File file = new File(fileName);
			 FileWriter outputfile = new FileWriter(file); 
			 CSVWriter writer = new CSVWriter(outputfile);
			 int errorData = 0;
			 String[] dataHeading = {"Order Date","Store Order Id","Order Number", "PO Number", "Reference Number","Channel Name", "Order Status", "SKU","Product Name",
					 "Quantity", "Supplier Cost Per SKU","Sale Price", "Supplier Cost Total", "Order Total","Supplier Name", "Store Shipping Method", 
					 "Shipping Carrier","Shipping Method", "Tracking Number(s)", "Customer Name","Customer Email", "Customer Phone", "Address Line 1"
					 ,"Address Line 2", "City", "State","Zip","Country","Company"};
	  	    	writer.writeNext(dataHeading);
			 
			 if(csvFile.length()<=0){
				 throw new NoDataFileException(" "); 
			 }else{
				 String name = csvFile.getName();
				 String extension = name.substring(name.lastIndexOf("."));
				 
				 if(extension.equalsIgnoreCase(".csv")){
					 int count = 0;
					
			         Date toDate = dateFormat.parse(actionRequest.getParameter("toDate"));
			         Date fromDate = dateFormat.parse(actionRequest.getParameter("fromDate"));
			         
					 Scanner scanner = new Scanner(csvFile);
			         while (scanner.hasNext()) {
			        	List<String> line = parseLine(scanner.nextLine());
			        	//System.out.println(line.size());
			        	
			        	if(count == 0){
			        		 if(line.get(0).equalsIgnoreCase("Order Date") == false){
			        			 break;
			        		 }
			        	 }else{
			        		 if(count >= 1){
					  	            String normalDoubleValue = null;
					  	            if(line.get(18).isEmpty() == false){
					  	            	try{
					  		            	 Double Tracking_Numbers = Double.parseDouble(line.get(18));
					  		                 normalDoubleValue = String.format("%.0f",Tracking_Numbers);
					  	            	}catch(NumberFormatException e){
					  	            		normalDoubleValue = line.get(18);
					  	            	}
					  	            }
					  	            fileDataInsertion = FileStoringServiceController.orderCsvFileData(line.get(0),line.get(1), line.get(2), line.get(3), line.get(4), line.get(5), line.get(6), 
					  	        		line.get(7), line.get(8), line.get(9), line.get(10),line.get(11), line.get(12), line.get(13), line.get(14), line.get(15), 
					  	        		line.get(16), line.get(17),normalDoubleValue, line.get(19), line.get(20), line.get(21), line.get(22), line.get(23), line.get(24),
					  	        		line.get(25), line.get(26),line.get(27),line.get(28), toDate, fromDate,user.getUserId());
					  	      
						  	      if(fileDataInsertion.equalsIgnoreCase("Fail")){
						  	    	System.out.println("Order Data :: "+line);
						  	    	String[] data1 = {line.get(0),line.get(1), line.get(2), line.get(3), line.get(4), line.get(5), line.get(6), 
						  	        		line.get(7), line.get(8), line.get(9), line.get(10),line.get(11), line.get(12), line.get(13), line.get(14), line.get(15), 
						  	        		line.get(16), line.get(17), normalDoubleValue, line.get(19), line.get(20), line.get(21), line.get(22), line.get(23), line.get(24),
						  	        		line.get(25), line.get(26),line.get(27),line.get(27),line.get(28)};
						  	    	errorData ++;
						  	    	writer.writeNext(data1);
						  	      }
					  	      
					        }
			        	 }
			        	count++;
			        }
			        scanner.close();
			        writer.close();
			        
			        if(count == 0){
			        	SessionErrors.add(actionRequest, "file-exception");
	        			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			        }else if(errorData == 0){
			        	SessionMessages.add(actionRequest, "success");
		    			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			        }
			        
			        if(errorData > 0){
			        	SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			        	actionResponse.setRenderParameter("ErrorDataValue", String.valueOf(errorData));
			        	actionResponse.setRenderParameter("ErrorDataFileName", file.getAbsolutePath());
						actionResponse.setRenderParameter("jspPage", "/jsp/ErrorDataDownload.jsp");
			        }
			        
				 }else{
					 throw new MyFileFormatException(" "); 
				 }
			 }
			}catch (MyFileFormatException ex){ 
	            SessionErrors.add(actionRequest, "file-extension-exception");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	        }catch (NoDataFileException ex){ 
	            SessionErrors.add(actionRequest, "file-data-exception");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	        }
			catch (Exception e) {
				e.printStackTrace();
			}
	}
    
    public void uploadWarehousrCSVData(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException, SystemException, ParseException {
    	try{
    		
	    	 UploadPortletRequest uploadPortletRequest = null;
			 uploadPortletRequest  = PortalUtil.getUploadPortletRequest(actionRequest);
			 File csvFile = uploadPortletRequest.getFile("orderWarehouseCsvFile"); 
			 Date toDate = null, fromDate = null , currentDate = new Date();
			 
			 String fileDataInsertion = null;
			 int flag = 0, warehouseFlag = 0, startDateFlag = 0;
			 
			 if(csvFile.length()<=0){
				 throw new NoDataFileException(" "); 
				 
			 }else{
				 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				 String name = csvFile.getName();
				 String extension = name.substring(name.lastIndexOf("."));
				 long warehouseId = Long.parseLong(actionRequest.getParameter("selectedData"));
				 
				 if(extension.equalsIgnoreCase(".csv")){
					 
					 
					 /*This code not using. 28-11-2019*/
			         List<WarehouseFileRegistration> fileRegistrationList = WarehouseFileRegistrationLocalServiceUtil.getWarehouseFileRegistrations(0,WarehouseFileRegistrationLocalServiceUtil.getWarehouseFileRegistrationsCount());
			         Date warehouseTodate = null , warehouseFromDate = null;
			         WarehouseFileRegistration fileRegistration = null;
			         
			         if(fileRegistrationList.size() > 0){
				       	  fileRegistration = fileRegistrationList.get(fileRegistrationList.size()-1);
				       	 
				       	  warehouseTodate = fileRegistration.getToDate();
				       	  warehouseFromDate = fileRegistration.getFromDate();
			         }
			         /*End This code not using. 28-11-2019*/
	        	 ThemeDisplay themeDisplayCurrentUser = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
				 User currentUser = themeDisplayCurrentUser.getUser();
					
				 WarehouseInvoiceDetailsController invoiceDetailsController = new WarehouseInvoiceDetailsController();
				 fileDataInsertion = WarehouseInvoiceDetailsController.warehouseCsvFileData(csvFile,currentUser.getUserId(), warehouseId, actionRequest);
				 String[] status = fileDataInsertion.split(" ");
				 String data = status[0]; 
				 
				 if(data.equalsIgnoreCase("FailWarehouse")){
					 flag = 1;
					 SessionErrors.add(actionRequest, "warehouseCode-exception");
		    		 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		    		 
				 }else if(data.equalsIgnoreCase("Fail")){
					 String rowNumber = status[1];
					 flag = 1;
						 java.sql.Connection conn1 = null;
				  	      
				  	      String driver1 = PropsUtil.get("jdbc.default.driverClassName");
						  String url1 = PropsUtil.get("jdbc.default.url");
						  String userName1 = PropsUtil.get("jdbc.default.username");
						  String password1 = PropsUtil.get("jdbc.default.password");
							
				  	      try {
					  	      Class.forName(driver1).newInstance();
					  	      conn1 = DriverManager.getConnection(url1,userName1,password1);
					  	      Statement st1 = conn1.createStatement();
					  	      
					  	      int deleteInvoice1 = st1.executeUpdate("TRUNCATE TABLE da_trns_invoice_details");
					  	      int deleteMaster1= st1.executeUpdate("TRUNCATE TABLE da_trns_invoice_master");
					  	      int deleteShipping1 = st1.executeUpdate("TRUNCATE TABLE da_trns_shipping_details");
					  	      int deleteCustomer1 = st1.executeUpdate("TRUNCATE TABLE da_trns_customer_master");
					  	      
					  	      conn1.close();
				  	      }
				  	      catch (Exception e) {
				  	      e.printStackTrace();
				  	      }
				  	 
				  	 actionResponse.setRenderParameter("my-error-number", rowNumber);
					 actionResponse.setRenderParameter("jspPage", "/jsp/WarehouseErrorCode.jsp");
					
					 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
					 
					 }else if(data.equalsIgnoreCase("FailAlready")){//Not using
						 warehouseFlag = 1;
					}
				 }
				 else{
					 throw new MyFileFormatException("Incorrect file format. "); 
				 }
				 
				 if(warehouseFlag == 1){//Not using if condition only not else if
					 flag = 1;
					 SessionErrors.add(actionRequest, "warehouseDate-exception");
		    		 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				 }else if(flag == 0){
					  java.sql.Connection conn = null;
			  	      
			  	      String driver = PropsUtil.get("jdbc.default.driverClassName");
					  String url = PropsUtil.get("jdbc.default.url");
					  String userName = PropsUtil.get("jdbc.default.username");
					  String password = PropsUtil.get("jdbc.default.password");
						
			  	      try {
				  	      Class.forName(driver).newInstance();
				  	      conn = DriverManager.getConnection(url,userName,password);
				  	      Statement st = conn.createStatement();
				  	      //Copy table
				  	      int rowsInvoice = st.executeUpdate("INSERT INTO da_invoice_details SELECT * FROM da_trns_invoice_details");
				  	      if (rowsInvoice == 0){
				  	    	  System.out.println("Don't add any row in Invoice Details Table!");
				  	      }
				  	      int rowsMaster = st.executeUpdate("INSERT INTO da_invoice_master SELECT * FROM da_trns_invoice_master");
				  	      if (rowsMaster == 0){
				  	    	  System.out.println("Don't add any row in Invoice Master Table!");
				  	      }
				  	      int rowsShipping = st.executeUpdate("INSERT INTO da_shipping_details SELECT * FROM da_trns_shipping_details");
				  	      if (rowsShipping == 0){
				  	    	  System.out.println("Don't add any row in Shipping Details Table!");
				  	      }
				  	      int rowsCustomer = st.executeUpdate("INSERT INTO da_customer_master SELECT * FROM da_trns_customer_master");
				  	      if (rowsCustomer == 0){
				  	    	  System.out.println("Don't add any row in Customer Details Table!");
				  	      }
				  	      else{
					  	      System.out.println(rowsInvoice + " row(s)affected in Invoice Details Table.");
					  	      System.out.println(rowsMaster + " row(s)affected in Invoice Master Table.");
					  	      System.out.println(rowsShipping + " row(s)affected in Shipping Details Table.");
					  	      System.out.println(rowsCustomer + " row(s)affected in Customer Details Table.");
					  	      
					  	      List<TransCustomerManagement> customerManagement = TransCustomerManagementLocalServiceUtil.getTransCustomerManagements(0, TransCustomerManagementLocalServiceUtil.getTransCustomerManagementsCount());
					  	      TransCustomerManagement customerManagementLastData = null;
					  	      if(customerManagement.size() > 0){
					  	    	customerManagementLastData = customerManagement.get(customerManagement.size()-1);
						       	 
					  	    	fromDate = customerManagementLastData.getFromDate();
					  	    	toDate = customerManagementLastData.getToDate();
					  	    	
					  	      }
					  	      
					  	      WarehouseFileRegistration fileRegistration = null;
					  	      long fileRegistrationId = 0;
					  	      
					  	      fileRegistrationId = CounterLocalServiceUtil.increment(WarehouseFileRegistration.class.getName());
					  	      fileRegistration = WarehouseFileRegistrationLocalServiceUtil.createWarehouseFileRegistration(fileRegistrationId);
					  	    
					  	      fileRegistration.setFileName(csvFile.getName());
					  	      fileRegistration.setWarehouseID(warehouseId);
					  	      fileRegistration.setFromDate(fromDate);
					  	      fileRegistration.setToDate(toDate);
					  	      fileRegistration.setCreatedDate(currentDate);
					  	  
					  	      WarehouseFileRegistrationLocalServiceUtil.addWarehouseFileRegistration(fileRegistration);
					  	      
				  	      }
				  	      
				  	      int deleteInvoice = st.executeUpdate("TRUNCATE TABLE da_trns_invoice_details");
				  	      int deleteMaster = st.executeUpdate("TRUNCATE TABLE da_trns_invoice_master");
				  	      int deleteShipping = st.executeUpdate("TRUNCATE TABLE da_trns_shipping_details");
				  	      int deleteCustomer = st.executeUpdate("TRUNCATE TABLE da_trns_customer_master");
				  	      
				  	      conn.close();
				  	      SessionMessages.add(actionRequest, "success");
		    			  SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
		    			  
			  	      }
			  	      catch (Exception e) {
			  	      e.printStackTrace();
			  	      }
				 }
			 }
    	}catch (MyFileFormatException ex){ 
           
            SessionErrors.add(actionRequest, "file-extension-exception");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			
        }catch (NoDataFileException ex){ 
            SessionErrors.add(actionRequest, "file-data-exception");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
        }catch(Exception e){
    		//e.printStackTrace();
        	SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
    	}
    }
    
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<String>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
    
    String dateStart = "", dateTo = "";
    /*This function using for download corruptedOrderRows(Today date).xsls*/
    @Override
	public final void serveResource(final ResourceRequest request, 
			final ResourceResponse response) throws IOException, PortletException {
    	try{
    		 
    		String ServerResourceReconcile = request.getParameter("ServerResourceReconcile");
    		
    		SimpleDateFormat formetter = new SimpleDateFormat("MM-dd-YYYY");
    		ArrayList<String> arrayList = new ArrayList<String>();
    		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
    		
    		if(ServerResourceReconcile.equals("ErrorDataFileName")){
    			
    			String ErrorDataFileName = request.getParameter("ErrorDataFileName");
        		
        		File file = new File(ErrorDataFileName);
        		
        		final String downloadDirectoryPath = file.getParent();
        		final File outputFile = new File(downloadDirectoryPath, file.getName());
        		response.setContentType("text/plain");
        		response.addProperty("Content-disposition", "atachment; filename="+file.getName());
        		OutputStream out = null;
        		InputStream in = null;
        		try {
        			out = response.getPortletOutputStream();
        			in = new FileInputStream(outputFile);
        			IOUtils.copy(in, out);		
        		} catch (final IOException e) {
        			e.printStackTrace();
        		} finally {
        			try {
        				if (Validator.isNotNull(out)) {
        					out.flush();
        					out.close();
        				}
        				if (Validator.isNotNull(in)) {
        					in.close();
        				}

        			} catch (final IOException e) {
        				e.printStackTrace();
        			}
        			SessionMessages.add(request, "success");
        			SessionMessages.add(request, PortalUtil.getPortletId(request) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
        		}	
    		}
    		if(ServerResourceReconcile.equals("ReconcileReportCsv")){ //Not using this condition28/11/2019
    			
    			 Date today = new Date();
	       		 SimpleDateFormat dateFormat1=new SimpleDateFormat("MM-dd-YYYY");
	   			 String fileName = "/opt/liferay/tomcat-7.0.62/webapps/portal_content/dashboard_files/ReconcileReport"+ dateFormat1.format(today)+".csv";
	   			 //String fileName = "F:/dalrada_kt/DashboardApplicationDataFile/ReconcileReport"+ dateFormat1.format(today) +".csv";
	   			 
	   			 File file = new File(fileName);
	   			 FileWriter outputfile = new FileWriter(file); 
	   			 CSVWriter writer = new CSVWriter(outputfile);
	   			 
	   			 String[] dataHeading = {"Order Date","Order Number","PO Number","Channel Name","Order Status","SKU","Product Name","Quantity", 
	   					 "Sale Price","Order Total","Supplier Cost Per SKU","Amazon Amt","Ext Net Unit","Profit","ROI"};
	   	  	    	writer.writeNext(dataHeading);
	   			 
	   			 List<OrderRegistration> listOrder=OrderRegistrationLocalServiceUtil.getOrderRegistrations(0, OrderRegistrationLocalServiceUtil.getOrderRegistrationsCount());
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
	    				
	    				DecimalFormat df2 = new DecimalFormat("#.##"); 
	    				df2.setRoundingMode(RoundingMode.DOWN);
	    					
	       				String[] data1 = {String.valueOf(orderDetails.getOrderDate()),orderDetails.getOrderNumber(),orderDetails.getPoNumber(),
	       						orderDetails.getChannelName(),orderDetails.getOrderStatus(),orderDetails.getSKU(),orderDetails.getProductName(),
	       						String.valueOf(orderDetails.getQuantity()),String.valueOf(df2.format(orderDetails.getSalePrice())),String.valueOf(orderDetails.getOrderTotal()),
	       						String.valueOf(orderDetails.getSupplierCostPerSKU()),String.valueOf(orderDetails.getAmazonAmount()),String.valueOf(ExtNetUnit),
	       						String.valueOf(orderDetails.getProfit()),String.valueOf(orderDetails.getRoi())+"%"};
	       	  	    	writer.writeNext(data1);
	    			
	    			}
	   	  	    	writer.close();
   	  	    	
	   	  	    final String downloadDirectoryPathReport = file.getParent();
	       		final File outputFileReport = new File(downloadDirectoryPathReport, file.getName());
	       		response.setContentType("text/plain");
	       		response.addProperty("Content-disposition", "atachment; filename="+file.getName());
	       		
	       		OutputStream out = null;
        		InputStream in = null;
        		try {
        			out = response.getPortletOutputStream();
        			in = new FileInputStream(outputFileReport);
        			IOUtils.copy(in, out);		
        		} catch (final IOException e) {
        			e.printStackTrace();
        		} finally {
        			try {
        				if (Validator.isNotNull(out)) {
        					out.flush();
        					out.close();
        				}
        				if (Validator.isNotNull(in)) {
        					in.close();
        				}

        			} catch (final IOException e) {
        				e.printStackTrace();
        			}
        			SessionMessages.add(request, "success");
        			SessionMessages.add(request, PortalUtil.getPortletId(request) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
        		}
    		}
    		
    		if(ServerResourceReconcile.equals("WarehouseStartSelection")){
    			dateStart = ParamUtil.getString(request, "dateStart");
    		}
    		if(ServerResourceReconcile.equals("WarehouseEndSelection")){
    			dateTo = ParamUtil.getString(request, "dateTo");
    		}
    		if(ServerResourceReconcile.equals("WarehouseLoadSelection")){
    			dateStart = ParamUtil.getString(request, "dateStart");
    			dateTo = ParamUtil.getString(request, "dateTo");
    		}
    		if(dateStart != "" && dateTo != "" ){
    			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

    			Date startDateServer = dateFormat.parse(dateStart);
    			Date endDateServer = dateFormat.parse(dateTo);
    				
    			int counter = 0 , rowDataWarehouse = 0;
    			ArrayList<Long> arrli = new ArrayList<Long>(); 
    			ArrayList<Long> arrliAll = new ArrayList<Long>(); 
    			ArrayList<Long> arrliDiff = new ArrayList<Long>(); 
    			
    			List<WarehouseFileRegistration> warehouseRegistrationList = WarehouseFileRegistrationLocalServiceUtil.findByFrom_To_Date(startDateServer, endDateServer);
    			
				String warehouseId ="", warehouseName ="";
				
				java.util.Collections.sort(warehouseRegistrationList);
				
				if(warehouseRegistrationList.size() > 0){
					for(WarehouseFileRegistration registration : warehouseRegistrationList){
						WarehouseRegistration warehouseRegistration = WarehouseRegistrationLocalServiceUtil.findBywarehouseID(registration.getWarehouseID());
						if(registration.getWarehouseID() == warehouseRegistration.getWarehouseID()){
							
							arrli.add(warehouseRegistration.getWarehouseID()); 
							
							arrayList.add( String.valueOf(warehouseRegistration.getWarehouseID()) );
							arrayList.add( warehouseRegistration.getWarehouseName() );
							warehouseId = String.valueOf(warehouseRegistration.getWarehouseID());
							warehouseName = warehouseRegistration.getWarehouseName();
							
							JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
							jsonObject.put("warehouseYes",1);
							jsonObject.put("warehouseId",warehouseId);
							jsonObject.put("warehouseName",warehouseName);
							jsonArray.put(jsonObject);
						}
					 }	
				}
				
				List<WarehouseRegistration> warehouseRegistration = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrations(0, WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrationsCount());
				for(WarehouseRegistration wareObj : warehouseRegistration){
					arrliAll.add(wareObj.getWarehouseID());
				}
				arrliDiff = (ArrayList<Long>) org.apache.commons.collections.CollectionUtils.disjunction(arrli,arrliAll);
				java.util.Collections.sort(arrliDiff);
				
				for(int i = 0; i<arrliDiff.size(); i++){
					WarehouseRegistration warehouseRegistration2 = WarehouseRegistrationLocalServiceUtil.findBywarehouseID(arrliDiff.get(i));
					
					arrayList.add( String.valueOf(warehouseRegistration2.getWarehouseID()) );
					arrayList.add( warehouseRegistration2.getWarehouseName() );
					warehouseId = String.valueOf(warehouseRegistration2.getWarehouseID());
					warehouseName = warehouseRegistration2.getWarehouseName();
					
					JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
					jsonObject.put("warehouseYes",0);
					jsonObject.put("warehouseId",warehouseId);
					jsonObject.put("warehouseName",warehouseName);
					jsonArray.put(jsonObject);
				}
				
				Iterator<String> iterator = arrayList.iterator();
				while (iterator.hasNext()) {
					String name = (String) iterator.next();
				}
				
				  PrintWriter writer = response.getWriter();
			        writer.write(jsonArray.toString());
			        writer.flush();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
    
}

