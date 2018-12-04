package cacheOverflow2;


import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StoryFactory extends Observable{
	private ObservableList<UserStory> productBacklog;
	private ObservableList<UserStory> sprintBackLog;
	private ObservableList<UserStory> completed;
	
	private static StoryFactory instance = null;

	public StoryFactory() {
		productBacklog = FXCollections.observableArrayList();
		sprintBackLog = FXCollections.observableArrayList();
	}
	
	public static StoryFactory getInstance() {
		if(instance == null) {
			instance = new StoryFactory();
		}
		
		return instance;
	}
	
	public void addStory(String title, String author, String description, int points) {
		productBacklog.add(new UserStory(title, author, description, points));
		
		setChanged();
		notifyObservers();
	}

	public void sprintStory(UserStory story) {
		productBacklog.remove(story);
		sprintBackLog.add(story);
		
		setChanged();
		notifyObservers();
	}
	
	public void sprintStory(int index) {
		sprintBackLog.add(productBacklog.get(index));
		productBacklog.remove(index);
		
		setChanged();
		notifyObservers();
	}

	public void finishStory(UserStory story) {
		sprintBackLog.remove(story);
		completed.add(story);
		
		setChanged();
		notifyObservers();
	}
	
	public void finishStory(int index) {
		completed.add(sprintBackLog.get(index));
		sprintBackLog.remove(index);
		
		setChanged();
		notifyObservers();
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
		
		setChanged();
		notifyObservers();
	}
	
	
	public ObservableList<UserStory> getProductBacklog(){
		return productBacklog;
	}
	
	public ObservableList<UserStory> getSprintBacklog(){
		return sprintBackLog;
	}
}
