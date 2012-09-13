<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.lyncode.oai.proxy.core.RepositoryManager" %>
<%@ page import="com.lyncode.oai.proxy.xml.repository.Repository" %>
<%@ page import="java.util.List" %>

<%

String op = request.getParameter("op");
String id = request.getParameter("id");
if (op != null && id != null) {
	if (op.equals("activate")) {
		Repository r = RepositoryManager.getByID(id);
		r.setActive(true);
		RepositoryManager.save(r);
	} else if (op.equals("deactivate")) {
		Repository r = RepositoryManager.getByID(id);
		r.setActive(false);
		RepositoryManager.save(r);
	} else if (op.equals("delete")){
		Repository r = RepositoryManager.getByID(id);
		RepositoryManager.delete(r);
	}
}

List<Repository> rep = RepositoryManager.getRepositories();

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
        		<li ><a href="/">OAI Proxy</a></li>
				<li class="selected"><a href="/admin_repositories.jsp">Admin</a></li>
        	</ul>
        </div>
        
    <div class="center_content">
		
		<div class="add_repository">
		<form method="post" action="/admin_add.jsp">
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
			<h1>Repositories</h1>
			
			<% if (rep.isEmpty()) { %>
			<div style="padding: 30px;">No repositories</div>
			<% } else { %>
			<ul>
				<% for (Repository r : rep) { %>
				<li>
					<div class="data">
						<div class="name"><%=r.getName() %></div>
						<div class="url"><label>URL</label><span><%=r.getURL() %></span></div>
						<% if (r.getSet() == null) { %>
						<div class="set"><label>Set choosen</label><span>None</span></div>
						<% } else { %>
						<div class="set"><label>Set choosen</label><span><%=r.getSet() %></span></div>
						<% } %>
						<div class="total"><label>Indexed Items</label><span><%=RepositoryManager.harvestItems(r) %></span></div>
						<div class="last"><label>Last Harvest</label><span><%=r.getLastHarvest() %></span></div>
					</div>
					<div class="operations">
						<% if (!r.isActive()) { %>
						<a href="/admin_repositories.jsp?op=activate&amp;id=<%=r.getID()%>">Activate</a>
						<% } else { %>
						<a href="/admin_repositories.jsp?op=deactivate&amp;id=<%=r.getID()%>">Deactivate</a>
						<% } %>
						<a href="/admin_repositories.jsp?op=delete&amp;id=<%=r.getID()%>">Delete</a>
					</div>
				</li>
				<% } %>
			</ul>
			<% } %>
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