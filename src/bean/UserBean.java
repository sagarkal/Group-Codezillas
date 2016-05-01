/* Author: Kulvir Gahlawat
 * Group 10 Codezillas
 * Purpose: Bean class that contains the attributes of user table
 */

package bean;

public class UserBean {
	private String username;
	private String firstName;
	private String lastName;
	private int java;
	private int javascript;
	private int python;
	private int cpp;
	private int csharp;
	private String quiz;
	private String password;
	private int details;
	private int unique;
	private int motivation;
	private String aboutme;
	private int novice;
	
	public String getAboutme() {
		return aboutme;
	}
	public void setAboutme(String aboutme) {
		this.aboutme = aboutme;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public int getNovice() {
		return novice;
	}
	public void setNovice(int novice) {
		this.novice = novice;
	}
	public int getDetails() {
		return details;
	}
	public void setDetails(int details) {
		this.details = details;
	}
	public int getUnique() {
		return unique;
	}
	public void setUnique(int unique) {
		this.unique = unique;
	}
	public int getMotivation() {
		return motivation;
	}
	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getJava() {
		return java;
	}
	public void setJava(int java) {
		this.java = java;
	}
	public int getJavascript() {
		return javascript;
	}
	public void setJavascript(int javascript) {
		this.javascript = javascript;
	}
	public int getPython() {
		return python;
	}
	public void setPython(int python) {
		this.python = python;
	}
	public int getCpp() {
		return cpp;
	}
	public void setCpp(int cpp) {
		this.cpp = cpp;
	}
	public int getCsharp() {
		return csharp;
	}
	public void setCsharp(int csharp) {
		this.csharp = csharp;
	}
	public String getQuiz() {
		return quiz;
	}
	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


}
