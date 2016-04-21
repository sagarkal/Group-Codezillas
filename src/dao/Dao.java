/*
 * Copyright 2012 Wipro Technologies All Rights Reserved
 *
 *Customer specific copyright notice     : Pizza 2 Home.
 *
 * File Name			:	LoginDao.java
 *
 * Short Description	:	Used to perform login operation on database.
 *
 * Version Number	:	1.0.0
 *
 * Created Date		:	15 may 2012
 *
 * Modification History	:	Kulvir,15-MAY-2012
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.AnswerBean;
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
			System.out.println("Entered Questions DAO");
			pStmt = con.prepareStatement("select * from questions");
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				QuestionBean q = new QuestionBean();
				q.setId(rSet.getInt(1));
				q.setQuestion(rSet.getString(2));
				q.setUpvotes(rSet.getInt(3));
				q.setDownvotes(rSet.getInt(4));
				q.setTags(rSet.getString(5));
				q.setUsername(rSet.getString(6));
				al.add(q);
				System.out.println("Exited Questions DAO");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public ArrayList<UserBean> getFeedback(String username) {
		ArrayList<UserBean> ul = new ArrayList<UserBean>();
		username = username.trim();
		UserBean u = new UserBean();
		try {
			System.out.println("Entered Get Feedback DAO");
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
		System.out.println("------------------------------------------------------------------------------");
		System.out.println(u.getAccuracy()+" "+u.getConciseness()+" "+u.getRedundancy()+" "+u.getGrammar());
		System.out.println("------------------------------------------------------------------------------");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ul;
	}

	public ArrayList<AnswerBean> getAnswers(long qid) {
		ArrayList<AnswerBean> al = new ArrayList();
		try {
			pStmt = con
					.prepareStatement("select * from answers where questionid = ?");
			pStmt.setLong(1, qid);
			rSet = pStmt.executeQuery();
			while (rSet.next()) {
				AnswerBean a = new AnswerBean();
				a.setId(rSet.getInt(1));
				a.setAnswer(rSet.getString(2));
				a.setUpvotes(rSet.getInt(3));
				a.setDownvotes(rSet.getInt(4));
				a.setUsername(rSet.getString(5));
				a.setQuestionId(rSet.getInt(6));
				al.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}

	public void saveUser(UserBean u)
	{
		try{
			pStmt = con.prepareStatement("insert into users values(?,?,?,?,?,?,?,?)");
			pStmt.setString(1, u.getUsername());
			pStmt.setInt(2, 0);
			pStmt.setInt(3, 0);
			pStmt.setInt(4, 0);
			pStmt.setInt(5, 0);
			pStmt.setInt(6, 0);
			pStmt.setInt(7, 0);
			pStmt.setString(8, u.getPassword());
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
			System.out.println("In DAO...");
			System.out.println(rSet.getString(1));
			if (rSet.getString(1).equals(user.getPassword()))
			{
				System.out.println("Returning true from DAO...");
				return true;
			}
			}
		}catch (SQLException e) {
		e.printStackTrace();
		}
		return false;
	}

	public long saveAnswer(AnswerBean a) {
		long aid = 0;
		try {
			pStmt = con.prepareStatement("select max(id) from answers");
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				aid = rSet.getLong(1) + 1;
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aid;
	}

	public UserBean getReputation(String username) {
		UserBean u = new UserBean();
		try {
			pStmt = con
					.prepareStatement("select * from users where username = ?");
			pStmt.setString(1, username);
			rSet = pStmt.executeQuery();
			if (rSet.next()) {
				u.setUsername(rSet.getString(1));
				u.setJava(rSet.getInt(2));
				u.setCpp(rSet.getInt(3));
				u.setPython(rSet.getInt(4));
				u.setCsharp(rSet.getInt(5));
				u.setJavascript(rSet.getInt(6));
				u.setQuiz(rSet.getString(7));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
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
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateUpVote(AnswerBean a)
	{
		try {
			System.out.println("Entered DAO to update Upvote....................");
			pStmt = con.prepareStatement("update answers set UPVOTES=?  where id=?");
			System.out.println("a.upvotes :" + a.getUpvotes() );
			System.out.println("a.id :" + a.getId());

			pStmt.setInt(1, a.getUpvotes());
			pStmt.setInt(2, a.getId());			
			//System.out.println(pStmt.toString());
			pStmt.executeUpdate();
			System.out.println("Finished DAO to update Upvote....................");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDownVote(AnswerBean a)
	{
		try {
			System.out.println("Entered DAO to update Downvote....................");
			pStmt = con.prepareStatement("update answers set downvotes=?  where id=?");
			pStmt.setInt(2, a.getId());
			pStmt.setInt(1,a.getDownvotes());
			
			pStmt.executeUpdate();
			System.out.println("Finished DAO to update Downvote....................");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateFeedback(int accuracy, int conciseness, int redundancy, int grammar, int id)
	{
		String username = "";
		int oldAccuracy = 0 , oldConciseness = 0, oldRedundancy = 0, oldGrammar = 0;
		try{
			System.out.println("Entered UpdateFeedback DAO................");
			System.out.println(accuracy+" "+conciseness+" "+redundancy+" "+grammar);
			
			pStmt = con.prepareStatement("select username from answers where id="+id);
			rSet = pStmt.executeQuery();
			
			if (rSet.next()) {
				username =	rSet.getString(1);
			}
			username=username.trim();
			System.out.println("Extracted username from answers table!! It is "+ username);
			
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
			//pStmt.setInt(1, accuracy);
			//pStmt.setInt(2, conciseness);
			//pStmt.setInt(3, redundancy);
			//pStmt.setInt(4, grammar);
			pStmt.setString(1, username);
			pStmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
