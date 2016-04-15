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
				q.setId(rSet.getLong(1));
				q.setQuestion(rSet.getString(2));
				q.setUpvotes(rSet.getLong(3));
				q.setDownvotes(rSet.getLong(4));
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
				u.setJava(rSet.getLong(2));
				u.setCpp(rSet.getLong(3));
				u.setPython(rSet.getLong(4));
				u.setCsharp(rSet.getLong(5));
				u.setJavascript(rSet.getLong(6));
				u.setQuiz(rSet.getString(7));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}
	
	public void updateUpVote(AnswerBean a)
	{
		try {
			System.out.println("Entered DAO");
			pStmt = con.prepareStatement("update answers set UPVOTES=?  where id=?");
			System.out.println("a.upvotes :" + a.getUpvotes() );
			System.out.println("a.downvotes :" + a.getId());
			
			pStmt.setLong(1, a.getUpvotes());
			pStmt.setLong(2, a.getId());			
			//System.out.println(pStmt.toString());
			pStmt.executeUpdate();
			System.out.println("Finished DAO");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDownVote(AnswerBean a)
	{
		try {
			pStmt = con.prepareStatement("update answers set downvotes=?  where id=?");
			pStmt.setLong(2, a.getId());
			pStmt.setLong(1,a.getDownvotes());
			
			pStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
