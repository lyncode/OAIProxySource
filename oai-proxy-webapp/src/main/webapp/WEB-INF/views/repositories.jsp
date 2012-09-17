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
			if ($(this).val() == "Base OAI URL") {
				$(this).val("");
				$(this).css('color', '#333333');
			}
		});
		$('#baseurl').focusout(function() {
			if ($(this).val() == "") {
				$(this).val("Base OAI URL");
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
		
		<form method="post" action="/admin_repositories_add.go">
			<div class="field">
				<div>
					<input id="baseurl" class="nofocus" type="text" name="url" value="Base OAI URL" />
				</div>
			</div>
			<div class="button">
				<input type="submit" id="submeter" value="Add Repository" />
			</div>
		</form>
		</div>
		
        <div class="clear"></div> 
    
		<div class="separator"></div>
		
		<div class="list">
                        <h1 style="float: left;">Repositories</h1>
                        
                        <div style="float: right;padding: 25px 30px;">
                        	<label style="font-weight: bold; margin-right: 10px;">Next Harvest</label>
                        	<span>${next}</span>
                        	[<a href="/admin_run_harvest.go">Run Now</a>]
                        </div>
                        
                        <div style="clear: both;"></div>

                        <c:if test="${empty repositories}">
                        <div style="padding: 30px;">No repositories</div>
                        </c:if>

                        <c:if test="${not empty repositories}">
                                <ul>
                                <c:forEach var="rep" items="${repositories}">
                                <li>
                                        <div class="data">
                                                <div class="name">${rep.name}</div>
                                                <div class="url"><label>URL</label><span>${rep.URL}</span></div>
                                                <c:if test="${not empty rep.set}">
                                                <div class="set"><label>Set chosen</label><span>${rep.set}</span></div>
                                                </c:if>
                                                <c:if test="${empty rep.set}">
                                                <div class="set"><label>Set chosen</label><span>None</span></div>
                                                </c:if>
                                                <div class="total"><label>Indexed Items</label><span>${rep.count}</span></div>
                                                <div class="last"><label>Last Harvest</label><span>${rep.lastHarvest}</span></div>
                                        </div>
                                        <div class="operations">
                                                <c:if test="${not rep.active}">
                                                <a href="/admin_repositories_activate.go?id=${rep.ID}">Activate</a>
                                                </c:if>
                                                <c:if test="${rep.active}">
                                                <a href="/admin_repositories_deactivate.go?id=${rep.ID}">Deactivate</a>
                                                </c:if>
                                                <a href="/admin_repositories_reset.go?id=${rep.ID}">Reset</a>
                                                <a href="/admin_repositories_del.go?id=${rep.ID}">Delete</a>
                                        </div>
                                </li>
                                </c:forEach>
                        </ul>
                        </c:if>
                </div>
                
		</div>


    
    <div id="footer">                                              
        <div class="left_footer"><a href="http://www.lyncode.com">Lyncode Website</a> <a href="http://proxy.oai-pmh.net">OAI Proxy Website</a> <a href="mailto:general@lyncode.com">Contact</a></div>
        </div>   
    
    </div>
    
    
    
</div>
<!-- end of main_container -->

</body>
</html>