package cacheOverflow2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserStory implements Serializable{
	String title;
	String author;
	String desciption;
	int points;
	List<String> comments;
	int finishDate;
	int sprintStatus;

	private static final long serialVersionUID = 1L;


	public UserStory(String titleIn, String authorIn, String descriptionIn, int pointsIn) {
		title = titleIn;
		author = authorIn;
		desciption = descriptionIn;
		points = pointsIn;
		comments = new ArrayList<String>();
		finishDate = 0;
		sprintStatus = 0;
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
	
	public int getFinishDate() {
		return finishDate;
	}
	
	public void setFinishDate(int dateIn) {
		finishDate = dateIn;
	}
	
	public int getSprintStatus() {
		return sprintStatus;
	}
	
	public void setSprintStatus(int statusIn) {
		sprintStatus = statusIn;
	}
}
