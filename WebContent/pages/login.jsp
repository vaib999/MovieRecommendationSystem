<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<meta charset='utf-8'>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Movie Recommendation System</title>
	<link href="/movies/css/bootstrap.min.css" rel="stylesheet">
	<link href="/movies/css/style.css" rel="stylesheet">
	<script src="/movies/js/bootstrap.min.js"></script>
</head>
<body>
<script type="text/javascript">
	function switchToRegistrationPage(){
		location.href = '/movies/pages/registration.jsp';
	}
</script>
	<header> 
		<div class="container-fluid" align="center">
			<h1>Movie Recommendation System</h1>
		</div>
	</header>

	<section id="showcase">
		<div  class="container-fluid">
			The one stop for the movies you LOVE!!
		</div>
	</section>
	
	<section id="login">
		<h2>Login Form</h2>
	</section>
	
	<p><font color="#ff0000"><c:out value="${userMsg}"/></font></p>
	
	<section id="login-form">
		<form name="loginForm" method="post" action="/movies/LoginServlet">
			<p style="color: green">${resSuccess}</p>
			<p style="color: red">${resError}</p>
			<table border="0" class="table">
				<tbody>
					<tr>
						<td class='blue'>Username*</td>
						<td><input type="text" class="form-control" required="true" placeholder="Enter user name" name="userName" value ="" size="20"></td>
					</tr>
					<tr>
						<td class='blue'>Password*</td>
						<td><input type="password" class="form-control" required="true" placeholder="Set password" name="password" value ="" size="20"></td>
					</tr>
				</tbody>
			</table>
			<div align="center" style="height:100px;">
				<button type="submit" class="button_2">Login</button>	<button type="submit" class="button_2" onclick="switchToRegistrationPage();">Register</button>
			</div>	
		</form>
	</section>
	<footer>
		<p>Movie Recommendation System, Copyright &copy; 2017 </p>
	</footer>
</body>
</html>