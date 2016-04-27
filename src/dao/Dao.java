

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
			System.out.println(username);
			pStmt = con.prepareStatement("select accuracy, conciseness, redundancy, grammar from users where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				u.setAccuracy(rSet.getInt(1));
				u.setConciseness(rSet.getInt(2));
				u.setRedundancy(rSet.getInt(3));
				u.setGrammar(rSet.getInt(4));
				ul.add(u);
			}
			rSet.close();
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aid;
	}

	public UserBean getReputation(String username) {
		UserBean u = new UserBean();
		username = username.trim();
		username += "@asu.edu";
		System.out.println("In GETREPUTATION DAO.....................");
		System.out.println("Username: "+username);
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
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+rSet.getInt(5));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(6));
				u.setQuiz(rSet.getString(7));
			}
			rSet.close();
			
		System.out.println("For username "+ u.getUsername() +" Java: "+ u.getJava()+" CPP: "+u.getCpp()+" Python: "+u.getPython()+
				" C#: "+u.getCsharp()+" Javascript: "+u.getJavascript());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	public ArrayList<UserBean> getReputationForTopFiveUsers()
	{
		ArrayList<UserBean> ul = new ArrayList<UserBean>();
		try {
			pStmt = con
					.prepareStatement("select username, java, cpp, python, csharp, javascript from users");
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				UserBean u = new UserBean();
				u.setUsername(rSet.getString(1));
				u.setJava(rSet.getInt(2));
				u.setCpp(rSet.getInt(3));
				u.setPython(rSet.getInt(4));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(6));
				//System.out.println("Username: "+u.getUsername()+" Java: "+u.getJava()+" CPP: "+u.getCpp()+" Python: "+
				//u.getPython()+" CSharp: "+u.getCsharp()+" Javascript: "+u.getJavascript());
				ul.add(u);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(UserBean u : ul){
		System.out.println("Username: "+u.getUsername()+" Java: "+u.getJava()+" CPP: "+u.getCpp()+" Python: "+
				u.getPython()+" CSharp: "+u.getCsharp()+" Javascript: "+u.getJavascript());
		}
		return ul;
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
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ul;
	}

	public int updateReputation(String username, String language, int pointsToAdd)
	{
		int currentReputation = 0;
		try {
			pStmt = con
					.prepareStatement("select "+language+" from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();

			if (rSet.next()) {
				currentReputation =	rSet.getInt(1);
			}

			System.out.println(" In DAO updateReputation.....");
			System.out.println("Current reputation is "+currentReputation);

			if(currentReputation == 0)
			{
			pStmt = con
					.prepareStatement("update users set "+language+"="+(currentReputation+pointsToAdd)+" where username = ?");
			pStmt.setString(1, username);
			pStmt.executeUpdate();
			return 1;
			}
			rSet.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateUpVote(String sid)
	{
		try {
			int id =0;
			int up = 0;
			if (sid.contains("and")){
				id = Integer.parseInt(sid.split("and")[1]);
				pStmt = con.prepareStatement("select upvotes from answers where id="+id);
				rSet = pStmt.executeQuery();
				if (rSet.next()) {
					up =	rSet.getInt(1) + 1;
				}
				pStmt = con.prepareStatement("update answers set UPVOTES=?  where id=?");

			} else {
				id = Integer.parseInt(sid);
				pStmt = con.prepareStatement("select upvotes from questions where id="+id);
				rSet = pStmt.executeQuery();
				if (rSet.next()) {
					up =	rSet.getInt(1) + 1;
				}
				pStmt = con.prepareStatement("update questions set UPVOTES=?  where id=?");
			}

			pStmt.setInt(1, up);
			pStmt.setInt(2, id);
			pStmt.executeUpdate();
			rSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateDownVote(String sid)
	{
		try {
			int id =0;
			int down = 0;
			if (sid.contains("and")){
				id = Integer.parseInt(sid.split("and")[1]);
				pStmt = con.prepareStatement("select downvotes from answers where id="+id);
				rSet = pStmt.executeQuery();
				if (rSet.next()) {
					down =	rSet.getInt(1) + 1;
				}
				System.out.println(down);
				pStmt = con.prepareStatement("update answers set DOWNVOTES=?  where id=?");

			} else {
				id = Integer.parseInt(sid);
				pStmt = con.prepareStatement("select downvotes from questions where id="+id);
				rSet = pStmt.executeQuery();
				if (rSet.next()) {
					down =	rSet.getInt(1) + 1;
				}
				System.out.println(down);
				pStmt = con.prepareStatement("update questions set DOWNVOTES=?  where id=?");
			}

			pStmt.setInt(2, id);
			pStmt.setInt(1, down);
			pStmt.executeUpdate();
			rSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateFeedback(int accuracy, int conciseness, int redundancy, int grammar, int id, String comments)
	{
		String username = "";
		int oldAccuracy = 0 , oldConciseness = 0, oldRedundancy = 0, oldGrammar = 0;
		try{
			pStmt = con.prepareStatement("select username from answers where id="+id);
			rSet = pStmt.executeQuery();

			if (rSet.next()) {
				username =	rSet.getString(1);
			}
			username=username.trim();
			username += "@asu.edu";

			pStmt = con.prepareStatement("select accuracy, conciseness, redundancy, grammar from users where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();

			if (rSet.next()) {
				oldAccuracy =	rSet.getInt(1);
				oldConciseness = rSet.getInt(2);
				oldRedundancy = rSet.getInt(3);
				oldGrammar = rSet.getInt(4);
			}

			pStmt = con.prepareStatement("update users set accuracy="+(oldAccuracy+accuracy)+", conciseness="+(oldConciseness+conciseness)+", "
					+ "redundancy="+(redundancy+oldRedundancy)+", grammar="+(oldGrammar+grammar)+"  where username=?");
			pStmt.setString(1, username);
			pStmt.executeUpdate();
			
			pStmt = con.prepareStatement("insert into comments values(?,?,?)");
			pStmt.setInt(1, id);
			pStmt.setString(2, username);
			pStmt.setString(3, comments);
			pStmt.executeQuery();
			rSet.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<CommentBean> getFeedbackComments(String username){
		username = username.trim();
		ArrayList<CommentBean> cl = new ArrayList<CommentBean>();
		System.out.println("Entered getFeedbackComments DAO...............");
		try{
			pStmt = con.prepareStatement("select * from comments where username=?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			
			while (rSet.next()){
				CommentBean c = new CommentBean();
				c.setAnswerid(rSet.getInt(1));
				c.setUsername(rSet.getString(2));
				c.setComments(rSet.getString(3));
				System.out.println(c.getAnswerid()+ " "+c.getUsername()+" "+c.getComments());
				cl.add(c);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return cl;
	}
}
