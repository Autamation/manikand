<%@page import="com.prakat.dashboard.application.service.RoleRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.RoleRegistration"%>
<%@page import="com.liferay.portal.service.RoleLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
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
        
        
    </style>
   
<% 
long roleId = Long.parseLong(request.getParameter("roleId"));
RoleRegistration editeRoleRegistration = RoleRegistrationLocalServiceUtil.getRoleRegistration(roleId);
%> 
<div class="container" style="    margin-left: -96px;">   
<div class="row">
	<div class="col-sm-12">
    	<div class="col-sm-1"></div>
        <div class="col-sm-10">
        	<portlet:actionURL var="eitRoleRegistration" name="eitRoleRegistration"></portlet:actionURL>
            <form action="<%= eitRoleRegistration %>" method="post">
            	<input value="<%= editeRoleRegistration.getRoleID() %>" type="hidden" name="<portlet:namespace/>roleId" >
            	<div class="panel panel-default">
                	<div class="panel-heading">
                      <h4><b>Edit Role</b></h4>
                  	</div>
                   	<div class="panel-body">
                       <div class="col-sm-5">
                               <div class="form-group">
                                   <label for="role_name" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Edit Role Name:</label>
                                   <input pattern="[A-Za-z ]+$" title="Format: Letters only" value="<%= editeRoleRegistration.getRoleName() %>" type="name" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name" placeholder="Enter Role Name" name="<portlet:namespace/>eiteRoleName" onblur="validateEmail(this);" required>
                               </div>
                       </div>
                       <div class="col-sm-1">
                       </div>
                       <div class="col-sm-5">
                       </div>
                       <div class="col-sm-1"></div>
                   	</div>
                  	<div class="panel-footer">
                       <div class="row">
                           <div class="col-sm-12">
                               <div class="col-sm-8"></div>
                               <div class="col-sm-4" style="margin-left:704px;">

                                   <button type="submit" onclick="<%= eitRoleRegistration %>" class="btn btn-md btn-primary"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Update</button>
                                   <button type="button" onclick="window.history.back();" class="btn btn-md btn-danger"><span class="	glyphicon glyphicon-remove"></span>&nbsp;Cancel</button>
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
</div>