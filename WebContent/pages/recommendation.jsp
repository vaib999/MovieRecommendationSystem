<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<meta charset='utf-8'>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Movie Recommendation System</title>
	<link href="/movies/css/style.css" rel="stylesheet">
	<link href="/movies/css/bootstrap.min.css" rel="stylesheet">
	<script src="/movies/js/bootstrap.min.js"></script>
	<jsp:include page="/RecommendationServlet"></jsp:include>
	<script type="text/javascript">
		function resetMovie()
		{
			var dropdownList = document.getElementById('movieListDrop');
			var tableObj = document.getElementById('selectedMovieId');
			var movieStr = dropdownList.options[dropdownList.selectedIndex].value;
			if(movieStr==""){
				var row=tableObj.rows[0];
				var cells=row.cells;
			    cells[0].innerHTML="";
			    cells[1].innerHTML="";
			    cells[2].innerHTML="";
			}else{
				var res=movieStr.split(":");
				var row=tableObj.rows[0];
				var cells=row.cells;
			    cells[0].innerHTML=res[0];
			    cells[1].innerHTML=res[1];
			    cells[2].innerHTML=res[2];
			}
		}
	</script>	
</head>
<body id="bodyNew">
	<header> 
		<div class="container-fluid" style="min-height: 70px;">
			<div id ="branding" align="center">
				<h1>Movie Recommendation System</h1>
			</div>
			<nav style="float:right;">
				<ul>
					<li> <a href="/movies/pages/userMovieRating.jsp"> user Movie Rating </a> </li>
					<li> <a href="/movies/pages/logout.jsp"> Logout </a> </li>
				</ul>
			</nav>
		</div>
		<table>
			<tr >
				<td  style="color:#ffffff;" align="left">
					Welcome <%= session.getAttribute("userName") %>
				</td>
			</tr>
		</table>
	</header>

	<section id="showcase">
		<div  class="container-fluid">
			The one stop for the movies you LOVE!!
		</div>
	</section>
	
	<form name="recommendationForm" method="get" action="/movies/RecommendationServlet">
		<section id="search" style="background-color:#35424a; border-bottom: #e8491d 5px solid;">
			<h2>Search Movie</h2>
			<div class="form-group blu-margin">
			    <select class="form-control"   id="movieListDrop" name="movieListDrop" onchange="resetMovie();">
				    <option value="" selected="selected">--Select--</option>
		 			<c:forEach var="allMovieObj" items="${moviesRatingList}">
				    	<option value="${allMovieObj.movieName}:${allMovieObj.rating}:${allMovieObj.numUsers}">${allMovieObj.movieName}</option>
				    </c:forEach>
				</select>
			    <table id="selectedMovieId" class="table" style="color:#ffffff;">
			    	<tr> <td></td> <td></td> <td></td> </tr>
			    </table>
			</div>
			
		</section>
		
		<p><font color="#ff0000"><c:out value="${userMsg}"/></font></p>
		
		<section id="recommendation" style="margin-top: 50px; background-color:#35424a; color:#ffffff;">
			<p style="color: green">${resSuccess}</p>
			<p style="color: red">${resError}</p>
			<h3 align="center"> Movies you would like </h3>
			<div style="min-height: 200px; max-height: 300px; overflow-y:scroll;">
				<table border="0" class="table" style="color:#ffffff;">
					<thead align="center">				
					  	<tr>
					     	<th>Movie Name</th>
					     	<th>Rating</th>
					     	<th>Number Of Users</th>
					    </tr>
					</thead>
	 				<c:forEach var="movieObj" items="${recommendedMovieList}">
	 				<tbody align="center">
						<tr>
							<td><c:out value="${movieObj.movieName}" /></td>
							<td><c:out value="${movieObj.rating}" /></td>
							<td><c:out value="${movieObj.numUsers}" /></td>
						</tr>
						</tbody>
					</c:forEach>
				</table>
			</div>
		</section>
	</form>
	
	
	<footer>
		<p>Movie Recommendation System, Copyright &copy; 2017 </p>
	</footer>
</body>
</html>