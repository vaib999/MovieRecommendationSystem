package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.LoginService;
import service.RecommendationService;
import vo.MovieRating;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "Servlet class for login of a user", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String userName = (request.getParameter("userName").trim()).toLowerCase();
		String password = request.getParameter("password").trim();
		LoginService loginService = new LoginService();
		 
		String status = loginService.loginUser(userName, password);
		if(status.equalsIgnoreCase("Success")) {
			HttpSession session = request.getSession();
			session.setAttribute("userName", userName);
			response.sendRedirect("/movies/pages/recommendation.jsp");
		}else {
			request.setAttribute("resError", status); // Set error.
		    request.getRequestDispatcher("/pages/login.jsp").include(request, response);
		}	
	}

}
