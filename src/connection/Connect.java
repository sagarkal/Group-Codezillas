/* Author: Abhishek Reddy
 * Group 10 Codezillas
 * Purpose: Class that defines a new connection object to the underlying database
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
