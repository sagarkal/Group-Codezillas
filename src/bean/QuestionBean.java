/* Author: Kulvir Gahlawat
 * Group 10 Codezillas
 * Purpose: Bean class that contains the attributes of question table
 */

package bean;

public class QuestionBean {
	private int id;
	private String question;
	private int upvotes;
	private int downvotes;
	private String tags;
	private String username;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getUpvotes() {
		return upvotes;
	}
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	public int getDownvotes() {
		return downvotes;
	}
	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


}
