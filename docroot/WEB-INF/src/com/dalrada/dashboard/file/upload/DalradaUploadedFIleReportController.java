package com.dalrada.dashboard.file.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.StateAwareResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.prakat.dashboard.application.NoSuchInvoiceManagementException;
import com.prakat.dashboard.application.NoSuchInvoiceRegistrationException;
import com.prakat.dashboard.application.model.InvoiceManagement;
import com.prakat.dashboard.application.model.InvoiceRegistration;
import com.prakat.dashboard.application.model.OrderRegistration;
import com.prakat.dashboard.application.service.InvoiceManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.InvoiceRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.OrderRegistrationLocalServiceUtil;


public class DalradaUploadedFIleReportController extends MVCPortlet{
	
	 Date startDateServer = null, endDateServer = null;
	
	  public void orderReportCsvFileUpload(ActionRequest actionRequest,
	    		ActionResponse actionResponse) throws IOException, PortletException {
	    	try{
	    		
	    		CacheRegistryUtil.clear();
	    		MultiVMPoolUtil.clear();
	    		WebCachePoolUtil.clear();
	    		
	    		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		 String fromDateString = actionRequest.getParameter("fromDate");
				 String toDateString = actionRequest.getParameter("toDate");
				 
				 if(fromDateString.isEmpty() == false){
					 if(toDateString.isEmpty() == false){
						 Date toDate = dateFormat.parse(toDateString);
				         Date fromDate = dateFormat.parse(fromDateString);
						 
				         //List<WarehouseFileRegistration> warehouseRegistrationList = WarehouseFileRegistrationLocalServiceUtil.findByFrom_To_Date(fromDate, toDate);
				         
						 //if(warehouseRegistrationList.size() > 0){
				         
							 startDateServer = fromDate;
							 endDateServer = toDate;
							 actionResponse.setRenderParameter("toDate", String.valueOf(toDate));
							 actionResponse.setRenderParameter("fromDate", String.valueOf(fromDate));
							 actionResponse.setRenderParameter("jspPage", "/jsp/ReconcileReport1.jsp");
							 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
						// }
							 /*if(warehouseRegistrationList.size() == 0){
							 SessionErrors.add(actionRequest, "no-date-match-exception");
							 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
							 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
						 }*/
						 
					 }else{
						 SessionErrors.add(actionRequest, "no-date");
						 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
					 }
				 }else{
					 SessionErrors.add(actionRequest, "no-date");
					 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				 }
	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	   /*This function using for sending email with csv file. currently not using.*/
		public void reconcileSendMail(ActionRequest actionRequest,
				ActionResponse actionResponse) throws IOException, PortletException {
			try {
				
				  Date today = new Date();
		       		 SimpleDateFormat dateFormat1=new SimpleDateFormat("MM-dd-yyyy");
		   			 String fileName = "/opt/liferay/tomcat-7.0.62/webapps/portal_content/dashboard_files/ReconcileReport"+ dateFormat1.format(today)+".xlsx";
		   			 //String fileName = "F:/dalrada_kt/DashboardApplicationDataFile/ReconcileReport"+ dateFormat1.format(today) +".xlsx";
		   			 
		   			 File file = new File(fileName);
		   			 
		   			 String[] dataHeading = {"Order Date","Order Number","PO Number","Channel Name","Order Status","SKU","Product Name","Quantity", 
		   					 "Sale Price","Order Total","Supplier Cost Per SKU","Amazon Amt","Ext Net Unit","Profit","ROI"};
		   	  	    	//writer.writeNext(dataHeading);
		   	  	    	
		   	  	   Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
		   	  	   CreationHelper createHelper = workbook.getCreationHelper();

			        // Create a Sheet
			        Sheet sheet = workbook.createSheet("Employee");

			        // Create a Font for styling header cells
			        Font headerFont = workbook.createFont();
			        headerFont.setFontHeightInPoints((short) 14);
			        headerFont.setColor(IndexedColors.RED.getIndex());
			        
			        CellStyle headerCellStyle = workbook.createCellStyle();
			        headerCellStyle.setFont(headerFont);

			        // Create a Row
			        Row headerRow = sheet.createRow(0);

			        // Create cells
			        for(int i = 0; i < dataHeading.length; i++) {
			            Cell cell = headerRow.createCell(i);
			            cell.setCellValue(dataHeading[i]);
			            cell.setCellStyle(headerCellStyle);
			        }
			        int rowNum = 1;
			        List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.findOrderDetailsBetweenQuery(startDateServer, endDateServer);
	 				 for (OrderRegistration orderDetails : listOrder) {
		    				long invoiceNumber = 0;
		    				Double ExtNetUnit = 0.0 , profit = 0.0;
		    				try{
		    					InvoiceRegistration invoiceManagement;
								try {
									invoiceManagement = InvoiceRegistrationLocalServiceUtil.findBypoNumber(orderDetails.getPoNumber());
									invoiceNumber =	invoiceManagement.getInvoiceNumber();
								} catch (SystemException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    					InvoiceManagement invoiceData;
								try {
									invoiceData = InvoiceManagementLocalServiceUtil.findByinvoiceNumber(invoiceNumber);
									ExtNetUnit = invoiceData.getExtNetUnit();
								} catch (SystemException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		       					
		    				}catch(NoSuchInvoiceRegistrationException ex){
		    				}catch(NoSuchInvoiceManagementException ex){
		    				}
		    				
            				
		    				DecimalFormat df2 = new DecimalFormat("#.##"); 
		    				df2.setRoundingMode(RoundingMode.DOWN);
		    					
				       	  	Row row = sheet.createRow(rowNum++);
				       	  	row.createCell(0).setCellValue(String.valueOf(orderDetails.getOrderDate()));
				       	  	row.createCell(1).setCellValue(orderDetails.getOrderNumber());
				       	  	row.createCell(2).setCellValue(orderDetails.getPoNumber());
				       	  	row.createCell(3).setCellValue(orderDetails.getChannelName());
				       	  	row.createCell(4).setCellValue(orderDetails.getOrderStatus());
				       	  	row.createCell(5).setCellValue(orderDetails.getSKU());
				       	  	row.createCell(6).setCellValue(orderDetails.getProductName());
				       	  	row.createCell(7).setCellValue(String.valueOf(orderDetails.getQuantity()));
				       	  	row.createCell(8).setCellValue(String.valueOf(df2.format(orderDetails.getSalePrice())));
				       	  	row.createCell(9).setCellValue(String.valueOf(orderDetails.getOrderTotal()));
				       	  	row.createCell(10).setCellValue(String.valueOf(orderDetails.getSupplierCostPerSKU()));
				       	  	row.createCell(11).setCellValue(df2.format(orderDetails.getAmazonAmount()));
				       	  	row.createCell(12).setCellValue(String.valueOf(ExtNetUnit));
				       	  	row.createCell(13).setCellValue(df2.format(orderDetails.getProfit()));
				       	  	row.createCell(14).setCellValue(orderDetails.getRoi()+"%");
				    		}
			        
		   	       	 for(int i = 0; i < dataHeading.length; i++) {
				            sheet.autoSizeColumn(i);
				        }
			       	 
			         FileOutputStream fileOut = new FileOutputStream(fileName);
				        workbook.write(fileOut);
				        fileOut.close();

		   	  	    final String downloadDirectoryPathReport = file.getParent();
		       		final File outputFileReport = new File(downloadDirectoryPathReport, file.getName());
		       		
		       		ThemeDisplay td  =(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		       		User user = td.getUser();
		       		
		       		//Edit By Mani on 11-08-2019
		       		SendAttachmentInEmail.sendEmail("website.prakat@gmail.com", user.getEmailAddress() , "Reconcile Report File", "Download attchment file for Reconcile Report.", file.getPath());
		       		
	       			SessionMessages.add(actionRequest, "success");
	       			actionResponse.setRenderParameter("jspPage", "/jsp/Upload.jsp");
        			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	  
		/*Reconcile Report generated*/
		@Override
		public final void serveResource(final ResourceRequest request, 
				final ResourceResponse response) throws IOException, PortletException {
		  
		  String ServerResourceReconcile = request.getParameter("cmd");
		  
		  //Download the csv file with data.
		  if(ServerResourceReconcile.equals("downloadDocument")){
			  Date today = new Date();
	       		 SimpleDateFormat dateFormat1=new SimpleDateFormat("MM-dd-yyyy");
	       		 String fileName = "/opt/liferay/tomcat-7.0.62/webapps/portal_content/dashboard_files/ReconcileReport"+ dateFormat1.format(today)+".xlsx";
	   			 //String fileName = "F:/dalrada_kt/DashboardApplicationDataFile/ReconcileReport"+ dateFormat1.format(today) +".xlsx";
	   			 
	   			 File file = new File(fileName);
	   			 
	   			 String[] dataHeading = {"Order Date","Order Number","PO Number","Channel Name","Order Status","SKU","Product Name","Quantity", 
	   					 "Sale Price","Order Total","Supplier Cost Per SKU","Amazon Amt","Ext Net Unit","Profit","ROI"};
	   	  	    	//writer.writeNext(dataHeading);
	   	  	    	
	   	  	   Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
	   	  	   CreationHelper createHelper = workbook.getCreationHelper();

		        // Create a Sheet
		        Sheet sheet = workbook.createSheet("Employee");

		        // Create a Font for styling header cells
		        Font headerFont = workbook.createFont();
		        headerFont.setFontHeightInPoints((short) 14);
		        headerFont.setColor(IndexedColors.RED.getIndex());
		        
		        CellStyle headerCellStyle = workbook.createCellStyle();
		        headerCellStyle.setFont(headerFont);

		        // Create a Row
		        Row headerRow = sheet.createRow(0);

		        // Create cells
		        for(int i = 0; i < dataHeading.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(dataHeading[i]);
		            cell.setCellStyle(headerCellStyle);
		        }
		        int rowNum = 1;
		        List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.findOrderDetailsBetweenQuery(startDateServer, endDateServer);
				 for (OrderRegistration orderDetails : listOrder) {
	    				long invoiceNumber = 0;
	    				Double ExtNetUnit = 0.0;
	    				try{
	    					InvoiceRegistration invoiceManagement;
							try {
								invoiceManagement = InvoiceRegistrationLocalServiceUtil.findBypoNumber(orderDetails.getPoNumber());
								invoiceNumber =	invoiceManagement.getInvoiceNumber();
							} catch (SystemException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    					InvoiceManagement invoiceData;
							try {
								invoiceData = InvoiceManagementLocalServiceUtil.findByinvoiceNumber(invoiceNumber);
								ExtNetUnit = invoiceData.getExtNetUnit();
							} catch (SystemException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	       					
	    				}catch(NoSuchInvoiceRegistrationException ex){
	    				}catch(NoSuchInvoiceManagementException ex){
	    				}
	    				
	    				DecimalFormat df2 = new DecimalFormat("#.##"); 
	    				df2.setRoundingMode(RoundingMode.DOWN);
	    			   
	    					
			       	  	Row row = sheet.createRow(rowNum++);
			       	  	row.createCell(0).setCellValue(String.valueOf(orderDetails.getOrderDate()));
			       	  	row.createCell(1).setCellValue(orderDetails.getOrderNumber());
			       	  	row.createCell(2).setCellValue(orderDetails.getPoNumber());
			       	  	row.createCell(3).setCellValue(orderDetails.getChannelName());
			       	  	row.createCell(4).setCellValue(orderDetails.getOrderStatus());
			       	  	row.createCell(5).setCellValue(orderDetails.getSKU());
			       	  	row.createCell(6).setCellValue(orderDetails.getProductName());
			       	  	row.createCell(7).setCellValue(String.valueOf(orderDetails.getQuantity()));
			       	  	row.createCell(8).setCellValue(String.valueOf(df2.format(orderDetails.getSalePrice())));
			       	  	row.createCell(9).setCellValue(String.valueOf(orderDetails.getOrderTotal()));
			       	  	row.createCell(10).setCellValue(String.valueOf(orderDetails.getSupplierCostPerSKU()));
			       	  	row.createCell(11).setCellValue(df2.format(orderDetails.getAmazonAmount()));
			       	  	row.createCell(12).setCellValue(String.valueOf(ExtNetUnit));
			       	  	row.createCell(13).setCellValue(df2.format(orderDetails.getProfit()));
			       	  	row.createCell(14).setCellValue(df2.format(orderDetails.getRoi())+"%");
			    		}
		        
		   	       	 for(int i = 0; i < dataHeading.length; i++) {
				            sheet.autoSizeColumn(i);
				        }
			       	 
			        FileOutputStream fileOut = new FileOutputStream(fileName);
			        workbook.write(fileOut);
			        fileOut.close();
			        
			        //Download The file in local system
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
	        		//End Download The file in local system
		  }
		//End Download the csv file with data.
		  
		  //Quantity value is more than one 
		  if(ServerResourceReconcile.equals("flatDataSource")){
			  JSONArray usersJsonArray = JSONFactoryUtil.createJSONArray();
			  try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					JSONObject jsonUser = null;
					
					List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.findOrderDetailsBetweenQuery(startDateServer, endDateServer);
					if(listOrder.size() > 0){
	        			for (OrderRegistration orderDetails : listOrder) {
	        			    long invoiceNumber = 0;
	        				Double ExtNetUnit = 0.0;
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
	           				
	           				if(orderDetails.getQuantity() == 1){
	           				
		          			  jsonUser = JSONFactoryUtil.createJSONObject();
		        			  jsonUser.put("orderDate", formatter.format(orderDetails.getOrderDate()));
		        			  jsonUser.put("orderNumber", orderDetails.getOrderNumber());
		        			  jsonUser.put("poNumber", orderDetails.getPoNumber());
		        			  jsonUser.put("channelName", orderDetails.getChannelName());
		        			  jsonUser.put("orderStatus", orderDetails.getOrderStatus());
		        			  jsonUser.put("sku", orderDetails.getSKU());
		        			  jsonUser.put("productName", orderDetails.getProductName());
		        			  jsonUser.put("quantity", orderDetails.getQuantity());
		        			  jsonUser.put("orderTotal", orderDetails.getOrderTotal());
		        			  jsonUser.put("supplierCostPerSku", orderDetails.getSupplierCostPerSKU());
		        			  jsonUser.put("salesPrice", df2.format(orderDetails.getSalePrice()));
		        			  jsonUser.put("amazonAmount", df2.format(orderDetails.getAmazonAmount()));
		        			  jsonUser.put("extNetUnit", ExtNetUnit);
		        			  jsonUser.put("profit",  df2.format(orderDetails.getProfit()));
		        			  jsonUser.put("roi", df2.format(orderDetails.getRoi()));
		        			  usersJsonArray.put(jsonUser);
	           				}
	        			}
				  }
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			   
			  ServletResponseUtil.write(PortalUtil.getHttpServletResponse(response),
			  usersJsonArray.toString());

			  }
		  //End Quantity value is more than one 
		  
		  //Quantity value is one 
		  if(ServerResourceReconcile.equals("flatDataSourceMorethanOne")){
			  JSONArray usersJsonArray = JSONFactoryUtil.createJSONArray();
			  try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					JSONObject jsonUser = null;
					
					List<OrderRegistration> listOrder = OrderRegistrationLocalServiceUtil.findOrderDetailsBetweenQuery(startDateServer, endDateServer);
					if(listOrder.size() > 0){
	        			for (OrderRegistration orderDetails : listOrder) {
	        			    long invoiceNumber = 0;
	        				Double ExtNetUnit = 0.0; 
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
	           				
	           				if(orderDetails.getQuantity() > 1){
	           				  //System.out.println("amzPercentage : " + amzPercentage+",salesPrice : "+salesPrice);
		          			  jsonUser = JSONFactoryUtil.createJSONObject();
		        			  jsonUser.put("orderDate", formatter.format(orderDetails.getOrderDate()));
		        			  jsonUser.put("orderNumber", orderDetails.getOrderNumber());
		        			  jsonUser.put("poNumber", orderDetails.getPoNumber());
		        			  jsonUser.put("channelName", orderDetails.getChannelName());
		        			  jsonUser.put("orderStatus", orderDetails.getOrderStatus());
		        			  jsonUser.put("sku", orderDetails.getSKU());
		        			  jsonUser.put("productName", orderDetails.getProductName());
		        			  jsonUser.put("quantity", orderDetails.getQuantity());
		        			  jsonUser.put("orderTotal", orderDetails.getOrderTotal());
		        			  jsonUser.put("supplierCostPerSku", orderDetails.getSupplierCostPerSKU());
		        			  jsonUser.put("salesPrice", df2.format(orderDetails.getSalePrice()));
		        			  jsonUser.put("amazonAmount", df2.format(orderDetails.getAmazonAmount()));
		        			  jsonUser.put("extNetUnit", ExtNetUnit);
		        			  jsonUser.put("profit",  df2.format(orderDetails.getProfit()));
		        			  jsonUser.put("roi", df2.format(orderDetails.getRoi()));
		        			  usersJsonArray.put(jsonUser);
	           				}
	        			}
				  }
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			   
			  ServletResponseUtil.write(PortalUtil.getHttpServletResponse(response),
			  usersJsonArray.toString());

			  }
		  	//End Quantity value is one 
			  
			  
			  }
		
		
		
		
		
}

