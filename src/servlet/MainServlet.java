package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;

import bean.AnswerBean;
import bean.QuestionBean;
import bean.UserBean;

import com.google.gson.Gson;

import dao.Dao;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = (String) request.getParameter("type");
		Dao dao = new Dao();
		if(type.equals("getquestions")){
			System.out.println("Entered Main Servlet to fetch questions....");
			ArrayList<QuestionBean> al = dao.getQuestions();
			String json = new Gson().toJson(al);
			System.out.println("Exiting Main Servlet after fetching questions....");
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getFeedback")){
			ArrayList<UserBean> al = new ArrayList<UserBean>();
			UserBean u=new UserBean();
			String username = (String) request.getParameter("username");
			al = dao.getFeedback(username);
			String json = new Gson().toJson(al);
			response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getanswers")){
			long qid = Long.parseLong(request.getParameter("qid"));
			ArrayList<AnswerBean> al = dao.getAnswers(qid);
			String json = new Gson().toJson(al);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getrep")){
			String username = (String) request.getParameter("username");
			UserBean u = dao.getReputation(username);
			String json = new Gson().toJson(u);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("updaterep")){
			String username = (String) request.getParameter("username");
			String language = (String) request.getParameter("language");
			int pointsToAdd = Integer.parseInt(request.getParameter("pointsToAdd"));
			System.out.println("In Mainservlet Updaterep, about to update "+ username + language + pointsToAdd);
			int updateSuccess = dao.updateReputation(username, language, pointsToAdd);
			if(updateSuccess == 1)
			{
			response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(String.valueOf(pointsToAdd));
		    
		    System.out.println("---------------------------------------------");
		    System.out.println("Points to be added in updateReputation: "+pointsToAdd);
		    System.out.println("Response object: "+response.toString());
		    System.out.println("---------------------------------------------");
			}
			else
			{
				response.setContentType("text/plain");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(String.valueOf(0));
			    System.out.println("---------------------------------------------");
			    System.out.println("Response object: "+response.toString());
			    System.out.println("---------------------------------------------");
			}
		}
		
		if(type.equals("saveanswer")){
			AnswerBean a = new AnswerBean();
			a.setUsername((String) request.getParameter("username"));
			a.setAnswer((String) request.getParameter("answer"));
			a.setUpvotes(0);
			a.setDownvotes(0);
			a.setQuestionId(Integer.parseInt(request.getParameter("qid")));
			long aid  = dao.saveAnswer(a);
			String json = new Gson().toJson(aid);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);

		}
		
		if(type.equals("upvote")){
			AnswerBean a = new AnswerBean();
			int ups = Integer.parseInt(request.getParameter("up"));
			int id =  Integer.parseInt(request.getParameter("id"));
			System.out.println("Upvotes" + ups);
			a.setUpvotes(ups);
			dao.updateUpVote(a);
		}
		
		if(type.equals("downvote")){
			AnswerBean a = new AnswerBean();
			int downs = Integer.parseInt(request.getParameter("down"));
			int id =  Integer.parseInt(request.getParameter("id"));
			System.out.println("Downvotes" + downs);
			a.setDownvotes(downs);
			dao.updateDownVote(a);
		}
		
		if(type.equals("feedback")){
			int accuracy = Integer.parseInt(request.getParameter("accuracy"));
			int conciseness = Integer.parseInt(request.getParameter("conciseness"));
			int redundancy = Integer.parseInt(request.getParameter("redundancy"));
			int grammar = Integer.parseInt(request.getParameter("grammar"));
			int id = Integer.parseInt(request.getParameter("id"));
			dao.updateFeedback(accuracy, conciseness, redundancy, grammar, id);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
