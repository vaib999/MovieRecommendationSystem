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
import service.UserMovieRatingService;
import vo.MovieRating;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "Servlet class for login of a user", urlPatterns = { "/UserMovieRatingServlet" })
public class UserMovieRatingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserMovieRatingServlet() {
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
			if(request.getSession().getAttribute("userMsg") != null) {		
				request.setAttribute("userMessageRequest", request.getSession().getAttribute("userMsg")); 
				request.getSession().setAttribute("userMsg", null);
			}
			RecommendationService recService = new RecommendationService();		
			List<MovieRating> movieRatingList = recService.fetchAllMoviesList();		
			request.setAttribute("moviesRatingList", movieRatingList); 
			if(request.getParameter("movieListDrop") != null && request.getParameter("ratingList") != null ) {
				UserMovieRatingService userMovService = new UserMovieRatingService();
				String message = userMovService.updateUserMovieRating(request.getSession().getAttribute("userName").toString(), 
														request.getParameter("movieListDrop"), request.getParameter("ratingList"));
				request.getSession().setAttribute("userMsg", message);
				response.sendRedirect("/movies/pages/userMovieRating.jsp");
			}
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("/pages/login.jsp");
		    rd.include(request, response);
		}
	}

}
