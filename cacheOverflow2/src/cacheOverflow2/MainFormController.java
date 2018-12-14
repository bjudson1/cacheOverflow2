package cacheOverflow2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;



public class MainFormController implements Observer, Initializable{
	@FXML
	private Button addButton;

	@FXML
	private Button sprintButton;
	
	@FXML
	private Button unsprintButton;

	@FXML
	private Button finishButton;

	@FXML
	private Button finishButton2;
	
	@FXML
	private Button unfinishButton;

	@FXML
	private Button removeButton;
	
	@FXML
	private Button assignButton;
	
	@FXML
	private Button unassignButton;
	
	@FXML
	private Button inprogressButton;
	
	@FXML
	private Button notInprogressButton;
	
	@FXML
	private Button detailsButton;
	
	@FXML
	private ListView<Label> productBacklog;
	
	@FXML
	private ListView<Label> SprintBacklog;
	
	@FXML
	private ListView<Label> completionLog;
	
	@FXML
	private ListView<Label> waitingLog;
	
	@FXML
	private ListView<Label> assignedLog;
	
	@FXML
	private ListView<Label> inprogressLog;
	
	@FXML
	private LineChart<Integer, Integer> burnDown;
	
	@FXML
	private NumberAxis xAxis;
	
	@FXML
	private NumberAxis yAxis;
	
	private Socket client_socket;
	
	private ObjectOutputStream outputToServer;
		
	private ObjectInputStream inputFromServer1;
	
	private ObjectInputStream inputFromServer2;
	
	private ArrayList<ArrayList<UserStory>> allLogs;
	
	private class updateListener implements Runnable{		
		public updateListener() {
		}
		
		public void run() {
			try {
				inputFromServer2 = new ObjectInputStream(client_socket.getInputStream());
			
	   	 		while(true){
				System.out.println("fff");
				//inputFromServer.readObject();
				
					inputFromServer2.readObject();
	   	 		}
	   	 	} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
		
		        //recieve new state
	   	 		//StoryFactory.getInstance().setAllLogs((ArrayList<ArrayList<UserStory>>) inputFromServer.readObject());
	        
		}
	}

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		boolean connected = true; 
		   
		//connect to server
	    try {
	    	client_socket = new Socket("localhost", 8000);
	    	outputToServer = new ObjectOutputStream(client_socket.getOutputStream());
	    	inputFromServer1 = new ObjectInputStream(client_socket.getInputStream());
	    	
	    	System.out.println("Connected to server");
	    }
	    catch (IOException e) {
	        System.out.println(e);
	        connected = false;
	    }
		
		if(connected) {
			try {
				outputToServer.writeObject("get_state");
				outputToServer.flush();
				
				String input = (String) inputFromServer1.readObject();
				
				//server has no state
				if(input.equals("none")) {
			        //load state from disk
					StoryFactory.getInstance().loadState();
					
					//send state to server
					outputToServer.writeObject(StoryFactory.getInstance().getAllLogs());
					outputToServer.flush();
				}
				
				//else get state from server
				else if(input.equals("ready?")){
					outputToServer.writeObject("ready");
					outputToServer.flush();
					
					//load from server
					StoryFactory.getInstance().setAllLogs((ArrayList<ArrayList<UserStory>>) inputFromServer1.readObject());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();	
			}
		}
		
		//no server
		else {
			//load state from disk
			try {
				StoryFactory.getInstance().loadState();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		StoryFactory.getInstance().addObserver(this);

		//update ui to load state
		updateUI();
		
		xAxis.setUpperBound(30);
		yAxis.setUpperBound(100);
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
		
		try {
			inputFromServer1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//start update listener
		new Thread(new updateListener()).start();
    }

	@FXML
	protected void handleAddButtonAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addUserStoryForm.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Add User Story");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}
	}
	
	@FXML
	protected void handleSprintButtonAction(ActionEvent event) {
		Window owner = sprintButton.getScene().getWindow();

		int selectedStory = productBacklog.getSelectionModel().getSelectedIndex();
				
		if(selectedStory == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the product backlog.");
			return;
		}
		
		StoryFactory.getInstance().sprintStory(selectedStory);
	}
	
	@FXML
	protected void handleUnsprintButtonAction(ActionEvent event) {
		Window owner = unsprintButton.getScene().getWindow();

		int selectedStory = SprintBacklog.getSelectionModel().getSelectedIndex();
				
		if(selectedStory == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the sprint backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(SprintBacklog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
		
		StoryFactory.getInstance().setSelectedStory(story);
		StoryFactory.getInstance().unsprintStory(selectedStory);		
		StoryFactory.getInstance().setSelectedStoryAssignee("None");
		
	}

	@FXML
	protected void handleFinishButtonAction(ActionEvent event) {
		Window owner = finishButton.getScene().getWindow();
		int selectedIndex = SprintBacklog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the sprint backlog.");
			return;
		}
		
		//set selected story
		StoryFactory.getInstance().setSelectedStory(StoryFactory.getInstance().getSprintBacklog().get(selectedIndex));
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dateEntryForm.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Finish Date Entry");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}

		StoryFactory.getInstance().finishStory(selectedIndex);
	}
	
	@FXML
	protected void handleFinishButton2Action(ActionEvent event) {
		Window owner = finishButton2.getScene().getWindow();
		int selectedIndex = inprogressLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the inprogress backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(inprogressLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
		//set selected story
		StoryFactory.getInstance().setSelectedStory(story);
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dateEntryForm.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Finish Date Entry");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}

		StoryFactory.getInstance().finishStory(story);
	}
	
	@FXML
	protected void handleUnfinishButtonAction(ActionEvent event) {
		Window owner = unfinishButton.getScene().getWindow();
		int selectedIndex = completionLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the completion backlog.");
			return;
		}
		
		//set selected story
		StoryFactory.getInstance().setSelectedStory(StoryFactory.getInstance().getcompletedLog().get(selectedIndex));
		//clear finish date
		StoryFactory.getInstance().getSelectedStory().setFinishDate(0);
		//move story
		StoryFactory.getInstance().unfinishStory(selectedIndex);
	}

	@FXML
	protected void handleRemoveButtonAction(ActionEvent event) {
		Window owner = removeButton.getScene().getWindow();
		int correspondingLog = 0;
		
		int selectedStory = productBacklog.getSelectionModel().getSelectedIndex();
		
		if(selectedStory == -1) {
			correspondingLog = 1;
			selectedStory = SprintBacklog.getSelectionModel().getSelectedIndex();

			if(selectedStory == -1) {
				correspondingLog = 2;
				selectedStory = completionLog.getSelectionModel().getSelectedIndex();
				
				if(selectedStory == -1) {
					AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from a backlog.");
					return;
				}
			}
		}
		
		StoryFactory.getInstance().removeStory(selectedStory,correspondingLog);		
	}
	
	@FXML
	public void handleAssignButtonAction(ActionEvent event) {
		Window owner = assignButton.getScene().getWindow();
		
		int selectedIndex = waitingLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from waiting backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(waitingLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);

		StoryFactory.getInstance().setSelectedStory(story);
		
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("assigneeEntryForm.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Assignee Entry Form");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}
		
		StoryFactory.getInstance().setSelectedStorySprintStatus(1);
	}
	
	@FXML
	public void handleUnassignButtonAction(ActionEvent event) {
		Window owner = unassignButton.getScene().getWindow();
		
		int selectedIndex = assignedLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from assigned backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(assignedLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
		
		StoryFactory.getInstance().setSelectedStory(story);
		StoryFactory.getInstance().setSelectedStorySprintStatus(0);
		StoryFactory.getInstance().setSelectedStoryAssignee("None");
	}
	
	@FXML
	public void handleInprogressButtonAction(ActionEvent event) {
		Window owner = inprogressButton.getScene().getWindow();
		
		int selectedIndex = assignedLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from assigned backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(assignedLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
		
		StoryFactory.getInstance().setSelectedStory(story);
		StoryFactory.getInstance().setSelectedStorySprintStatus(2);
	}
	
	@FXML
	public void handleNotInprogressButtonAction(ActionEvent event) {
		Window owner = notInprogressButton.getScene().getWindow();
		
		int selectedIndex = inprogressLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from inprogress backlog.");
			return;
		}
		
		//find story by title
		UserStory story = StoryFactory.getInstance().findByTitle(inprogressLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
		
		StoryFactory.getInstance().setSelectedStory(story);
		StoryFactory.getInstance().setSelectedStorySprintStatus(1);
	}
	
	@FXML
	public void handleDetailsButtonAction(ActionEvent event) {
		Window owner = detailsButton.getScene().getWindow();
		
		int selectedIndex = productBacklog.getSelectionModel().getSelectedIndex();
		UserStory story;
				
		if(selectedIndex == -1) {
			selectedIndex = SprintBacklog.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex == -1) {
				selectedIndex = completionLog.getSelectionModel().getSelectedIndex();
				
				if(selectedIndex == -1) {
					selectedIndex = waitingLog.getSelectionModel().getSelectedIndex();
					
					if(selectedIndex == -1) {
						selectedIndex = assignedLog.getSelectionModel().getSelectedIndex();
						
						if(selectedIndex == -1) {
							selectedIndex = inprogressLog.getSelectionModel().getSelectedIndex();
							
							if(selectedIndex == -1) {
								AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from a backlog.");
								return;
							}
							else {
								story = StoryFactory.getInstance().findByTitle(inprogressLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
							}
						}
						else {
							story = StoryFactory.getInstance().findByTitle(assignedLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
						}
					}
					else {
						story = StoryFactory.getInstance().findByTitle(waitingLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
					}
				}
				else {
					story = StoryFactory.getInstance().findByTitle(completionLog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
				}
			}
			else {
				story = StoryFactory.getInstance().findByTitle(SprintBacklog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);
			}
		}
		else {
			story = StoryFactory.getInstance().findByTitle(productBacklog.getSelectionModel().getSelectedItem().getText().split("\n")[0]);			
		}
		
		StoryFactory.getInstance().setSelectedStory(story);
				
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detailedView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Detailed View");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}
	}
	
	//wrapper func for observable class to call
	@Override
	public void update(Observable o, Object arg) {
		updateUI();
		StoryFactory.getInstance().saveState();
		updateServer();
	}
	
	public void updateServer() {
		try {
			outputToServer.writeObject("update");
			outputToServer.flush();
			
			inputFromServer1.readObject();

			outputToServer.writeObject(StoryFactory.getInstance().getAllLogs());
			outputToServer.flush();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateUI() {
		ObservableList<Label> backlog_titles = FXCollections.observableArrayList();
		ObservableList<Label> sprint_titles = FXCollections.observableArrayList();
		ObservableList<Label> finished_titles = FXCollections.observableArrayList();
		ObservableList<Label> waiting_titles = FXCollections.observableArrayList();
		ObservableList<Label> assigned_titles = FXCollections.observableArrayList();
		ObservableList<Label> inprogress_titles = FXCollections.observableArrayList();
		
		for(UserStory story : StoryFactory.getInstance().getProductBacklog()) {
			String text = String.format("%s\nAuthor: %s\n%d points", story.getTitle(), story.getAuthor(), story.getScore());			
			backlog_titles.add(makeLabel(text,story.getScore()));
		}
		
		for(UserStory story : StoryFactory.getInstance().getSprintBacklog()) {
			String text = String.format("%s\nAuthor: %s\nAssignee: %s\n%d points", story.getTitle(), story.getAuthor(), story.getAssignee(),  story.getScore());
			sprint_titles.add(makeLabel(text,story.getScore()));
			
			if(story.getSprintStatus() == 0){
				text = String.format("%s\nAuthor: %s\nAssignee: %s\n%d points", story.getTitle(), story.getAuthor(), story.getAssignee(),  story.getScore());
				waiting_titles.add(makeLabel(text,story.getScore()));
			}
			
			if(story.getSprintStatus() == 1){
				text = String.format("%s\nAuthor: %s\nAssignee: %s\n%d points", story.getTitle(), story.getAuthor(), story.getAssignee(),  story.getScore());
				assigned_titles.add(makeLabel(text,story.getScore()));
			}
			
			if(story.getSprintStatus() == 2){
				text = String.format("%s\nAuthor: %s\nAssignee: %s\n%d points", story.getTitle(), story.getAuthor(),  story.getAssignee(), story.getScore());
				inprogress_titles.add(makeLabel(text,story.getScore()));
			}
		}
		
	
		for(UserStory story : StoryFactory.getInstance().getcompletedLog()) {
			String text = String.format("%s\nAuthor: %s\nAssignee: %s\n%d points\nCompleted: Day %d", story.getTitle(), story.getAuthor(), story.getAssignee(), story.getScore(), story.getFinishDate());			
			finished_titles.add(makeLabel(text,story.getScore()));
		}
				
		productBacklog.setItems(backlog_titles);
		SprintBacklog.setItems(sprint_titles);
		completionLog.setItems(finished_titles);
		waitingLog.setItems(waiting_titles);
		assignedLog.setItems(assigned_titles);
		inprogressLog.setItems(inprogress_titles);
		
		updateGraph();
	}
	
	public void updateGraph() {
		int totalPoints = 0;
		XYChart.Series<Integer,Integer> series = new XYChart.Series<Integer,Integer>();
		TreeMap<Integer, Integer> sorted = new TreeMap<>();
		
		if(StoryFactory.getInstance().getProductBacklog() != null) {
			for(UserStory story : StoryFactory.getInstance().getProductBacklog()) {
				totalPoints += story.getScore();
			}
		}
		
		if(StoryFactory.getInstance().getSprintBacklog() != null) {
			for(UserStory story : StoryFactory.getInstance().getSprintBacklog()) {
				totalPoints += story.getScore();
			}
		}
		
		if(StoryFactory.getInstance().getcompletedLog() != null) {
			for(UserStory story : StoryFactory.getInstance().getcompletedLog()) {
				totalPoints += story.getScore();
				sorted.put((Integer)story.getFinishDate(), (Integer)story.getScore());
			}
		}
		
		int currentPoints = totalPoints;
		series.getData().add(new XYChart.Data<Integer, Integer>(0, 99));
		for(Entry<Integer, Integer> entry : sorted.entrySet()) {
			currentPoints -= entry.getValue();
			int percentDone = (int)Math.round(((double)currentPoints/(double)totalPoints)*100);
			series.getData().add(new XYChart.Data<Integer, Integer>(entry.getKey(), percentDone));			
		}
		
		burnDown.getData().clear();
		burnDown.getData().add(series);	
	}

	public Label makeLabel(String text,int score) {
		Label label = new Label(text);
		
		if(score <= 3) {
			label.setTextFill(Color.GREEN);
		}
		
		else if(score <= 6){
			label.setTextFill(Color.ORANGE);
		}
		
		else {
			label.setTextFill(Color.RED);
		}
		
		return label;
	}
	

}
