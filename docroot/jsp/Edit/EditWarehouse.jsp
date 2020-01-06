<%@page import="com.prakat.dashboard.application.service.WarehouseRegistrationLocalServiceUtil"%>
<%@page import="com.prakat.dashboard.application.model.WarehouseRegistration"%>
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
    
 <script type="text/javascript">
 
 $(document).on('input','#warehouseCode',function(){
	    var warehouseCode = $('#warehouseCode').val();
	   if(warehouseCode.indexOf('0')==0){
	     alert('First digit should not be 0.lease Enter valide Code.');
	     $('#warehouseCode').val('');
	   }
	});
 
    </script>
        
<% 
long warehouseId = Long.parseLong(request.getParameter("warehouseId"));
WarehouseRegistration editeRoleRegistration = WarehouseRegistrationLocalServiceUtil.getWarehouseRegistration(warehouseId);
%> 
 <div class="container" style="margin-left: -96px;">
        <div class="row">
            <div class="col-sm-12">
                <div class="col-sm-1"></div>
                <div class="col-sm-10">
					<portlet:actionURL var="editWarehouseRegistration" name="editWarehouseRegistration"></portlet:actionURL>
                    <form action="<%= editWarehouseRegistration %>" method="post">
                    	<input type="hidden" value="<%= editeRoleRegistration.getWarehouseID() %>" name="<portlet:namespace/>warehouseId" >
                    	<div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4><b>Edit Warehouse</b></h4>
	                        </div>
	                        <div class="panel-body">
	                            <div class="col-sm-5">
	                                    <div class="form-group">
	                                        <label for="full_name" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Warehouse Name:</label>
	                                        <input title="Format: Letter, Digit , Length(2-35) "  pattern="^[a-zA-Z][a-zA-Z0-9-_ \.]{1,35}$" value="<%= editeRoleRegistration.getWarehouseName() %>" type="text" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name" placeholder="Enter Warehouse Name" name="<portlet:namespace/>eiteWarehouseName" required>
	                                    </div>
	                                    <div class="form-group">
	                                        <label for="full_name" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Warehouse Code:</label>
	                                        <input maxlength="10"  minlength="5"  pattern="^[0-9]*$" title="Format: numeric Only ,Length(5-10)" value="<%= editeRoleRegistration.getWarehouseCode() %>" type="text" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name" placeholder="Enter Warehouse Code" name="<portlet:namespace/>warehouseCode" onblur="validateEmail(this);" required></textarea>
	                                    </div>
	
	
	                                
	                            </div>
	                            <div class="col-sm-1"></div>
	                            <div class="col-sm-5">
	                                    <div class="form-group">
	                                        <label for="full_name" style="display: inline-block;max-width: 100%;margin-bottom: 5px;font-weight: 700;">Warehouse Address:</label>
	                                        <%-- <textarea  value="" rows="4" cols="50" name="<portlet:namespace/>address" style="display: block; padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name" placeholder="Enter Warehouse Address"></textarea> --%>
	                                        <%-- <aui:input value="<%= editeRoleRegistration.getWarehouse_Address() %>" name="editwarehouseAddress" type="textarea"></aui:input> --%>
	                                        <textarea required rows="4" cols="50" name="<portlet:namespace/>warehouseAddress" style="display: block; width: 100%;height: 111px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="full_name"><%= editeRoleRegistration.getWarehouseAddress() %></textarea>
	                                    </div>
	                            </div>
	                            
	                            <div class="col-sm-1"></div>
	
	                        </div>


	                        <div class="panel-footer">
	                            <div class="row">
	                                <div class="col-sm-12">
	                                    <div class="col-sm-8"></div>
	                                    <div class="col-sm-4" style="margin-left:704px;">
	
	                                        <button type="submit" onclick="<%= editWarehouseRegistration %>" class="btn btn-md btn-primary"><span class="glyphicon glyphicon-floppy-saved"></span>&nbsp;Update</button>
	                                        <button type="button" onclick="window.history.back();" class="btn btn-md btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;Cancel</button>
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