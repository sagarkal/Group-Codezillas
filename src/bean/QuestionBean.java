package bean;

public class QuestionBean {
	private long id;
	private String question;
	private long upvotes;
	private long downvotes;
	private String tags;
	private String username;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public long getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(long upvotes) {
		this.upvotes = upvotes;
	}

	public long getDownvotes() {
		return downvotes;
	}

	public void setDownvotes(long downvotes) {
		this.downvotes = downvotes;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
