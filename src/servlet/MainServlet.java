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
import bean.CommentBean;
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
			ArrayList<QuestionBean> al = dao.getQuestions();
			String json = new Gson().toJson(al);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getUserDetails")){
			String username = (String) request.getParameter("username");
			UserBean u = dao.getUserDetails(username);
			String json = new Gson().toJson(u);
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
		
		if(type.equals("getFeedbackComments")){
			ArrayList<CommentBean> cl = new ArrayList<CommentBean>();
			String username = (String) request.getParameter("username");
			cl = dao.getFeedbackComments(username);
			String json = new Gson().toJson(cl);
			response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getanswers")){
			int qid = Integer.parseInt(request.getParameter("qid"));
			ArrayList<AnswerBean> al = dao.getAnswers(qid);
			String json = new Gson().toJson(al);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
		
		if(type.equals("getrepfortopfive")){
			ArrayList<UserBean> ul = new ArrayList<UserBean>();
			ul = dao.getReputationForTopFiveUsers();
			String json = new Gson().toJson(ul);
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

		if(type.equals("getrepforprofile")){
			String username = (String) request.getParameter("username");
			ArrayList<UserBean> ul = new ArrayList<UserBean>();
			ul = dao.getReputationForProfile(username);
			String json = new Gson().toJson(ul);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}

		if(type.equals("updaterep")){
			String username = (String) request.getParameter("username");
			String language = (String) request.getParameter("language");
			double pointsToAdd = Double.parseDouble(request.getParameter("pointsToAdd"));
			int updateSuccess = dao.updateReputation(username, language, pointsToAdd);
			if(updateSuccess == 1)
			{
			response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(String.valueOf(pointsToAdd));

			}
			else
			{
				response.setContentType("text/plain");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(String.valueOf(0));
			}
		}

		if(type.equals("saveanswer")){
			AnswerBean a = new AnswerBean();
			a.setUsername((String) request.getParameter("username"));
			a.setAnswer((String) request.getParameter("answer"));
			a.setUpvotes(0);
			a.setDownvotes(0);
			a.setQuestionId(Integer.parseInt(request.getParameter("qid")));
			int aid  = dao.saveAnswer(a);
			String json = new Gson().toJson(aid);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}

		if(type.equals("savequestion")){
			QuestionBean a = new QuestionBean();
			a.setUsername((String) request.getParameter("username"));
			a.setQuestion((String) request.getParameter("question"));
			a.setTags((String) request.getParameter("tag"));
			a.setUpvotes(0);
			a.setDownvotes(0);
			a.setId(Integer.parseInt(request.getParameter("qid")));
			dao.saveQuestion(a);
			ArrayList<QuestionBean> al = dao.getQuestions();
			String json = new Gson().toJson(al);
		    response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}

		if(type.equals("upvote")){
			int ups = Integer.parseInt(request.getParameter("up"));
			String idTemp =  (String) request.getParameter("id");
			String user = idTemp.split("user")[1];
			String id = idTemp.split("user")[0];
			boolean flag = dao.updateUpVote(id, user);
			if(!flag)
			{
				response.setContentType("text/plain");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(String.valueOf(0));
			}
		}

		if(type.equals("downvote")){
			int downs = Integer.parseInt(request.getParameter("down"));
			String idTemp =  (String) request.getParameter("id");
			String user = idTemp.split("user")[1];
			String id = idTemp.split("user")[0];
			boolean flag = dao.updateDownVote(id, user);
			if(!flag)
			{
				response.setContentType("text/plain");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(String.valueOf(0));
			}
		}

		if(type.equals("feedback")){
			int novice = Integer.parseInt(request.getParameter("novice"));
			int details = Integer.parseInt(request.getParameter("details"));
			int unique = Integer.parseInt(request.getParameter("unique"));
			int motivation = Integer.parseInt(request.getParameter("motivation"));
			int id = Integer.parseInt(request.getParameter("id"));
			String comments = (String) request.getParameter("comments");
			dao.updateFeedback(novice, details, unique, motivation, id, comments);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
