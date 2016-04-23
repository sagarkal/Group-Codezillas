package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.LoginBean;
import bean.QuestionBean;
import dao.Dao;

public class LoginServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    String result="false";
		String userId = (String) request.getParameter("inputEmail");
		String password = (String) request.getParameter("inputPassword");
		System.out.println("In Login Servlet....");
		System.out.println("Entered user name is "+ userId);
		System.out.println("Entered password is "+password);
		Dao dao = new Dao();
		LoginBean lBean = new LoginBean();
		lBean.setUserId(userId);
		lBean.setPassword(password);
		if(dao.userLogin(lBean))
			response.sendRedirect("home.html?usernameg="+userId.split("@")[0]);
	}

}