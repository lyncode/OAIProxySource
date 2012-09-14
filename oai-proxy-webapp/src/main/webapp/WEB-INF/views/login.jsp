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
		$("#submeter").button();
	});
</script>

<div id="main_container">
	<div id="header">
    	<div class="logo"></div>       
    </div>
        <div class="menu">
        	<ul>                                                                         
        		<li ><a href="/index.jsp">OAI Proxy</a></li>
				<li><a href="/admin_repositories.go">Repositories</a></li>
        	</ul>
        </div>
        
    <div class="center_content">
		
		<div class="login">
		
		
		<c:if test="${not empty error}">
			<div class="error">
				Your login attempt was not successful, try again.<br /> Caused :
				${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</div>
		</c:if>
		
		<h1>Administrator Login</h1>
		
		
		<form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
	 
			<table>
				<tr>
					<td>User:</td>
					<td><input type='text' name='j_username' value=''>
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='j_password' />
					</td>
				</tr>
				<tr>
					<td class="button" colspan='2'><input id="submeter" name="submit" type="submit"
						value="Login as Administrator" />
					</td>
				</tr>
			</table>
	 
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