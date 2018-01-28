package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.RecommendationService;
import vo.MovieRating;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "Servlet class for login of a user", urlPatterns = { "/RecommendationServlet" })
public class RecommendationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		if(request.getSession() != null && request.getSession().getAttribute("userName") != null) {
			String userName = request.getSession().getAttribute("userName").toString();
			RecommendationService recService = new RecommendationService();		
			List<MovieRating> movieRatingList = recService.fetchAllMoviesList();		
			List<MovieRating> usermovieList = recService.fetchUserMoviesList(movieRatingList, userName);		
			request.setAttribute("moviesRatingList", movieRatingList); 		
			request.setAttribute("recommendedMovieList", usermovieList); 
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("/pages/login.jsp");
		    rd.include(request, response);
		}
	}

}
