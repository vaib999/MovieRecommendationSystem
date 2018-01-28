<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<meta charset='utf-8'>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Movie Recommendation System</title>
	<link href="/movies/css/style.css" rel="stylesheet">
	<link href="/movies/css/bootstrap.min.css" rel="stylesheet">
	<script src="/movies/js/bootstrap.min.js"></script>
	<jsp:include page="/UserMovieRatingServlet"></jsp:include>
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
			    document.getElementById('submit').disabled=true;
			}else{
				var row=tableObj.rows[0];
				var cells=row.cells;
			    cells[0].innerHTML=movieStr;
			    
			    while (cells[1].hasChildNodes()) {
			    	cells[1].removeChild(cells[1].lastChild);
			    }
			    
			    var selectList = document.createElement("select");
			    selectList.id = "ratingList";
			    selectList.name = "ratingList";

			    //Create and append the options
			    for (var i = 1; i <= 10; i++) {
			        var option = document.createElement("option");
			        option.value = i;
			        option.text = i;
			        selectList.appendChild(option);
			    }
			    cells[1].appendChild(selectList);
			    document.getElementById('submit').disabled=false;
			}
		}
	</script>	
</head>
<body id="bodyNew">
<%
String userName = session.getAttribute("userName")==null?"":session.getAttribute("userName").toString();
String unixCommand = "sh ../../src/scala/start-scala.sh "+ userName;
Runtime rt = Runtime.getRuntime();
rt.exec(unixCommand);
%>
	<header> 
		<div class="container-fluid" style="min-height: 70px;">
			<div id ="branding" align="center">
				<h1>Movie Recommendation System</h1>
			</div>
			<nav style="float:right;">
				<ul>
					<li> <a href="/movies/pages/recommendation.jsp"> Movie Recommendation </a> </li>
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
	
	<form name="userMovieRateForm" method="get" action="/movies/UserMovieRatingServlet">
		<section id="recommendMovie" style="background-color:#35424a; border-bottom: #e8491d 5px solid; min-height: 300px;">
			
			<p style="background-color:#35424a; color:#ffffff;"><c:out value="${userMessageRequest}"/></p>
			
			<h2>Recommend A Movie</h2>
			<div class="form-group blu-margin">
			    <select class="form-control"   id="movieListDrop" name="movieListDrop" onchange="resetMovie();">
				    <option value="" selected="selected">--Select--</option>
		 			<c:forEach var="allMovieObj" items="${moviesRatingList}">
				    	<option value="${allMovieObj.movieName}">${allMovieObj.movieName}</option>
				    </c:forEach>
				</select>
			    <table id="selectedMovieId" class="table" style="color:#ffffff;">
			    	<tr> <td></td> <td></td></tr>
			    </table>
			</div>
			<div align="center" style="height:50px;">
				<button id="submit" type="submit" class="button_1" disabled="disabled">Submit</button>
			</div>				
			
		</section>
	</form>
	
	
	<footer>
		<p>Movie Recommendation System, Copyright &copy; 2017 </p>
	</footer>
</body>
</html>
