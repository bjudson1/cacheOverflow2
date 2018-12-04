package cacheOverflow2;

import java.util.ArrayList;
import java.util.List;

public class UserStory {
	String title;
	String author;
	String desciption;
	int points;
	List<String> comments;

	public UserStory(String titleIn, String authorIn, String descriptionIn, int pointsIn) {
		title = titleIn;
		author = authorIn;
		desciption = descriptionIn;
		points = pointsIn;
		comments = new ArrayList<String>();
	}

	public void addComment(String comment) {
		comments.add(comment);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public int getScore() {
		return points;
	}
}
