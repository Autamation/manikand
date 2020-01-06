package com.dalrada.dashboard.application.module.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.prakat.dashboard.application.model.RoleRegistration;
import com.prakat.dashboard.application.model.UserRegistration;
import com.prakat.dashboard.application.model.WarehouseRegistration;
import com.prakat.dashboard.application.service.RoleRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.UserRegistrationLocalServiceUtil;
import com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil;

public class DashboardApplicationModuleController extends MVCPortlet {
	
	public void roleRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			/*--------------------------- Fetch Data from jsp ---------------------------------------------*/
			String roleName = actionRequest.getParameter("roleName");
			
			Date creationDate = Calendar.getInstance().getTime();//current date
			
			int flag = 0;
			List<RoleRegistration> roleList = RoleRegistrationLocalServiceUtil.getRoleRegistrations(0, RoleRegistrationLocalServiceUtil.getRoleRegistrationsCount());
			for(RoleRegistration registration : roleList){
				if(registration.getRoleName().equalsIgnoreCase(roleName)){
					flag = 1;
					break;
				}
			}
			
			/*--------------------------- check & Add Data in db ---------------------------------------------*/
			if(flag == 1){ // Already registered role
				
				SessionErrors.add(actionRequest, "error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
			}else if(flag == 0) { //Not Register in db
				
				ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
				User user = themeDisplay.getUser();
				
				/*--------------------------- Add New Role ---------------------------------------------*/
				
				Role addRole = RoleLocalServiceUtil.addRole(user.getUserId(), user.getCompanyId(), roleName, null, null, RoleConstants.TYPE_REGULAR);
				RoleLocalServiceUtil.updateRole(addRole);
				
				Role adduserRole = RoleLocalServiceUtil.getRole(addRole.getRoleId());
				adduserRole.setName(roleName);
				RoleLocalServiceUtil.updateRole(adduserRole);
				
				/*--------------------------- Check List of Roles from DB ---------------------------------------------*/
				long liferayRoleId = 0;
				List<Role> userRoles=RoleLocalServiceUtil.getRoles(0, RoleLocalServiceUtil.getRolesCount());
				for (Role role : userRoles) {
					if(role.getName().equalsIgnoreCase(roleName) == true){
						liferayRoleId = role.getRoleId();
						break;
					}
				}
				/*--------------------------- Add Role in Custom DB ---------------------------------------------*/
				
				RoleRegistration roleRegistration = null;
				
				roleRegistration = RoleRegistrationLocalServiceUtil.createRoleRegistration(liferayRoleId);
				roleRegistration.setRoleName(roleName);
				roleRegistration.setStatus(DalradaDashboardConstant.ROLE_STATUS_ACTIVE);// 1:Active 0:Inactive
				roleRegistration.setCreatedBy(user.getUserId());
				roleRegistration.setCreatedDate(creationDate);
				
				RoleRegistrationLocalServiceUtil.addRoleRegistration(roleRegistration);
				
				SessionMessages.add(actionRequest,"success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void activeInactiveRole(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			/*--------------------------- Fetch Data from jsp ---------------------------------------------*/ 
			long roleId = Long.parseLong(actionRequest.getParameter("roleID"));
			
			RoleRegistration editRoleRegistrationData = RoleRegistrationLocalServiceUtil.getRoleRegistration(roleId);
			int flag = 0;
			
			List<User> user = UserLocalServiceUtil.getRoleUsers(roleId);
			
			/*--------------------------- Change Status of Active/Inactive Role ---------------------------------------------*/
			
			if(editRoleRegistrationData.getStatus() == DalradaDashboardConstant.ROLE_STATUS_ACTIVE){ // If Satus is Active
								
				if(user.isEmpty() == false){ // Already Assign to User
					SessionErrors.add(actionRequest, "already-error-key");
					SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				}
				else if(user.isEmpty() == true){ // Change Role Status
					editRoleRegistrationData.setStatus(DalradaDashboardConstant.ROLE_STATUS_INACTIVE);
					RoleRegistrationLocalServiceUtil.updateRoleRegistration(editRoleRegistrationData);
					SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				}
			}else if(editRoleRegistrationData.getStatus() == DalradaDashboardConstant.ROLE_STATUS_INACTIVE){ // If Satus is Inactive
				editRoleRegistrationData.setStatus(DalradaDashboardConstant.ROLE_STATUS_ACTIVE);
				RoleRegistrationLocalServiceUtil.updateRoleRegistration(editRoleRegistrationData);
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				
			}
			
		}catch(Exception e){
		}
	}
	
	public void eitRoleRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			/*--------------------------- Fetch Data from jsp ---------------------------------------------*/ 
			String eiteRoleName = actionRequest.getParameter("eiteRoleName");
			long roleId = Long.parseLong(actionRequest.getParameter("roleId"));
			int flag = 0;
			
			/*--------------------------- Check Role in DB ---------------------------------------------*/
			
			List<RoleRegistration> roleRegistrationsList = RoleRegistrationLocalServiceUtil.getRoleRegistrations(0, RoleRegistrationLocalServiceUtil.getRoleRegistrationsCount());
			for(RoleRegistration roleRegistration : roleRegistrationsList){
				if(eiteRoleName.equalsIgnoreCase(roleRegistration.getRoleName())){
					if(roleRegistration.getRoleID() != roleId){
						flag = 1;
						break;
					}
				}
			}
			
			/*--------------------------- Edit Role ---------------------------------------------*/
			
			if(flag == 0){ // Role not exist in db
				RoleRegistration editRoleRegistrationData = RoleRegistrationLocalServiceUtil.getRoleRegistration(roleId);
					
				editRoleRegistrationData.setRoleName(eiteRoleName);
				RoleRegistrationLocalServiceUtil.updateRoleRegistration(editRoleRegistrationData);
				
				RoleRegistrationLocalServiceUtil.updateRoleRegistration(editRoleRegistrationData);
				
				Role editUserRoles = RoleLocalServiceUtil.getRole(editRoleRegistrationData.getRoleID());
				editUserRoles.setName(eiteRoleName);
				RoleLocalServiceUtil.updateRole(editUserRoles);
				
				SessionMessages.add(actionRequest,"edit-success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
					
			}
			else{ // Already exist Role
				SessionErrors.add(actionRequest, "edit-error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			}
				
		}catch(Exception e){
			
		}
	}
	
	
	public void warehouseRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			
			/*--------------------------- Fetch Data from jsp ---------------------------------------------*/ 
			
			String warehouseName = actionRequest.getParameter("warehouseName");
			int warehouseCode = Integer.parseInt(actionRequest.getParameter("warehouseCode"));
			String warehouseAddress = actionRequest.getParameter("warehouseAddress");
			
			/*--------------------------- Fetch current logged-in user ---------------------------------------------*/ 
			ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();
			
			Date creationDate = Calendar.getInstance().getTime(); // Current Date
			
			/*--------------------------- Check duplicate WarehouseName and WarehouseCode  ---------------------------------------------*/
			int flag = 0;
			List<WarehouseRegistration> warehouseCheckList = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrations(0, WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrationsCount());
			for(WarehouseRegistration registration : warehouseCheckList){
				if(registration.getWarehouseName().equalsIgnoreCase(warehouseName)){
					flag = 1;
					break;
				}
				if(registration.getWarehouseCode() == warehouseCode){
					flag = 1;
					break;
				}
			}
			
			if(flag == 1){ // duplicate data
				
				SessionErrors.add(actionRequest, "error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
			}else if(flag == 0){ // no any duplication
				
				long warehouseId = 0;
				WarehouseRegistration warehouseRegistration = null;
				
				warehouseId = CounterLocalServiceUtil.increment(WarehouseRegistration.class.getName());
				warehouseRegistration = WarehouseRegistrationLocalServiceUtil.createWarehouseRegistration(warehouseId);
				
				warehouseRegistration.setWarehouseName(warehouseName);
				warehouseRegistration.setWarehouseCode(warehouseCode);
				warehouseRegistration.setWarehouseAddress(warehouseAddress);
				warehouseRegistration.setStatus(DalradaDashboardConstant.ROLE_STATUS_ACTIVE);
				warehouseRegistration.setCreatedBy(user.getUserId());
				warehouseRegistration.setCreatedDate(creationDate);
				
				WarehouseRegistrationLocalServiceUtil.addWarehouseRegistration(warehouseRegistration);
				
				SessionMessages.add(actionRequest,"success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
		}catch(Exception e){
			
		}
	}
	
	public void ActiveInactiveWarehouse(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			long warehouseId = Long.parseLong(actionRequest.getParameter("warehouseId"));
			
			WarehouseRegistration editWarehouseRegistrationData = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistration(warehouseId);// fetch particular warehouse details
			
			/*--------------------------- Change Status of Active/Inactive Warehouse ---------------------------------------------*/
			if(editWarehouseRegistrationData.getStatus() == DalradaDashboardConstant.ROLE_STATUS_ACTIVE){ // if Status Active
				editWarehouseRegistrationData.setStatus(DalradaDashboardConstant.ROLE_STATUS_INACTIVE);
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				
			}else if(editWarehouseRegistrationData.getStatus() == DalradaDashboardConstant.ROLE_STATUS_INACTIVE){ // if Status Inactive
				editWarehouseRegistrationData.setStatus(DalradaDashboardConstant.ROLE_STATUS_ACTIVE);
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
			WarehouseRegistrationLocalServiceUtil.updateWarehouseRegistration(editWarehouseRegistrationData);
			
		}catch(Exception e){
			
		}
	}
	
	public void editWarehouseRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			
			/*--------------------------- Fetch Data from jsp ---------------------------------------------*/
			String editWarehouseName = actionRequest.getParameter("eiteWarehouseName");
			int editwarehouseCode = Integer.parseInt(actionRequest.getParameter("warehouseCode"));
			String editwarehouseAddress = actionRequest.getParameter("warehouseAddress");
			long warehouseId = Long.parseLong(actionRequest.getParameter("warehouseId"));
			
			/*--------------------------- Check duplicate WarehouseName and WarehouseCode ---------------------------------------------*/
			int flag = 0;
			List<WarehouseRegistration> warehouseCheckList = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrations(0, WarehouseRegistrationLocalServiceUtil.getWarehouseRegistrationsCount());
			for(WarehouseRegistration registration : warehouseCheckList){
				if(registration.getWarehouseName().equalsIgnoreCase(editWarehouseName)){
					if(registration.getWarehouseID() != warehouseId){
						flag = 1;
						break;
					}
				}
				if(registration.getWarehouseCode() == editwarehouseCode){
					if(registration.getWarehouseID() != warehouseId){
						flag = 1;
						break;
					}
				}
			}
			
			if(flag == 1){ // duplicate data
				
				SessionErrors.add(actionRequest, "edit-error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
			}else if(flag == 0){ // no any duplication
			
				WarehouseRegistration editWarehouseRegistrationData = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistration(warehouseId);
				
				editWarehouseRegistrationData.setWarehouseName(editWarehouseName);
				editWarehouseRegistrationData.setWarehouseCode(editwarehouseCode);
				editWarehouseRegistrationData.setWarehouseAddress(editwarehouseAddress);
				
				WarehouseRegistrationLocalServiceUtil.updateWarehouseRegistration(editWarehouseRegistrationData);
				
				SessionMessages.add(actionRequest,"edit-success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
			
		}catch(Exception e){
			
		}
	}
	
	public void userRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			
			/*--------------------------- Add User and assign Role in Liferay DB ---------------------------------------------*/
			String fullName = actionRequest.getParameter("fullName");
			String roleName = actionRequest.getParameter("roleName");
			String email = actionRequest.getParameter("email");
			String password = actionRequest.getParameter("password");
			
			Date creationDate = Calendar.getInstance().getTime();//Current date
			
			System.out.println("Full Name :::: " + fullName);
			
			/*--------------------------- Check Aleady Registed User EmailId  ---------------------------------------------*/
			int flag = 0;
			List<UserRegistration> userList = UserRegistrationLocalServiceUtil.getUserRegistrations(0, UserRegistrationLocalServiceUtil.getUserRegistrationsCount());
			for(UserRegistration registration : userList){
				if(registration.getUserEmail().equalsIgnoreCase(email)){
					flag = 1;
					break;
				}
			}
			
			if(flag == 1){ // duplicate data
				
				SessionErrors.add(actionRequest, "error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
			}else if(flag == 0){ // no any duplication 
				
				/*--------------------------- Registed User in Liferay Table  ---------------------------------------------*/
				
				ThemeDisplay themeDisplayCurrentUser = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
				User currentUser = themeDisplayCurrentUser.getUser();
				
				ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
				User dalradaSiteUser = UserLocalServiceUtil.addUser(0, PortalUtil.getDefaultCompanyId(), false, password, password, false, 
						fullName+"User", email, 0L, "", themeDisplay.getLocale(), fullName, "", "", 0, 0, false,
						10, 1, 1970, "", null, null, null, null, false, new ServiceContext());
				
				Role roleProjectOwner= RoleLocalServiceUtil.fetchRole(PortalUtil.getDefaultCompanyId(), roleName);
				UserLocalServiceUtil.addRoleUser(roleProjectOwner.getRoleId(), dalradaSiteUser.getUserId());
				UserLocalServiceUtil.updateUser(dalradaSiteUser);
				
			
				Group group = GroupLocalServiceUtil.getGroup(themeDisplay.getCompanyId(), DalradaDashboardConstant.SITE_NAME_DALRADA);
				UserUtil.addGroup(dalradaSiteUser.getUserId(), group);
				
				/*--------------------------- Add User in Custom DB ---------------------------------------------*/
				UserRegistration userRegistration = null;
				
				userRegistration = UserRegistrationLocalServiceUtil.createUserRegistration(dalradaSiteUser.getUserId());
				userRegistration.setUserFullName(fullName);
				userRegistration.setUserRole(roleName);
				userRegistration.setUserEmail(email);
				userRegistration.setStatus(DalradaDashboardConstant.USER_STATUS_ACTIVE);// 1:Active 0:Inactive
				userRegistration.setCreatedBy(currentUser.getUserId());
				userRegistration.setCreatedDate(creationDate);
				
				UserRegistrationLocalServiceUtil.addUserRegistration(userRegistration);
				
				SessionMessages.add(actionRequest,"success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void activeInactiveUser(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			long userId = Long.parseLong(actionRequest.getParameter("userId"));
			
			UserRegistration editUserRegistrationData = UserRegistrationLocalServiceUtil.getUserRegistration(userId);
			User user = UserLocalServiceUtil.getUser(userId);
			
			/*--------------------------- Change Status of Active/Inactive User ---------------------------------------------*/
			
			if(editUserRegistrationData.getStatus() == DalradaDashboardConstant.USER_STATUS_ACTIVE){ // If active User
				
				User singleUser=UserLocalServiceUtil.getUser(userId);
				singleUser.setStatus(WorkflowConstants.STATUS_DENIED);
				UserLocalServiceUtil.updateUser(singleUser);
				
				editUserRegistrationData.setStatus(DalradaDashboardConstant.USER_STATUS_INACTIVE);
				
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
				
			}else if(editUserRegistrationData.getStatus() == DalradaDashboardConstant.USER_STATUS_INACTIVE){ // If inactive User
				
				User singleUser=UserLocalServiceUtil.getUser(userId);
				singleUser.setStatus(WorkflowConstants.STATUS_APPROVED);
				UserLocalServiceUtil.updateUser(singleUser);
				
				editUserRegistrationData.setStatus(DalradaDashboardConstant.USER_STATUS_ACTIVE);
				
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
			UserRegistrationLocalServiceUtil.updateUserRegistration(editUserRegistrationData);
			
		}catch(Exception e){
			
		}
	}
	
	public void editUserRegistration(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		try{
			String fullName = actionRequest.getParameter("fullName");
			String roleName = actionRequest.getParameter("roleName");
			String email = actionRequest.getParameter("email");
			long userId = Long.parseLong(actionRequest.getParameter("userId"));
			
			/*--------------------------- Check Aleady Registed User EmailId And Name  ---------------------------------------------*/
			int flag = 0;
			List<UserRegistration> userList = UserRegistrationLocalServiceUtil.getUserRegistrations(0, UserRegistrationLocalServiceUtil.getUserRegistrationsCount());
			for(UserRegistration registration : userList){
				if(registration.getUserFullName().equalsIgnoreCase(fullName)){
					if(registration.getUserID() != userId){
						flag = 1;
						break;
					}
				}
				if(registration.getUserEmail().equalsIgnoreCase(email)){
					if(registration.getUserID() != userId){
						flag = 1;
						break;
					}
				}
			}
			
			if(flag == 1){ // duplicate data 
				
				SessionErrors.add(actionRequest, "edit-error-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
			}else if(flag == 0){ // no any duplication 
			
				UserRegistration editUserRegistration = UserRegistrationLocalServiceUtil.getUserRegistration(userId);
				
				List<Role> role = RoleLocalServiceUtil.getUserRoles(userId);
				for(Role rolename : role){
					if(rolename.getName().equalsIgnoreCase(editUserRegistration.getUserRole())){
						RoleLocalServiceUtil.deleteUserRole(userId, rolename.getRoleId());
					}
				}
				
				User currentUser = UserLocalServiceUtil.fetchUser(userId);
				currentUser.setEmailAddress(email);
				currentUser.setFirstName(fullName);
				
				Role currentRole= RoleLocalServiceUtil.fetchRole(PortalUtil.getDefaultCompanyId(), roleName);
				UserLocalServiceUtil.addRoleUser(currentRole.getRoleId(), currentUser.getUserId());
				UserLocalServiceUtil.updateUser(currentUser); // update in liferay user table
				
				editUserRegistration.setUserEmail(email);
				editUserRegistration.setUserFullName(fullName);
				editUserRegistration.setUserRole(roleName);
				UserRegistrationLocalServiceUtil.updateUserRegistration(editUserRegistration); // update in custome user table
				
				SessionMessages.add(actionRequest,"edit-success-key");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
			}
		}catch(Exception e){
			
		}
	}
}
