package cacheOverflow2;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StoryFactory extends Observable implements Serializable{
	private ArrayList<UserStory> productBacklog;
	private ArrayList<UserStory> sprintBackLog;
	private ArrayList<UserStory> completed;
	private UserStory selectedStory;
	
	private ArrayList<ArrayList<UserStory>> allLogs;
	private static final long serialVersionUID = 1L;
	private static StoryFactory instance = null;

	public StoryFactory() {
		productBacklog = new ArrayList<UserStory>();
		sprintBackLog = new ArrayList<UserStory>();
		completed = new ArrayList<UserStory>();
		allLogs = new ArrayList<ArrayList<UserStory>>(); 
		allLogs.add(productBacklog);
		allLogs.add(sprintBackLog);
		allLogs.add(completed);
	}
	
	public static StoryFactory getInstance() {
		if(instance == null) {
			instance = new StoryFactory();
		}
		
		return instance;
	}
	
	public void saveState() {
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saveState.dat"));
			out.writeObject(allLogs);
			out.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void loadState() throws ClassNotFoundException {
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("saveState.dat"));
			allLogs = (ArrayList<ArrayList<UserStory>>) in.readObject();
			in.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		
		productBacklog = allLogs.get(0);
		sprintBackLog = allLogs.get(1);
		completed = allLogs.get(2);
		
		setChanged();
		notifyObservers();
	}
	
	public void addStory(String title, String author, String description, int points) {
		UserStory story = new UserStory(title, author, description, points);
		
		productBacklog.add(story);
		setChanged();
		notifyObservers();
	}

	public void sprintStory(UserStory story) {
		productBacklog.remove(story);
		sprintBackLog.add(story);
		
		setChanged();
		notifyObservers();
	}
	
	public void unsprintStory(int index) {
		UserStory story = sprintBackLog.get(index);
		story.setSprintStatus(-1);
		productBacklog.add(story);
		sprintBackLog.remove(index);
		
		setChanged();
		notifyObservers();
	}
	
	public void sprintStory(int index) {
		UserStory story = productBacklog.get(index);
		story.setSprintStatus(0);
		sprintBackLog.add(story);
		productBacklog.remove(index);
		
		setChanged();
		notifyObservers();
	}

	public void finishStory(UserStory story) {
		sprintBackLog.remove(story);
		completed.add(story);
		story.setSprintStatus(3);
		
		setChanged();
		notifyObservers();
	}
	
	public void unfinishStory(int index) {
		UserStory story = completed.get(index);
		story.setSprintStatus(0);
		story.setFinishDate(-1);
		sprintBackLog.add(story);
		completed.remove(index);
		story.setAssignee("None");
		
		setChanged();
		notifyObservers();
	}
	
	public void finishStory(int index) {
		UserStory story = sprintBackLog.get(index);
		story.setSprintStatus(3);
		completed.add(story);
		sprintBackLog.remove(index);

		
		//setChanged();
		//notifyObservers();
	}
	
	
	public void removeStory(UserStory story) {
		productBacklog.remove(story);
		sprintBackLog.remove(story);
		completed.remove(story);
		
		setChanged();
		notifyObservers();
	}
	
	public void removeStory(int index,int logNum) {
		if(logNum == 0) {
			productBacklog.remove(index);
		}
		
		else if(logNum == 1) {
			sprintBackLog.remove(index);
		}
		
		else if(logNum == 2) {
			completed.remove(index);
		}
		
		setChanged();
		notifyObservers();
	}
	
	//return as observable list
	public ObservableList<UserStory> getProductBacklog(){
		return FXCollections.observableArrayList(productBacklog);
	}
	
	//return as observable list
	public ObservableList<UserStory> getSprintBacklog(){
		return FXCollections.observableArrayList(sprintBackLog);
	}
	
	//return as observable list
	public ObservableList<UserStory> getcompletedLog(){
		return FXCollections.observableArrayList(completed);
	}
	
	public void setSelectedStory(UserStory storyIn){
		selectedStory = storyIn;
	}
	
	public UserStory getSelectedStory() {
		return selectedStory;
	}
	
	public void setSelectedStoryDate(int date) {
		selectedStory.setFinishDate(date);
		
		setChanged();
		notifyObservers();
	}
	
	public void setSelectedStorySprintStatus(int status) {
		selectedStory.setSprintStatus(status);
		
		setChanged();
		notifyObservers();
	}
	
	public void addCommentToSelected(String comment) {
		selectedStory.addComment(comment);
		
		setChanged();
		notifyObservers();
	}
	
	public void removeCommentFromSelected(String comment) {
		selectedStory.removeComment(comment);
		
		setChanged();
		notifyObservers();
	}
	
	public void detailUpdate(String title, String author, String description, int score) {		
		selectedStory.setTitle(title);
		selectedStory.setAuthor(author);
		selectedStory.setDescription(description);
		selectedStory.setScore(score);
		
		setChanged();
		notifyObservers();
	}
	
	//returns 1st user story with matching title
	public UserStory findByTitle(String title) {
		for(UserStory story : productBacklog) {
			if(story.getTitle().equals(title)) {
				return story;
			}
		}
		
		for(UserStory story : sprintBackLog) {
			if(story.getTitle().equals(title)){
				return story;
			}
				
		}
		
		for(UserStory story : completed) {
			if(story.getTitle().equals(title)) {
				return story;
			}
		}
		
		return null;
	}
	
	public void setSelectedStoryAssignee(String assignee) {
		selectedStory.setAssignee(assignee);
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<ArrayList<UserStory>> getAllLogs() {
		return allLogs;
	}
	
	public void setAllLogs(ArrayList<ArrayList<UserStory>> allLogsIn) {
		allLogs = allLogsIn;
		
		productBacklog = allLogs.get(0);
		sprintBackLog = allLogs.get(1);
		completed = allLogs.get(2);
		
		setChanged();
		notifyObservers();
	}
}
