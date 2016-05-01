/* Authors: Kulvir Gahlawat, Nitesh Dhanpal and Sagar Kalburgi
 * Group 10 Codezillas
 * Purpose: Class that contains code that provides access to the underlying database
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.AnswerBean;
import bean.CommentBean;
import bean.LoginBean;
import bean.QuestionBean;
import bean.UserBean;
import connection.Connect;

/**
 * class LoginDao for Login Details
 *
 * @version 1.0
 * @author Kulvir
 */
public class Dao {

	private Connection con;// Connection Object
	private PreparedStatement pStmt;// Prepared Statement object
	private ResultSet rSet;// ResultSet Object

	public Dao() {
		con = Connect.myConnection().getConnect();
	}

	public ArrayList<QuestionBean> getQuestions() {
		ArrayList<QuestionBean> al = new ArrayList();
		try {
			pStmt = con.prepareStatement("select * from questions");
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				QuestionBean q = new QuestionBean();
				q.setId(rSet.getInt(1));
				q.setQuestion(rSet.getString(2));
				q.setUpvotes(rSet.getInt(3));
				q.setDownvotes(rSet.getInt(4));
				q.setTags(rSet.getString(5));
				q.setUsername(rSet.getString(6).split("@")[0]);
				al.add(q);
			}
			rSet.close();
			pStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public UserBean getUserDetails(String username)
	{
		UserBean u = new UserBean();
		try{
			pStmt = con.prepareStatement("select firstname, lastname, username, aboutme from users where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()){
				u.setFirstName(rSet.getString(1));
				u.setLastName(rSet.getString(2));
				u.setUsername(rSet.getString(3));
				u.setAboutme(rSet.getString(4));
			}
			rSet.close();
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return u;
	}

	public ArrayList<UserBean> getFeedback(String username) {
		ArrayList<UserBean> ul = new ArrayList<UserBean>();
		username = username.trim();
		UserBean u = new UserBean();
		try {
			pStmt = con.prepareStatement("select novice, details, uniqueness, motivation from users where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				u.setNovice(rSet.getInt(1));
				u.setDetails(rSet.getInt(2));
				u.setUnique(rSet.getInt(3));
				u.setMotivation(rSet.getInt(4));
				ul.add(u);
			}
			rSet.close();
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ul;
	}
	
	

	public ArrayList<AnswerBean> getAnswers(int qid) {
		ArrayList<AnswerBean> al = new ArrayList();
		try {
			pStmt = con
					.prepareStatement("select * from answers where questionid = ?");
			pStmt.setInt(1, qid);
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				AnswerBean a = new AnswerBean();
				a.setId(rSet.getInt(1));
				a.setAnswer(rSet.getString(2));
				a.setUpvotes(rSet.getInt(3));
				a.setDownvotes(rSet.getInt(4));
				a.setUsername(rSet.getString(5).split("@")[0]);
				a.setQuestionId(rSet.getInt(6));
				al.add(a);
			}
			rSet.close();
			pStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}

	public void saveUser(UserBean u)
	{
		try{
			pStmt = con.prepareStatement("insert into users values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pStmt.setString(1, u.getUsername());
			pStmt.setInt(2, 0);
			pStmt.setInt(3, 0);
			pStmt.setInt(4, 0);
			pStmt.setInt(5, 0);
			pStmt.setInt(6, 0);
			pStmt.setInt(7, 0);
			pStmt.setString(8, u.getPassword());
			pStmt.setInt(9, 0);
			pStmt.setInt(10, 0);
			pStmt.setInt(11, 0);
			pStmt.setInt(12, 0);
			pStmt.setString(13, u.getFirstName());
			pStmt.setString(14, u.getLastName());
			pStmt.setString(15, u.getAboutme());
			pStmt.executeQuery();
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean userLogin(LoginBean user) {
		try {
		pStmt = con.prepareStatement("select password from users where username = ?");
		pStmt.setString(1, user.getUserId());
		rSet = pStmt.executeQuery();
		if (rSet.next())
			{
			if (rSet.getString(1).equals(user.getPassword()))
			{
				return true;
			}
			}
		rSet.close();
		pStmt.close();
		}catch (SQLException e) {
		e.printStackTrace();
		}
		return false;
	}

	public boolean saveQuestion(QuestionBean a) {
		try {
			pStmt = con
					.prepareStatement("insert into questions values(?,?,?,?,?,?)");
			pStmt.setInt(1,a.getId());
			pStmt.setString(2, a.getQuestion());
			pStmt.setInt(3, a.getUpvotes());
			pStmt.setInt(4, a.getDownvotes());
			pStmt.setString(5, a.getTags());
			pStmt.setString(6, a.getUsername());
			pStmt.executeUpdate();
			pStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int saveAnswer(AnswerBean a) {
		int aid = 0;
		try {
			pStmt = con.prepareStatement("select max(id) from answers");
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				aid = rSet.getInt(1) + 1;
			}
//			pStmt.close();
			
			pStmt = con
					.prepareStatement("insert into answers values(?,?,?,?,?,?)");
			pStmt.setInt(1, (int) aid);
			pStmt.setString(2, a.getAnswer());
			pStmt.setInt(3, (int) a.getUpvotes());
			pStmt.setInt(4, (int) a.getDownvotes());
			pStmt.setString(5, a.getUsername());
			pStmt.setInt(6, (int) a.getQuestionId());
			pStmt.executeUpdate();
			rSet.close();
			pStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aid;
	}

	public UserBean getReputation(String username) {
		UserBean u = new UserBean();
		username = username.trim();
		username += "@asu.edu";
		try {
			pStmt = con
					.prepareStatement("select * from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				u.setUsername(rSet.getString(1).split("@")[0]);
				u.setJava(rSet.getInt(2));
				u.setCpp(rSet.getInt(3));
				u.setPython(rSet.getInt(4));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(6));
				u.setQuiz(rSet.getString(7));
			}
			rSet.close();
			pStmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	public ArrayList<UserBean> getReputationForTopFiveUsers(String username)
	{
		ArrayList<UserBean> ml = new ArrayList<UserBean>();
		username = username.trim();
		username += "@asu.edu";
		ArrayList<UserBean> ul = new ArrayList<UserBean>();
		try {
			pStmt = con
					.prepareStatement("SELECT * FROM users ORDER BY QUIZ desc");		
			rSet = pStmt.executeQuery();
			
			boolean flag = false;
			
			while (rSet.next()) {
				UserBean u = new UserBean();
				u.setUsername(rSet.getString(1));
				u.setJava(rSet.getInt(2));
				u.setCpp(rSet.getInt(3));
				u.setPython(rSet.getInt(4));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(6));
				ul.add(u);
				if(rSet.getString(1).equals(username)){
					break;
				}
			}
			rSet.close();
			pStmt.close();
			
			

			int i = 0;
			int j = ul.size();
			while(i<5 && j!=0)
			{
				ml.add(ul.get(j-1));
				i++;
				j--;
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ml;
	}
	
	public ArrayList<UserBean> getReputationForProfile(String username)
	{
		ArrayList<UserBean> ul = new ArrayList();
		UserBean u = new UserBean();
		try {
			pStmt = con
					.prepareStatement("select java, javascript, cpp, python, csharp from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				u.setJava(rSet.getInt(1));
				u.setCpp(rSet.getInt(3));
				u.setPython(rSet.getInt(4));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(2));
				ul.add(u);
			}
			rSet.close();
			pStmt.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ul;
	}

	public void updateTotalReputationScore(String username){
		UserBean u=new UserBean();
		u.setUsername(username);
		try {
			pStmt = con
					.prepareStatement("select java, cpp, python, csharp, javascript from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			
			if (rSet.next()){
				u.setJava(rSet.getInt(1));
				u.setCpp(rSet.getInt(2));
				u.setPython(rSet.getInt(3));
				u.setCsharp(rSet.getInt(4));
				u.setJavascript(rSet.getInt(5));
			}
		int totalReputation = u.getJava() + u.getCpp() + u.getPython() + u.getCsharp() + u.getJavascript();
		
		pStmt = con
				.prepareStatement("update users set quiz=? where username = ?");
		pStmt.setInt(1, totalReputation);
		pStmt.setString(2, username);
		pStmt.executeUpdate();
		pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int updateReputation(String username, String language, double pointsToAdd)
	{
		double currentReputation = 0.0;
		try {
			pStmt = con
					.prepareStatement("select "+language+" from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
//			pStmt.close();
			
			if (rSet.next()) {
				currentReputation =	rSet.getDouble(1);
			}

			if(currentReputation == 0)
			{
			pStmt = con
					.prepareStatement("update users set "+language+"="+(currentReputation+pointsToAdd)+" where username = ?");
			pStmt.setString(1, username);
			pStmt.executeUpdate();
			
			updateTotalReputationScore(username);
			return 1;
			}
			rSet.close();
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateReputationFromOtherMeans(String username, String language, double pointsToAdd)
	{
		System.out.println(" IN other means :" + pointsToAdd +" "+language+" "+username);
		double currentReputation = 0.0;
		double updatedReputation = 0.0;
		try {
			username=username.trim();

			pStmt = con
					.prepareStatement("select "+language+" from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();

			if (rSet.next()) {
				currentReputation =	rSet.getDouble(1);	
				System.out.println("IN OTHER MEANS :"+currentReputation);
			}
//			pStmt.close();
			rSet.close();
			updatedReputation = currentReputation + pointsToAdd;

			pStmt = con.prepareStatement("update users set "+language+"="+String.valueOf(updatedReputation)+" where username =?");
			pStmt.setString(1, username);
			pStmt.executeUpdate();

			updateTotalReputationScore(username);
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateUpVote(String sid, String user)
	{
		try {
			int id =0;
			int up = 0;
			int quesid = 0;
			int reputation = 0;
			String language = "";
			String u = "";
			if (sid.contains("and")){
				id = Integer.parseInt(sid.split("and")[1]);
				pStmt = con.prepareStatement("select * from upvotes_answer where ansid=? and userid=? ");
				pStmt.setInt(1, id);
				pStmt.setString(2, user);
				rSet = pStmt.executeQuery();
				
				if (!rSet.next())
				{
					pStmt = con.prepareStatement("select questionid from answers where id=?");
					pStmt.setInt(1, id);
					rSet = pStmt.executeQuery();
					if (rSet.next()){
						quesid = rSet.getInt(1);
					}
//					pStmt.close();
					pStmt = con.prepareStatement("select tags from questions where id=?");
					pStmt.setInt(1, quesid);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						language = rSet.getString(1);
					}
					
					u = user+"@asu.edu";
					pStmt = con.prepareStatement("select "+language+" from users where username=?");
					pStmt.setString(1, u);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						reputation = rSet.getInt(1);
					}
					
					if(reputation >= 5){
						pStmt = con.prepareStatement("insert into upvotes_answer values(?,?)");
						pStmt.setInt(1, id);
						pStmt.setString(2, user);
						pStmt.execute();
						
						pStmt = con.prepareStatement("select upvotes from answers where id="+id);
						rSet = pStmt.executeQuery();
						if (rSet.next()) {
							up =	rSet.getInt(1) + 1;
						}
						
						pStmt = con.prepareStatement("update answers set UPVOTES=?  where id=?");
						pStmt.setInt(1, up);
						pStmt.setInt(2, id);
						pStmt.executeUpdate();
						updateReputationFromOtherMeans(user, language, 1.0);
						return 1;
					}else{
						return 2;
					}
				}	
				rSet.close();
				pStmt.close();
			}

			else {				
				id = Integer.parseInt(sid);
				pStmt = con.prepareStatement("select * from upvotes_question where qid=? and userid=? ");
				pStmt.setInt(1, id);
				pStmt.setString(2, user);
				rSet = pStmt.executeQuery();
				
				
				if (!rSet.next())
				{
					pStmt = con.prepareStatement("select tags from questions where id=?");
					pStmt.setInt(1, id);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						language = rSet.getString(1);
					}
					
					u = user+"@asu.edu";
					pStmt = con.prepareStatement("select "+language+" from users where username=?");
					pStmt.setString(1, u);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						reputation = rSet.getInt(1);
					}
					
					if(reputation >= 5){
						pStmt = con.prepareStatement("insert into upvotes_question values(?,?)");
						pStmt.setInt(1, id);
						pStmt.setString(2, user);
						pStmt.execute();
						
						pStmt = con.prepareStatement("select upvotes from questions where id="+id);
						rSet = pStmt.executeQuery();
						if (rSet.next()) {
							up =	rSet.getInt(1) + 1;
						}

						
						pStmt = con.prepareStatement("update questions set UPVOTES=?  where id=?");
						pStmt.setInt(1, up);
						pStmt.setInt(2, id);
						pStmt.executeUpdate();	

						updateReputationFromOtherMeans(user, language, 1.0);
						rSet.close();
						pStmt.close();
							return 1;
					}else {
						return 2;
					}	
				}
			}
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateDownVote(String sid, String user)
	{
		try {
			int id =0;
			int down = 0;
			String language = "";
			int quesid =0;
			String u = "";
			int reputation = 0;
			if (sid.contains("and")){
				id = Integer.parseInt(sid.split("and")[1]);
				pStmt = con.prepareStatement("select * from downvotes_answer where ansid=? and userid=? ");
				pStmt.setInt(1, id);
				pStmt.setString(2, user);
				rSet = pStmt.executeQuery();
				
				if (!rSet.next())
				{
					pStmt = con.prepareStatement("select questionid from answers where id=?");
					pStmt.setInt(1, id);
					rSet = pStmt.executeQuery();
					if (rSet.next()){
						quesid = rSet.getInt(1);
					}
					
					pStmt = con.prepareStatement("select tags from questions where id=?");
					pStmt.setInt(1, quesid);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						language = rSet.getString(1);
					}
					
					u = user+"@asu.edu";
					
					pStmt = con.prepareStatement("select "+language+" from users where username=?");
					pStmt.setString(1, u);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						reputation = rSet.getInt(1);
					}
					
					if(reputation >= 5){
						pStmt = con.prepareStatement("insert into downvotes_answer values(?,?)");
						pStmt.setInt(1, id);
						pStmt.setString(2, user);
						pStmt.execute();
						
						pStmt = con.prepareStatement("select downvotes from answers where id="+id);
						rSet = pStmt.executeQuery();
						if (rSet.next()) {
							down =	rSet.getInt(1) + 1;
						}
						rSet.close();
						
						pStmt = con.prepareStatement("update answers set DOWNVOTES=?  where id=?");
						pStmt.setInt(2, id);
						pStmt.setInt(1, down);
						pStmt.executeUpdate();
						updateReputationFromOtherMeans(user, language, -1.0);
						pStmt.close();
						return 1;
					}else{
						return 2;
					}						
				}
				
			}
			else {
				id = Integer.parseInt(sid);
				pStmt = con.prepareStatement("select * from downvotes_question where qid=? and userid=? ");
				pStmt.setInt(1, id);
				pStmt.setString(2, user);
				rSet = pStmt.executeQuery();
				
				if (!rSet.next())
				{
					pStmt = con.prepareStatement("select tags from questions where id=?");
					pStmt.setInt(1, id);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						language = rSet.getString(1);
					}
					
					u = user+"@asu.edu";
					
					pStmt = con.prepareStatement("select "+language+" from users where username=?");
					pStmt.setString(1, u);
					rSet = pStmt.executeQuery();
					
					if (rSet.next()){
						reputation = rSet.getInt(1);
					}
					
					if(reputation >= 5) {
						pStmt = con.prepareStatement("insert into downvotes_question values(?,?)");
						pStmt.setInt(1, id);
						pStmt.setString(2, user);
						pStmt.execute();
						
						pStmt = con.prepareStatement("select downvotes from questions where id="+id);
						rSet = pStmt.executeQuery();

						if (rSet.next()) {
							down =	rSet.getInt(1) + 1;
						}
						pStmt = con.prepareStatement("update questions set DOWNVOTES=?  where id=?");
						pStmt.setInt(2, id);
						pStmt.setInt(1, down);
						pStmt.executeUpdate();			
						
						updateReputationFromOtherMeans(user, language, -1.0);
						rSet.close();
						pStmt.close();
						return 1;
					}else{
						rSet.close();
						pStmt.close();
						return 2;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateFeedback(int novice, int details, int unique, int motivation, int id, String comments)
	{
		String username = "", language = "";
		int oldNovice = 0 , oldDetails = 0, oldUnique = 0, oldMotivation = 0, quesid = 0;
		double pointsToAdd = 0;
		try{
			pStmt = con.prepareStatement("select username from answers where id="+id);
			rSet = pStmt.executeQuery();
//			pStmt.close();
			if (rSet.next()) {
				username =	rSet.getString(1);
			}
			username=username.trim();
			username += "@asu.edu";

			pStmt = con.prepareStatement("select novice, details, uniqueness, motivation from users where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
//			pStmt.close();

			if (rSet.next()) {
				oldNovice =	rSet.getInt(1);
				oldDetails = rSet.getInt(2);
				oldUnique = rSet.getInt(3);
				oldMotivation = rSet.getInt(4);
			}

			pStmt = con.prepareStatement("update users set novice="+(oldNovice+novice)+", details="+(oldDetails+details)+", "
					+ "uniqueness="+(unique+oldUnique)+", motivation="+(oldMotivation+motivation)+"  where username=?");
			pStmt.setString(1, username);
			pStmt.executeUpdate();
//			pStmt.close();
			
			if(!comments.equals("")){
				pStmt = con.prepareStatement("insert into comments values(?,?,?)");
				pStmt.setInt(1, id);
				pStmt.setString(2, username);
				pStmt.setString(3, comments);
				pStmt.executeQuery();
				rSet.close();
//				pStmt.close();
			}
			
			pStmt = con.prepareStatement("select questionid from answers where id=?");
			pStmt.setInt(1, id);
			rSet = pStmt.executeQuery();
//			pStmt.close();
			
			
			if (rSet.next()) {
				quesid = rSet.getInt(1);
			}
			System.out.println("DAO UPDATE FEEDBACK QUESTIONID: "+quesid);
			
			pStmt = con.prepareStatement("select tags from questions where id=?");
			pStmt.setInt(1, quesid);
			rSet = pStmt.executeQuery();
			
			if (rSet.next()) {
				language = rSet.getString(1);
				System.out.println("DAO UPDATE FEEDBACK LANGUAGE: "+language);
			}
			
			rSet.close();
			pStmt.close();
			pointsToAdd = ((novice + details + unique + motivation)/20.0);
			
			updateReputationFromOtherMeans(username, language, pointsToAdd);
			System.out.println("IN updateFeedback: "+username+" "+language+" "+novice+details+unique+motivation+pointsToAdd);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<CommentBean> getFeedbackComments(String username){
		username = username.trim();
		ArrayList<CommentBean> cl = new ArrayList<CommentBean>();
		try{
			pStmt = con.prepareStatement("select * from comments where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			
			while (rSet.next()){
				CommentBean c = new CommentBean();
				c.setAnswerid(rSet.getInt(1));
				c.setUsername(rSet.getString(2));
				c.setComments(rSet.getString(3));
				cl.add(c);
			}
			rSet.close();
			pStmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return cl;
	}
}
