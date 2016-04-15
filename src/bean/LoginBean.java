/*
*
* Copyright 2006 Wipro Technologies All rights reserved.
*
* Customer specific copyright notice     : Pizza 2 Home.
*
* File Name       : LoginBean.java
*
* Description     :Used to set values and get values of attributes
*
* Version         : 1.0.0.
*
* Created Date    :15-MAY-2012
*
* Modification History:Kulvir ,17-MAY-2012
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
