/*
 * Copyright 2012 Wipro Technologies All Rights Reserved
 *
 * Customer specific copyright notice     : Pizza 2 Home.
 *
 * File Name			:	Connect.java
 *
 * Short Description	:	Establish connection to the database
 *
 * Version Number	:	1.0.0
 *
 * Created Date		:	15 may 2012
 *
 * Modification History	:	Kulvir,20-MAY-2012
 */


package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

	Connection con;//Connection object
	static Connect mC;//Connect object

	public static Connect myConnection() {
		if (mC == null)
			mC = new Connect();

		return mC;
	}

	public static void setConnectNull() {
		mC = null;
	}

	public Connection getConnect() {
		try {
			if (con == null) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:XE", "system",
						"oracle");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

}
