package cacheOverflow2;

import java.util.ArrayList;
import java.util.List;

public class UserStory {
	String author;
	String desciption;
	int points;
	int status;
	List<String> comments;
	
	public UserStory(String authorIn, String descriptionIn, int pointsIn) {
		author = authorIn;
		desciption = descriptionIn;
		points = pointsIn;
		status = 0;
		comments = new ArrayList<String>();
	}
	
	public void addComment(String comment){
		comments.add(comment);
	}
}
