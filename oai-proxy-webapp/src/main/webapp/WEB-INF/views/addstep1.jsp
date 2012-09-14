<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        		<li><a href="/index.jsp">OAI Proxy</a></li>
				<li><a href="/admin_repositories.go">Repositories</a></li>
        	</ul>
        	<ul class="right">
				<li><a href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
			</ul>
        </div>
        
    <div class="center_content">
		
		<div class="add_repository">
		
		
		<c:if test="${error}">
		<div class="error">
			${message}
		</div>
		</c:if>
		
		<h1 style="text-align: center;">${name}</h1>
		
		<form method="post" action="/admin_repositories_add_final.go">
			<input type="hidden" name="url" value="${url}" />
			<input type="hidden" name="name" value="${name}" />
			
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