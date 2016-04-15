package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;
import dao.Dao;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(description = "To register new users", urlPatterns = { "/RegisterServlet" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		System.out.println("Username passed is "+ userName);
		System.out.println("Password is "+ password);
		Dao dao = new Dao();
		UserBean u=new UserBean();
		u.setUsername(userName);
		u.setPassword(password);
		dao.saveUser(u);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
