package com.dalrada.dashboard.file.upload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.portlet.ActionRequest;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.prakat.dashboard.application.NoSuchInvoiceManagementException;
import com.prakat.dashboard.application.NoSuchInvoiceRegistrationException;
import com.prakat.dashboard.application.NoSuchShippingRegistrationException;
import com.prakat.dashboard.application.model.CustomerManagement;
import com.prakat.dashboard.application.model.InvoiceManagement;
import com.prakat.dashboard.application.model.InvoiceRegistration;
import com.prakat.dashboard.application.model.OrderRegistration;
import com.prakat.dashboard.application.model.ShippingRegistration;
import com.prakat.dashboard.application.model.TransCustomerManagement;
import com.prakat.dashboard.application.model.TransInvoiceManagement;
import com.prakat.dashboard.application.model.TransInvoiceRegistration;
import com.prakat.dashboard.application.model.TransShippingRegistration;
import com.prakat.dashboard.application.model.WarehouseFileRegistration;
import com.prakat.dashboard.application.model.WarehouseRegistration;
import com.prakat.dashboard.application.service.CustomerManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.InvoiceManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.InvoiceRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.OrderRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.ShippingRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.TransCustomerManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.TransInvoiceManagementLocalServiceUtil;
import com.prakat.dashboard.application.service.TransInvoiceRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.TransShippingRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.WarehouseFileRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil;

public class WarehouseInvoiceDetailsController {
	
	private static final char DEFAULT_SEPARATOR = '~';
    private static final char DEFAULT_QUOTE = '"';
	
	public static String warehouseCsvFileData(File csvFile, long UserId, long warehouseId, ActionRequest actionRequest){
		try {
			
			Scanner scanner = new Scanner(csvFile);
			List<String> lineNextData = null , line = null;
	        List<String> lineNext = null;
	        int counter = 0 , shippingCount = 0 , rows = 0 , flag = 0, rowNumber = 0, warehouseFlag = 0;
	        String status = "Success", stringWithoutSpacesStart = "", stringWithoutSpacesEnd = "";
	        Date fromDate = null, toDate = null;
	       
	        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
	        
	        WarehouseRegistration warehouseRegistration = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistration(warehouseId);
	        
			while (scanner.hasNext()) {
	        	 line = parseLine(scanner.nextLine());
	        	 
	        	 if(rows == 0){
	        		 /*if(line.get(0).equalsIgnoreCase("United Natural Foods, Inc - East Region")){
	        			 continue;
	        		 }else*/ if(line.get(0).equalsIgnoreCase("United Natural Foods, Inc - East Region") == false){
	        			break;
	        		 }
	        	 }
				if(rowNumber != 0){
					break;
				}
				if(rows == 1){
					String[] dateSplit = line.get(8).split(":");
					String[] dateSplitDate = dateSplit[1].split("to");
					
					stringWithoutSpacesStart = dateSplitDate[0].replaceAll("\\s+", "");
				    stringWithoutSpacesEnd = dateSplitDate[1].replaceAll("\\s+", "");
					
					fromDate = new Date(stringWithoutSpacesStart);
					toDate = new Date(stringWithoutSpacesEnd);
					
				}
	        	 if(line.get(0).equalsIgnoreCase("Invoice Number:")){
					  lineNext = parseLine(scanner.nextLine());
					  if(lineNext.get(3).equalsIgnoreCase(String.valueOf(warehouseRegistration.getWarehouseCode())) == false){
						  status = "FailWarehouse ";
						  rowNumber = rows;
						  break;
					  }
					  rows ++;
					  //System.out.println("InvoiceDetails  :: "+lineNext);
					  counter++;
					  flag = invoiceDetailsData(lineNext, warehouseId);//Insert into InvoiceDetails Table
					  if(flag == 1){
						 status = "Fail ";
						 rowNumber = rows;
						 break;
					 }
					  
					  while(scanner.hasNext()){
						  lineNextData = parseLine(scanner.nextLine());
						  
						  if(lineNextData.get(0).equalsIgnoreCase("Totals for Invoice Number:")){
							  //System.out.println("counter :: "+counter);
							  //System.out.println("Shipping Counter :: "+shippingCount);
							  //System.out.println("InvoiceMaster :: "+lineNextData);
							  flag = invoiceMasterData(lineNextData,UserId,lineNext.get(3),counter,shippingCount,warehouseId);//Insert into InvoiceMaster Table
							  if(flag == 1){
									 status = "Fail ";
									 rowNumber = rows;
									 break;
							  }
							  counter = 0;
							  shippingCount = 0;
							  //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
							  List<String> lineNext2 = parseLine(scanner.nextLine());
							  rows ++;
							  List<String> lineNext3 = parseLine(scanner.nextLine());
							  rows ++;
							  if(lineNext3.get(0).equalsIgnoreCase("Totals for Customer:")){
								  //System.out.println("CustomerInvoiceSummary  :: "+lineNext3);
								  flag = customerDetailsData(lineNext3, toDate, fromDate,warehouseId);//Insert into CustomerDetails Table
								  //System.out.println("flag  :: "+flag);
								  if(flag == 1){
										 status = "Fail ";
										 rowNumber = rows;
										 break;
								   }
								  break;
							  }
							  lineNext2.clear();
						  }
						  else{
							  if(lineNextData.get(5).equalsIgnoreCase("1196021")){
								  //System.out.println("ShippingDetails  :: "+lineNextData);
								  shippingCount++;
								  flag = shippingDetailsData(lineNextData,warehouseId);//Insert into ShippingDetails Table
								  if(flag == 1){
										 status = "Fail ";
										 rowNumber = rows;
										 break;
									 }
							  	}else{
									 //System.out.println("InvoiceDetails  :: "+lineNextData);
									 counter++;
									 flag = invoiceDetailsData(lineNextData, warehouseId);//Insert into InvoiceDetails Table
									 if(flag == 1){
										 status = "Fail ";
										 rowNumber = rows;
										 break;
									 }
								  }
						  }
						  lineNextData.clear();
						  rows ++;
					  }
					  lineNext.clear();
				  }
	        	 rows++;
	        }
	        scanner.close();
	        
	        if(rows == 0){
	        	status = "Fail ";
	        	SessionErrors.add(actionRequest, "file-exception");
    			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	        }
	        
	        return status+rowNumber;
		}catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		}
	}
	
	 private static int invoiceDetailsData(List<String> lineNextData, long warehouseId) throws SystemException {
	    try{
	    	int flag =0;
	    	try{
	    		InvoiceRegistration invoiceRegistrationDetails = InvoiceRegistrationLocalServiceUtil.findByinvoiceNumber(Long.parseLong(lineNextData.get(0)));
	    		if(lineNextData.get(0).isEmpty() == false ){
	    			invoiceRegistrationDetails.setPoNumber(lineNextData.get(1));
					  
	    			 if(lineNextData.get(2).isEmpty() == false){
	    				 Date orderDate = new Date(lineNextData.get(2));
	    				 invoiceRegistrationDetails.setInvDate(orderDate);
	    			 }
						  
	    			 long productName = 0;
	    			 if(lineNextData.get(5).isEmpty() == false){
	    				 productName = Long.parseLong(lineNextData.get(5));
	    			 }
					  
					  invoiceRegistrationDetails.setWarehouseID(warehouseId);
					  invoiceRegistrationDetails.setCustNumber(Integer.parseInt(lineNextData.get(3)));
					  invoiceRegistrationDetails.setChain(lineNextData.get(4));
					  invoiceRegistrationDetails.setProductNumber(productName);
					  invoiceRegistrationDetails.setUpcNumber(lineNextData.get(6));
					  invoiceRegistrationDetails.setBrand(lineNextData.get(7));
					  invoiceRegistrationDetails.setDescription(lineNextData.get(8));
					  invoiceRegistrationDetails.setPack(Integer.parseInt(lineNextData.get(9)));
					  invoiceRegistrationDetails.setSize(lineNextData.get(10));
					  invoiceRegistrationDetails.setOrdered(Integer.parseInt(lineNextData.get(11)));
					  invoiceRegistrationDetails.setShipped(Integer.parseInt(lineNextData.get(12)));
					  invoiceRegistrationDetails.setDiscDescr(lineNextData.get(13));
					
					  String discount1 = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
					  discount1 = discount1.replaceAll(",", "");
					  String tax1 = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
					  tax1 = tax1.replaceAll(",", "");
					  
					  char[] b = lineNextData.get(16).toCharArray();
						String d = "";
						for(int i =0; i<b.length;i++) {
							if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
								continue;
							}else{
								d=d+b[i];
							}
						}
					  String extNetUnit1 = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
					  extNetUnit1 = extNetUnit1.replaceAll(",", "");
					 
					  invoiceRegistrationDetails.setDiscounts(Double.parseDouble(discount1));
					  invoiceRegistrationDetails.setTax(Double.parseDouble(tax1));
					  invoiceRegistrationDetails.setExtNetUnit(Double.parseDouble(d));
					  
					  InvoiceRegistrationLocalServiceUtil.updateInvoiceRegistration(invoiceRegistrationDetails);
				  }else{
					  flag = 1;
				  }
	    	}catch(NoSuchInvoiceRegistrationException ex){
	    		long invoiceId = 0;
		    	TransInvoiceRegistration invoiceRegistration = null;
		    	
				  
		    	 invoiceId = CounterLocalServiceUtil.increment(TransInvoiceRegistration.class.getName());
				  invoiceRegistration = TransInvoiceRegistrationLocalServiceUtil.createTransInvoiceRegistration(invoiceId);
				 
				  if(lineNextData.get(0).isEmpty() == false ){
					  invoiceRegistration.setInvoiceNumber(Long.parseLong(lineNextData.get(0)));
					  invoiceRegistration.setPoNumber(lineNextData.get(1));
					  if(lineNextData.get(2).isEmpty() == false){
						  Date orderDate = new Date(lineNextData.get(2));
						  invoiceRegistration.setInvDate(orderDate);
					  }
					  
					  long productName = 0;
		    			 if(lineNextData.get(5).isEmpty() == false){
		    				 productName = Long.parseLong(lineNextData.get(5));
		    			 }
		    			 
					  invoiceRegistration.setWarehouseID(warehouseId);
					  invoiceRegistration.setCustNumber(Integer.parseInt(lineNextData.get(3)));
					  invoiceRegistration.setChain(lineNextData.get(4));
					  invoiceRegistration.setProductNumber(productName);
					  invoiceRegistration.setUpcNumber(lineNextData.get(6));
					  invoiceRegistration.setBrand(lineNextData.get(7));
					  invoiceRegistration.setDescription(lineNextData.get(8));
					  invoiceRegistration.setPack(Integer.parseInt(lineNextData.get(9)));
					  invoiceRegistration.setSize(lineNextData.get(10));
					  invoiceRegistration.setOrdered(Integer.parseInt(lineNextData.get(11)));
					  invoiceRegistration.setShipped(Integer.parseInt(lineNextData.get(12)));
					  invoiceRegistration.setDiscDescr(lineNextData.get(13));
					
					  String discount1 = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
					  discount1 = discount1.replaceAll(",", "");
					  String tax1 = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
					  tax1 = tax1.replaceAll(",", "");
					  
					  char[] b = lineNextData.get(16).toCharArray();
						String d = "";
						for(int i =0; i<b.length;i++) {
							if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
								continue;
							}else{
								d=d+b[i];
							}
						}
					  String extNetUnit1 = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
					  extNetUnit1 = extNetUnit1.replaceAll(",", "");
					 
					  invoiceRegistration.setDiscounts(Double.parseDouble(discount1));
					  invoiceRegistration.setTax(Double.parseDouble(tax1));
					  invoiceRegistration.setExtNetUnit(Double.parseDouble(d));
					  
					  TransInvoiceRegistrationLocalServiceUtil.addTransInvoiceRegistration(invoiceRegistration);
				  }else{
					  flag = 1;
				  }
	    	}
	    	
	    	return flag;
	    }catch(Exception e){
	    	return 1;
	    }
	}
    private static int invoiceMasterData(List<String> lineNextData,long userID,String custNumber,int counter,int shippingCount,long warehouseId) throws SystemException {
    	try{
	    	Date currentDate = new Date();
	    	int flag = 0;
	    	try{
	    		InvoiceManagement invoiceManagementData = InvoiceManagementLocalServiceUtil.findByinvoiceNumber(Long.parseLong(lineNextData.get(3)));
	    		invoiceManagementData.setWarehouseID(warehouseId);
					invoiceManagementData.setCustNumber(Integer.parseInt(custNumber));
				
					 if(lineNextData.get(2).isEmpty() == false){
					  Date orderDate = new Date(lineNextData.get(2));
					  invoiceManagementData.setInvDate(orderDate);
					 }
				  
				invoiceManagementData.setOrdered(Integer.parseInt(lineNextData.get(11)));
				invoiceManagementData.setShipped(Integer.parseInt(lineNextData.get(12)));
				  
				String discountMaster = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
				discountMaster = discountMaster.replaceAll(",", "");
				String taxMaster = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
				taxMaster = taxMaster.replaceAll(",", "");
				
				 char[] b = lineNextData.get(16).toCharArray();
					String d = "";
					for(int i =0; i<b.length;i++) {
						if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
							continue;
						}else{
							d=d+b[i];
						}
					}
				String extNetUnitMaster = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
				extNetUnitMaster = extNetUnitMaster.replaceAll(",", "");
				  
				invoiceManagementData.setDiscounts(Double.parseDouble(discountMaster));
				invoiceManagementData.setTax(Double.parseDouble(taxMaster));
				invoiceManagementData.setExtNetUnit(Double.parseDouble(d));
				invoiceManagementData.setInvCount(counter);
				invoiceManagementData.setShipCount(shippingCount);
				invoiceManagementData.setDateCreated(currentDate);
				invoiceManagementData.setCreatedBy(userID);
				  
				InvoiceManagementLocalServiceUtil.updateInvoiceManagement(invoiceManagementData);
	    	}catch(NoSuchInvoiceManagementException ex){
	    		TransInvoiceManagement invoiceManagement = null;
				long invoiceManagementId = 0 ;
				
				invoiceManagementId = CounterLocalServiceUtil.increment(TransInvoiceManagement.class.getName());
				invoiceManagement = TransInvoiceManagementLocalServiceUtil.createTransInvoiceManagement(invoiceManagementId);
				  
				if(lineNextData.get(3).isEmpty() == false){
					invoiceManagement.setInvoiceNumber(Long.parseLong(lineNextData.get(3)));
				}
				else{
					flag = 1;
				}
				invoiceManagement.setWarehouseID(warehouseId);
					invoiceManagement.setCustNumber(Integer.parseInt(custNumber));
								
					 if(lineNextData.get(2).isEmpty() == false){	
						 Date orderDate = new Date(lineNextData.get(2));
					  invoiceManagement.setInvDate(orderDate);
					 }
				  
				invoiceManagement.setOrdered(Integer.parseInt(lineNextData.get(11)));
				invoiceManagement.setShipped(Integer.parseInt(lineNextData.get(12)));
				  
				String discountMaster = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
				discountMaster = discountMaster.replaceAll(",", "");
				String taxMaster = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
				taxMaster = taxMaster.replaceAll(",", "");
				
				 char[] b = lineNextData.get(16).toCharArray();
					String d = "";
					for(int i =0; i<b.length;i++) {
						if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
							continue;
						}else{
							d=d+b[i];
						}
					}
				String extNetUnitMaster = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
				extNetUnitMaster = extNetUnitMaster.replaceAll(",", "");
				  
				invoiceManagement.setDiscounts(Double.parseDouble(discountMaster));
				invoiceManagement.setTax(Double.parseDouble(taxMaster));
				invoiceManagement.setExtNetUnit(Double.parseDouble(d));
				invoiceManagement.setInvCount(counter);
				invoiceManagement.setShipCount(shippingCount);
				invoiceManagement.setDateCreated(currentDate);
				invoiceManagement.setCreatedBy(userID);
				  
				TransInvoiceManagementLocalServiceUtil.addTransInvoiceManagement(invoiceManagement);
	    	}
	    	
			return flag;
	    }catch(Exception e){
	    	return 1;
	    }
	}
    // if flag value is 0 no errors otherwise wrong
    private static int shippingDetailsData(List<String> lineNextData,long warehouseId) throws SystemException {
    	try{
    		int flag = 0;
    		try{
				 ShippingRegistration shippingRegistrationDetails = ShippingRegistrationLocalServiceUtil.findByinvoiceNumber(Long.parseLong(lineNextData.get(0)));
				  shippingRegistrationDetails.setPoNumber(lineNextData.get(1));
  			 
				  if(lineNextData.get(2).isEmpty() == false){
  				  Date orderDate = new Date(lineNextData.get(2));
  				  shippingRegistrationDetails.setInvDate(orderDate);
				  }
				  long productName = 0;
	    			 if(lineNextData.get(5).isEmpty() == false){
	    				 productName = Long.parseLong(lineNextData.get(5));
	    			 }
	  			  shippingRegistrationDetails.setWarehouseID(warehouseId);
	  			  shippingRegistrationDetails.setCustNumber(Integer.parseInt(lineNextData.get(3)));
	  			  shippingRegistrationDetails.setChain(lineNextData.get(4));
	  			  shippingRegistrationDetails.setProductNumber(productName);
	  			  shippingRegistrationDetails.setUpcNumber(lineNextData.get(6));
	  			  shippingRegistrationDetails.setBrand(lineNextData.get(7));
	  			  shippingRegistrationDetails.setDescription(lineNextData.get(8));
	  			  shippingRegistrationDetails.setPack(Integer.parseInt(lineNextData.get(9)));
	  			  shippingRegistrationDetails.setSize(lineNextData.get(10));
	  			  shippingRegistrationDetails.setOrdered(Integer.parseInt(lineNextData.get(11)));
	  			  shippingRegistrationDetails.setShipped(Integer.parseInt(lineNextData.get(12)));
	  			 
	  			  String discountShipping = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
	  			  String taxShipping = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
	  			  
	  			char[] b = lineNextData.get(16).toCharArray();
				String d = "";
				for(int i =0; i<b.length;i++) {
					if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
						continue;
					}else{
						d=d+b[i];
					}
				}
	  			  String extNetUnitShipping = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
	  			  
	  			  shippingRegistrationDetails.setDiscDescr(lineNextData.get(13));
	  			  shippingRegistrationDetails.setDiscounts(Double.parseDouble(discountShipping));
	  			  shippingRegistrationDetails.setTax(Double.parseDouble(taxShipping));
	  			  shippingRegistrationDetails.setExtNetUnit(Double.parseDouble(d));
	  			  
	  			  ShippingRegistrationLocalServiceUtil.updateShippingRegistration(shippingRegistrationDetails);
    		}catch(NoSuchShippingRegistrationException ex){
    			TransShippingRegistration shippingRegistration = null;
    	        long shippingId = 0 ;
    	        
    	    	shippingId = CounterLocalServiceUtil.increment(TransShippingRegistration.class.getName());
    			  shippingRegistration = TransShippingRegistrationLocalServiceUtil.createTransShippingRegistration(shippingId);
    			  
    			  if(lineNextData.get(0).isEmpty() == false){
    				  shippingRegistration.setInvoiceNumber(Long.parseLong(lineNextData.get(0)));
    				}else{
    					flag = 1;
    				}
    			  
				  shippingRegistration.setPoNumber(lineNextData.get(1));
				  if(lineNextData.get(2).isEmpty() == false){
				  Date orderDate = new Date(lineNextData.get(2));
				  shippingRegistration.setInvDate(orderDate);
				  }
				  long productName = 0;
	    			 if(lineNextData.get(5).isEmpty() == false){
	    				 productName = Long.parseLong(lineNextData.get(5));
	    			 }
    			  shippingRegistration.setWarehouseID(warehouseId);
    			  shippingRegistration.setCustNumber(Integer.parseInt(lineNextData.get(3)));
    			  shippingRegistration.setChain(lineNextData.get(4));
    			  shippingRegistration.setProductNumber(productName);
    			  shippingRegistration.setUpcNumber(lineNextData.get(6));
    			  shippingRegistration.setBrand(lineNextData.get(7));
    			  shippingRegistration.setDescription(lineNextData.get(8));
    			  shippingRegistration.setPack(Integer.parseInt(lineNextData.get(9)));
    			  shippingRegistration.setSize(lineNextData.get(10));
    			  shippingRegistration.setOrdered(Integer.parseInt(lineNextData.get(11)));
    			  shippingRegistration.setShipped(Integer.parseInt(lineNextData.get(12)));
    			 
    			  String discountShipping = (String) lineNextData.get(14).subSequence(1, lineNextData.get(14).length());
    			  String taxShipping = (String) lineNextData.get(15).subSequence(1, lineNextData.get(15).length());
    			  
    			  char[] b = lineNextData.get(16).toCharArray();
					String d = "";
					for(int i =0; i<b.length;i++) {
						if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
							continue;
						}else{
							d=d+b[i];
						}
					}
    			  String extNetUnitShipping = (String) lineNextData.get(16).subSequence(1, lineNextData.get(16).length());
    			  
    			  shippingRegistration.setDiscDescr(lineNextData.get(13));
    			  shippingRegistration.setDiscounts(Double.parseDouble(discountShipping));
    			  shippingRegistration.setTax(Double.parseDouble(taxShipping));
    			  shippingRegistration.setExtNetUnit(Double.parseDouble(d));
    			  
    			  TransShippingRegistrationLocalServiceUtil.addTransShippingRegistration(shippingRegistration);
    		}
    		
			 return flag;
	    }catch(Exception e){
	    	return 1;
	    }
	}
    private static int customerDetailsData(List<String> lineNext3, Date toDate, Date fromDate,long warehouseId) throws SystemException {
    	try{
    		int flag = 0, warehouseFlag = 0;
    		long CustId = 0;
    		List<CustomerManagement> customerManagementList = CustomerManagementLocalServiceUtil.findByCustomer_From_To_Date(fromDate, toDate);
    		
    		if(customerManagementList.size() > 0){
    			for(CustomerManagement customerDataObj : customerManagementList){
					if(customerDataObj.getWarehouseID() == warehouseId){
						warehouseFlag = 1;
						CustId = customerDataObj.getCustID();
					}
				}
    		}
    		
    		if(warehouseFlag == 0){
    			long customerManagementId = 0;
    	    	  TransCustomerManagement customerManagement = null;
    	    	  customerManagementId = CounterLocalServiceUtil.increment(TransCustomerManagement.class.getName());
    			  customerManagement = TransCustomerManagementLocalServiceUtil.createTransCustomerManagement(customerManagementId);
    			  
    			  customerManagement.setWarehouseID(warehouseId);
    			  if(lineNext3.get(2).isEmpty() == false){
    				  customerManagement.setCustNumber(Integer.parseInt(lineNext3.get(2)));
    			  }else{
    				  flag = 1;
    			  }
    			  
    			  customerManagement.setCustName(lineNext3.get(3));
    			  customerManagement.setOrdered(Integer.parseInt(lineNext3.get(11)));
    			  customerManagement.setShipped(Integer.parseInt(lineNext3.get(12)));
    			  
    			  String discountCustomer = (String) lineNext3.get(14).subSequence(1, lineNext3.get(14).length());
    			  discountCustomer = discountCustomer.replaceAll(",", "");
    			  String taxCustomer = (String) lineNext3.get(15).subSequence(1, lineNext3.get(15).length());
    			  taxCustomer = taxCustomer.replaceAll(",", "");
    			  
    			  char[] b = lineNext3.get(16).toCharArray();
					String d = "";
					for(int i =0; i<b.length;i++) {
						if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
							continue;
						}else{
							d=d+b[i];
						}
					}
    			  String extNetUnitCustomer = (String) lineNext3.get(16).subSequence(1, lineNext3.get(16).length());
    			  extNetUnitCustomer = extNetUnitCustomer.replaceAll(",", "");
    			  
    			  customerManagement.setDiscounts(Double.parseDouble(discountCustomer));
    			  customerManagement.setTax(Double.parseDouble(taxCustomer));
    			  customerManagement.setExtNetUnit(Double.parseDouble(d));
    			  customerManagement.setFromDate(fromDate);
    			  customerManagement.setToDate(toDate);
    			  
    			  TransCustomerManagementLocalServiceUtil.addTransCustomerManagement(customerManagement);
    		}
    		
    		if(warehouseFlag == 1){
    			
    			CustomerManagement customerManagementDetails = CustomerManagementLocalServiceUtil.findBycustID(CustId);
    			customerManagementDetails.setWarehouseID(warehouseId);
    			  if(lineNext3.get(2).isEmpty() == false){
    				  customerManagementDetails.setCustNumber(Integer.parseInt(lineNext3.get(2)));
    			  }else{
    				  flag = 1;
    			  }
    			  
    			  customerManagementDetails.setCustName(lineNext3.get(3));
    			  customerManagementDetails.setOrdered(Integer.parseInt(lineNext3.get(11)));
    			  customerManagementDetails.setShipped(Integer.parseInt(lineNext3.get(12)));
    			  
    			  String discountCustomer = (String) lineNext3.get(14).subSequence(1, lineNext3.get(14).length());
    			  discountCustomer = discountCustomer.replaceAll(",", "");
    			  String taxCustomer = (String) lineNext3.get(15).subSequence(1, lineNext3.get(15).length());
    			  taxCustomer = taxCustomer.replaceAll(",", "");
    			  
    			  char[] b = lineNext3.get(16).toCharArray();
					String d = "";
					for(int i =0; i<b.length;i++) {
						if(b[i]=='('||b[i]==')'||b[i]=='$'||b[i]==',') {
							continue;
						}else{
							d=d+b[i];
						}
					}
    			  String extNetUnitCustomer = (String) lineNext3.get(16).subSequence(1, lineNext3.get(16).length());
    			  extNetUnitCustomer = extNetUnitCustomer.replaceAll(",", "");
    			  
    			  customerManagementDetails.setDiscounts(Double.parseDouble(discountCustomer));
    			  customerManagementDetails.setTax(Double.parseDouble(taxCustomer));
    			  customerManagementDetails.setExtNetUnit(Double.parseDouble(d));
    			  customerManagementDetails.setFromDate(fromDate);
    			  customerManagementDetails.setToDate(toDate);
    			  
    			  CustomerManagementLocalServiceUtil.updateCustomerManagement(customerManagementDetails);
    		}
    	  
		  return flag;
	    }catch(Exception e){
	    	return 1;
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

}
