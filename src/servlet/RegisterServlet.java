/* Author: Kulvir Gahlawat, Nitesh Dhanpal and Sagar Kalburgi
 * Group 10 Codezillas
 * Purpose: Servlet class that handles registration of new users
 * Reference: pastebin.com
 */

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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");
		String firstName = (String) request.getParameter("fname");
		String lastName = (String) request.getParameter("lname");
		String aboutme = (String) request.getParameter("aboutme");
		System.out.println("Username passed is "+ userName);
		System.out.println("Password is "+ password);
		Dao dao = new Dao();
		UserBean u=new UserBean();
		u.setUsername(userName);
		u.setPassword(password);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setAboutme(aboutme);
		dao.saveUser(u);
		response.sendRedirect("login.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
