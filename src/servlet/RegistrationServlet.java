package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.RegistrationService;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet(description = "Servlet class for registration of a user", urlPatterns = { "/RegistrationServlet" })
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
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
		//doGet(request, response);
		
		String userName = (request.getParameter("userName").trim()).toLowerCase();
		String email = request.getParameter("email").trim();
		String password = request.getParameter("password").trim();
		RegistrationService regService = new RegistrationService();
		 
		String status = regService.registerUser(userName, email, password);
		if(status.equalsIgnoreCase("Success")) {
			request.setAttribute("resSuccess", userName+" registered successfully, you can login now."); // Set error.
		    request.getRequestDispatcher("/pages/registration.jsp").forward(request, response); 
		}else {
			request.setAttribute("resError", status); // Set error.
		    request.getRequestDispatcher("/pages/registration.jsp").forward(request, response); 
		}		
	}

}
