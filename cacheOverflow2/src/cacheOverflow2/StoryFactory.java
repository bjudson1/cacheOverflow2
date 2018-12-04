package cacheOverflow2;

import java.util.ArrayList;
import java.util.List;

public class StoryFactory {
	private List<UserStory> productBacklog;
	private List<UserStory> sprintBackLog;
	private List<UserStory> completed;

	public StoryFactory() {
		productBacklog = new ArrayList<UserStory>();
		sprintBackLog = new ArrayList<UserStory>();
	}
	
	public void addStory(String author, String description, int points) {
		productBacklog.add(new UserStory(author,description,points));
	}
	
	public void sprintStory(UserStory story) {
		productBacklog.remove(story);
		sprintBackLog.add(story);
	}
	
	public void finishStory(UserStory story) {
		sprintBackLog.remove(story);
		completed.add(story);
	}
}
