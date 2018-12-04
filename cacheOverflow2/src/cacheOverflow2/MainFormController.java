package cacheOverflow2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.annotation.Resources;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Pair;

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
	private Button unfinishButton;

	@FXML
	private Button removeButton;
	
	@FXML
	private ListView<String> productBacklog;
	
	@FXML
	private ListView<String> SprintBacklog;
	
	@FXML
	private ListView<String> completionLog;
	
	@FXML
	private ListView<String> waitingLog;
	
	@FXML
	private ListView<String> assignedLog;
	
	@FXML
	private ListView<String> inprogressLog;
	
	@FXML
	private LineChart<Integer, Integer> burnDown;
	
	
	//add as observer to factory
	public MainFormController() throws ClassNotFoundException {		
		//load state from disk
		StoryFactory.getInstance().loadState();
		StoryFactory.getInstance().addObserver(this);
	}
	
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		//update ui to load state
		updateUI();
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
				
		StoryFactory.getInstance().unsprintStory(selectedStory);
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
			stage.setTitle("Add User Story");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}

		StoryFactory.getInstance().finishStory(selectedIndex);
	}
	
	@FXML
	protected void handleUnfinishButtonAction(ActionEvent event) {
		
		System.out.println("heyhey");

		Window owner = unfinishButton.getScene().getWindow();
		int selectedIndex = completionLog.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the completion backlog.");
			return;
		}
		
		//set selected story
		StoryFactory.getInstance().setSelectedStory(StoryFactory.getInstance().getSprintBacklog().get(selectedIndex));
		//clear finish date
		StoryFactory.getInstance().getSelectedStory().setFinishDate(0);
		//move story
		StoryFactory.getInstance().unfinishStory(selectedIndex);
	}

	@FXML
	protected void handleRemoveButtonAction(ActionEvent event) {
		Window owner = finishButton.getScene().getWindow();
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

	//wrapper func for observable class to call
	@Override
	public void update(Observable o, Object arg) {
		updateUI();
		StoryFactory.getInstance().saveState();
	}
	
	public void updateUI() {
		ObservableList<String> backlog_titles = FXCollections.observableArrayList();
		ObservableList<String> sprint_titles = FXCollections.observableArrayList();
		ObservableList<String> finished_titles = FXCollections.observableArrayList();
		
		for(UserStory story : StoryFactory.getInstance().getProductBacklog()) {
			backlog_titles.add(String.format("%s\n%s | %d points", story.getTitle(), story.getAuthor(), story.getScore()));
		}
		
		for(UserStory story : StoryFactory.getInstance().getSprintBacklog()) {
			sprint_titles.add(String.format("%s\n%s | %d points", story.getTitle(), story.getAuthor(), story.getScore()));
		}
		
		for(UserStory story : StoryFactory.getInstance().getcompletedLog()) {
			finished_titles.add(String.format("%s\n%s | %d points\nCompleted: Day %d", story.getTitle(), story.getAuthor(), story.getScore(), story.getFinishDate()));
		}
		
		productBacklog.setItems(backlog_titles);
		SprintBacklog.setItems(sprint_titles);
		completionLog.setItems(finished_titles);
				
		updateGraph();
	}
	
	public void updateGraph() {
		int totalPoints = 0;
		List<Pair<Integer, Integer>> finishInfo = new ArrayList<Pair<Integer, Integer>>();
		//ObservableList<XYChart<X, Y>.Data<X, Y>> data = FXCollections.observableArrayList();
		
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
				finishInfo.add(new Pair((Integer)story.getFinishDate(),(Integer)story.getScore()));
			}
		}
		
		//sort first
		
		XYChart.Series<Integer,Integer> series = new XYChart.Series<Integer,Integer>();
		for(int i=0;i<30;i++) {
			series.getData().add(new XYChart.Data<Integer, Integer>(i, i));
			//System.out.println(new XYChart.Data<Integer, Integer>(i, i));
			//data.add(new Pair<Integer,Integer>(i,totalPoints));
		}
		
		//System.out.println(series.getData());
		//burnDown.getData().add(series.getData());
		
		/*int finishDate;
		for(Pair<Integer, Integer> info : finishInfo) {
			finishDate = info.getKey();
			
			for(int i=finishDate;i<30;i++) {
				data.set(index, element)data.get(i) - info.getValue();
			}
			System.out.println(info);
		}
		
		System.out.println("yooo");
		
		
		
		burnDown.setData(data);*/
	}

	

}
