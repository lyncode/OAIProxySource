<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.lyncode.xoai.serviceprovider.HarvesterManager" %>
<%@ page import="com.lyncode.xoai.serviceprovider.configuration.Configuration" %>
<%@ page import="com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException" %>
<%@ page import="com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException" %>
<%@ page import="com.lyncode.xoai.serviceprovider.verbs.Identify" %>
<%@ page import="org.apache.log4j.LogManager" %>
<%@ page import="org.apache.log4j.Logger" %>

<% 
	boolean error = false;
	String message = "";
	
	Logger log = LogManager.getLogger(BadArgumentException.class);
	
	Configuration c = new Configuration();
	c.setResumptionInterval(1000);
	HarvesterManager h = new HarvesterManager(c, request.getParameter("url"));
	Identify id = null;
	try {
		id = h.identify();
	} catch (BadArgumentException e) {
		error = true;
		message = e.getMessage();
		log.debug(e.getMessage(), e);
	} catch (InternalHarvestException e) {
		error = true;
		message = e.getMessage();
		log.debug(e.getMessage(), e);
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>OAI Proxy</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<link type="text/css" href="jquery/css/custom-theme/jquery-ui-1.8.23.custom.css" rel="stylesheet" />
<script type="text/javascript" src="jquery/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="jquery/js/jquery-ui-1.8.23.custom.min.js"></script>
</head>
<body>

<script>
	$(function() {
		$('#baseurl').focusin(function() {
			if ($(this).val() == "Set (Blank if none)") {
				$(this).val("");
				$(this).css('color', '#333333');
			}
		});
		$('#baseurl').focusout(function() {
			if ($(this).val() == "") {
				$(this).val("Set (Blank if none)");
				$(this).css('color', '#CCCCCC');
			}
		});
		$("#submeter").button();
	});
</script>

<div id="main_container">
	<div id="header">
    	<div class="logo"></div>       
    </div>
        <div class="menu">
        	<ul>                                                                         
        		<li ><a href="/">OAI Proxy</a></li>
				<li class="selected"><a href="/admin_repositories.jsp">Admin</a></li>
        	</ul>
        </div>
        
    <div class="center_content">
		
		<div class="add_repository">
		
		
		
		<% if (error) { %>
		<h1 style="text-align: center; color: red;">Error</h1>
		<div style="padding: 30px;"><%=message%></div>
		<% } else { %>
		<h1 style="text-align: center;"><%=id.getRepositoryName()%></h1>
		
		<form method="post" action="/admin_add_final">
			<input type="hidden" name="url" value="<%=request.getParameter("url") %>" />
			<input type="hidden" name="name" value="<%=id.getRepositoryName() %>" />
			
			<div class="field">
				<div>
					<input id="baseurl" class="nofocus" type="text" name="set" value="Set (Blank if none)" />
				</div>
			</div>
			<div class="field">
				<div class="checkbox">
					<label>Active</label><input id="activecheck" class="checkbox" type="checkbox" name="active" value="ok" />
				</div>
			</div>
			<div class="button">
				<input type="submit" id="submeter" value="Add Repository : Finish" />
			</div>
		</form>
		</div>
		
		<% } %>
		
        <div class="clear"></div> 
    </div>    

    
    <div id="footer">                                              
        <div class="left_footer"><a href="http://www.lyncode.com">Lyncode Website</a> <a href="http://proxy.oai-pmh.net">OAI Proxy Website</a> <a href="mailto:general@lyncode.com">Contact</a></div>
        </div>   
    
    </div>
    
    
    
</div>
<!-- end of main_container -->

</body>
</html>