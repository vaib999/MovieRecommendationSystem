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
	function switchToLoginPage(){
		location.href = '/movies/pages/login.jsp';
	}
</script>
	<header> 
		<div class="container-fluid" align="center">
			<h1>Movie Recommendation System</h1>
		</div>
	</header>

	<section id="showcase">
		<div  class="container-fluid">
			Movies Just For You !!
		</div>
	</section>
	
	<section id="registration">
		<h2>Registration Form</h2>
	</section>
	
	<p><font color="#ff0000"><c:out value="${userMsg}"/></font></p>
	
	<section id="registration-form">
		<form name="registrationForm" method="post" action="/movies/RegistrationServlet">
			<p style="color: green">${resSuccess}</p>
			<p style="color: red">${resError}</p>
			<table border="0" class="table">
				<tbody>
					<tr>
						<td class='blue'>Username*</td>
						<td><input type="text" class="form-control" required="true" placeholder="Enter user name" name="userName" value ="" size="20"></td>
					</tr>
					<tr>
						<td class='blue'>Email id*</td>
						<td><input type="email" class="form-control" required="true"  placeholder="Enter email" name="email" value ="" size="20"></td>
					</tr>
					<tr>
						<td class='blue'>Password*</td>
						<td><input type="password" class="form-control" required="true" placeholder="Set password" name="password" value ="" size="20"></td>
					</tr>
				</tbody>
			</table>
			<div align="center" style="height:50px;">
				<button type="submit" class="button_1">Register</button> <button type="submit" class="button_1" onclick="switchToLoginPage();">Login</button>
			</div>		
		</form>
	</section>
	<footer>
		<p>Movie Recommendation System, Copyright &copy; 2017 </p>
	</footer>
</body>
</html>