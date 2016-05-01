/* Author: Kulvir Gahlawat
 * Group 10 Codezillas
 * Purpose: Bean class that contains the attributes to handle login functionality
 */

package bean;


public class LoginBean {

	private String userId;
	private String password;

	public String getPassword () {
		return password;
	}
	public void setPassword (String password) {
		this.password = password;
	}
	public String getUserId () {
		return userId;
	}
	public void setUserId (String userId) {
		this.userId = userId;
	}
}
