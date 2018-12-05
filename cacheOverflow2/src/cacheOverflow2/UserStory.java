package cacheOverflow2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserStory implements Serializable{
	String title;
	String author;
	String description;
	int points;
	List<String> comments;
	int finishDate;
	int sprintStatus;

	private static final long serialVersionUID = 1L;


	public UserStory(String titleIn, String authorIn, String descriptionIn, int pointsIn) {
		title = titleIn;
		author = authorIn;
		description = descriptionIn;
		points = pointsIn;
		comments = new ArrayList<String>();
		finishDate = 0;
		sprintStatus = 0;
	}

	public void addComment(String comment) {
		comments.add(comment);
	}
	
	public void removeComment(String comment) {
		comments.remove(comment);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String titleIn) {
		title = titleIn;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String authorIn) {
		author = authorIn;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descriptionIn) {
		description = descriptionIn;
	}
	
	public int getScore() {
		return points;
	}
	
	public void setScore(int score) {
		points = score;
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
	
	public ObservableList<String> getComments(){
		return FXCollections.observableArrayList(comments);
	}

}
