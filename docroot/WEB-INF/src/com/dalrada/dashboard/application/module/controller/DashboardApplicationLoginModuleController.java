package com.dalrada.dashboard.application.module.controller;

import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.counter.model.Counter;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PasswordTrackerLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.prakat.dashboard.application.service.RoleRegistrationLocalServiceUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ProcessAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class DashboardApplicationLoginModuleController extends MVCPortlet {
	
	 public void loginUser(ActionRequest actionRequest,
	            ActionResponse actionResponse) throws IOException, PortletException{
	           
	            String email = ParamUtil.getString(actionRequest, "emailId").trim();
	            String password = ParamUtil.getString(actionRequest, "password").trim();
	           
	            ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);     
	            HttpServletRequest request = PortalUtil.getHttpServletRequest(actionRequest);
	            HttpServletResponse response = PortalUtil.getHttpServletResponse(actionResponse);
	           
	            User userDetail = null;
	            try {
	                userDetail = UserLocalServiceUtil.fetchUserByEmailAddress(themeDisplay.getCompanyId(), email);
	                if(Validator.isNull(userDetail)){
	                 actionRequest.setAttribute("EMAIL_NOT_EXIST", "Account not exist with email");
	 				 SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	                  }else{
	                 boolean passwordMatched = PasswordTrackerLocalServiceUtil.isSameAsCurrentPassword(userDetail.getUserId(), password);
	                 if(!passwordMatched){
	                  actionRequest.setAttribute("PASSWORD_NOT_MATCHED", "Please Enter Correct Password");
	                  SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	                 }else{
	                  // if user exist with provide email and given password matched with user database password
	                  ClassLoader pcl = PortalClassLoaderUtil.getClassLoader();
	                  
	                        Class lClass = pcl.loadClass("com.liferay.portlet.login.util.LoginUtil");
	                        java.lang.reflect.Method method = lClass.getDeclaredMethod("login", HttpServletRequest.class, HttpServletResponse.class, String.class, String.class, Boolean.TYPE, String.class);
	                        method.invoke(null, request, response, email, password, false, CompanyConstants.AUTH_TYPE_EA);
	                       
	                          //redirection after login...
	                          String screenName = userDetail.getScreenName();
	                          String portalURL = PortalUtil.getPortalURL(actionRequest);
	                          if(PortalUtil.isOmniadmin(userDetail.getUserId()))
	                          {
	                              System.out.println("admin logged in, redirecting to admin  specified private page");
	                              actionResponse.sendRedirect(portalURL + "/web/dashboard/master-data");
	                          } else {
	                              System.out.println("normal user logged in, redirecting to private page specified for normal user");
	                              actionResponse.sendRedirect(portalURL + "/web/dashboard/master-data");       
	                          }
	                 }
	                 }
	              }catch (InvocationTargetException e) {
	            	  	SessionMessages.add(actionRequest, "Inactive-error-key");
	            	  	SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
	              }
	            catch (Exception e) {
                    e.printStackTrace();
              }
	     }
	 
	
	   
	}

