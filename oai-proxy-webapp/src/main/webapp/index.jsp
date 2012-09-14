<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>OAI Proxy</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>

	<div id="main_container">
		<div id="header">
			<div class="logo"></div>
		</div>
		<div class="menu">
			<ul>
				<li class="selected"><a href="/">OAI Proxy</a></li>
				<li><a href="/admin_repositories.go">Repositories</a></li>
			</ul>
		</div>

		<div class="center_content">
			<div style="text-align: center; padding: 20px;">
			<span style="display: block;
			    font-size: 15px;
			    border: solid 1px #CCC;
			    background-color: rgba(111,111,111,0.1);
			    padding: 20px;
			    font-weight: bold;">
			    <%=request.getRequestURL().toString().replaceFirst(request.getRequestURI(),"")%>/oai/request</span>
			</div>
			<div class="center_left">
				<div class="welcome_box">
                                        <p class="welcome">
                                                <span class="orange">OAI Proxy URL</span><br /> 
                                                <span style="display: block; text-align: center; font-size: 15px;font-weight: bold; padding-top: 40px; padding-bottom: 40px;">
                                                	<%=request.getRequestURL().toString().replaceFirst(request.getRequestURI(),"")%>/oai/request
                                                </span>
                                                Developed by <a href="http://www.lyncode.com">Lyncode</a>.
                                        </p>
                                </div>



			</div>



			<div class="clear"></div>

		</div>


		<div id="footer">
			<div class="left_footer">
				<a href="http://www.lyncode.com">Lyncode Website</a> <a
					href="http://proxy.oai-pmh.net">OAI Proxy Website</a> <a
					href="mailto:general@lyncode.com">Contact</a>
			</div>
		</div>

	</div>



	</div>
	<!-- end of main_container -->

</body>
</html>