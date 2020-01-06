<%
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="javax.portlet.ActionRequest"%>
<%@page import="javax.portlet.PortletURL"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

    <style>
        /*icons*/
        .center-block {
            float: none;
            margin-left: auto;
            margin-right: auto;
        }

        .input-group .icon-addon .form-control {
            border-radius: 0;
        }
        .icon-addon {
            position: relative;
            color: #555;
            display: block;
        }
        .icon-addon:after,
        .icon-addon:before {
            display: table;
            content: " ";
        }
        .icon-addon:after {
            clear: both;
        }
        .icon-addon.addon-md .glyphicon,
        .icon-addon .glyphicon,
        .icon-addon.addon-md .fa,
        .icon-addon .fa {
            position: absolute;
            z-index: 2;
            left: 10px;
            font-size: 14px;
            width: 20px;
            margin-left: -2.5px;
            text-align: center;
            padding: 10px 0;
            top: 1px
        }
        .icon-addon.addon-md .form-control,
        .icon-addon .form-control {
            padding-left: 30px;
            float: left;
            font-weight: normal;
        }

        .icon-addon .form-control:focus+.glyphicon,
        .icon-addon:hover .glyphicon,
        .icon-addon .form-control:focus+.fa,
        .icon-addon:hover .fa {
            color: #2580db;
        }

    </style>
    
 <liferay-ui:success key="Inactive-error-key" message="User Not Available."/> 
    
    <%
String passwordNotMatched = Validator.isNotNull((String)request.getAttribute("PASSWORD_NOT_MATCHED")) ? (String)request.getAttribute("PASSWORD_NOT_MATCHED") : StringPool.BLANK;
String emailNotExist = Validator.isNotNull((String)request.getAttribute("EMAIL_NOT_EXIST")) ? (String)request.getAttribute("EMAIL_NOT_EXIST"): StringPool.BLANK;

PortletURL loginURL = renderResponse.createActionURL();
loginURL.setParameter(ActionRequest.ACTION_NAME, "loginUser");
%>

<%
if(Validator.isNotNull(emailNotExist)){%>
 <div style="color:red;"><%=emailNotExist%></div>
<%}else if(Validator.isNotNull(passwordNotMatched)){%>
 <div style="color:red;"><%=passwordNotMatched%></div>
<%}%>

    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <div class="col-sm-2"></div>
                <div class="col-sm-8">
                    <center>
                        <h2 style="color:#337ab7;"><b>Sign In</b></h2>
                        <br>
                        <img src="/portal_content/loginUser.png" alt="Avatar" style=" vertical-align: middle; width: 150px;height: 150px;border-radius: 50%;" />
                        <br><br>

                        <form method="post" action="<%=loginURL.toString() %>" id="aui-login-form">
                            <div class="row">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
									<div class="form-group">
                                        <input type="text"  style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="phone" placeholder="Enter Username" name="<portlet:namespace/>emailId" onblur="validateEmail(this);" style="height: 40px; background-color: #e3eae8; width: 60%;"  required />
                                    </div>
                                </div>
                                <div class="col-sm-6"></div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
                                   <div class="form-group">
                                        <input type="password" style="display: block; width: 100%;height: 34px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff;background-image: none;border: 1px solid #ccc; border-radius: 4px;box-shadow: inset 0 1px 1px rgba(0,0,0,.075);transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;" id="phone" placeholder="Enter Password" name="<portlet:namespace/>password" onblur="validateEmail(this);" style="height: 40px; background-color: #e3eae8; width: 60%;"  required />
                                    </div>
                                </div>
                                <div class="col-sm-3"></div>
                            </div>

                            <div class="row">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                       <!--  <label class="checkbox-inline"><input type="checkbox"> Remember me</label>
                                        &nbsp;&nbsp;&nbsp;&nbsp; -->
                                        
										<!-- <a href="localhost:8080?p_p_id=58&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_58_struts_action=%2Flogin%2Fforgot_password" class="forgot-link" style=" color: #337ab7;">Forgot your password?</a> -->	
                                        <a href="/web/guest/home?p_p_id=58&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_58_struts_action=%2Flogin%2Fforgot_password" class="forgot-link" style=" color: #337ab7;">Forgot your password?</a>
                                    </div>
                                    <br />
                                </div>
                                <div class="col-sm-3"></div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-sm-12">
                                      <button type="submit" style="width: 200px; color: #ffffff;border-radius: 42px; height:42px;background-color:#337ab7;" >Login</button>
                                        <br /><br />

                                    </div>
                                </div>
                            </div>
                        </form>
                    </center>
                </div>
                <div class="col-sm-2"></div>
            </div>
        </div>
    </div>
