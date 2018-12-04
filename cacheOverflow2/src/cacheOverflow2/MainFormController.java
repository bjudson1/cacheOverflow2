package cacheOverflow2;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class MainFormController implements Observer{
	@FXML
	private Button addButton;

	@FXML
	private Button sprintButton;

	@FXML
	private Button finishButton;

	@FXML
	private Button removeButton;
	
	@FXML
	private ListView<String> productBacklog;
	
	@FXML
	private ListView<String> SprintBacklog;
	
	//add as observer to factory
	public MainFormController() {
		StoryFactory.getInstance().addObserver(this);
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
	protected void handleFinishButtonAction(ActionEvent event) {
		Window owner = finishButton.getScene().getWindow();
		
		int selectedStory = SprintBacklog.getSelectionModel().getSelectedIndex();
		
		if(selectedStory == -1) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from the sprint backlog.");
			return;
		}
		
		StoryFactory.getInstance().finishStory(selectedStory);
	}

	@FXML
	protected void handleRemoveButtonAction(ActionEvent event) {
		Window owner = finishButton.getScene().getWindow();
		int correspondingLog = 0;
		
		int selectedStory = productBacklog.getSelectionModel().getSelectedIndex();
		
		if(selectedStory == -1) {
			selectedStory = SprintBacklog.getSelectionModel().getSelectedIndex();

			if(selectedStory == -1) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a story from a backlog.");
				return;
			}
			
			correspondingLog = 1;
		}
		
		StoryFactory.getInstance().removeStory(selectedStory,correspondingLog);		
	}

	@Override
	public void update(Observable o, Object arg) {
		ObservableList<String> backlog_titles = FXCollections.observableArrayList();
		ObservableList<String> sprint_titles = FXCollections.observableArrayList();

		for(UserStory story : StoryFactory.getInstance().getProductBacklog()) {
			backlog_titles.add(String.format("%s | %s | %d", story.getTitle(), story.getAuthor(), story.getScore()));
		}
		
		for(UserStory story : StoryFactory.getInstance().getSprintBacklog()) {
			sprint_titles.add(String.format("%s | %s | %d", story.getTitle(), story.getAuthor(), story.getScore()));
		}
		
		productBacklog.setItems(backlog_titles);
		SprintBacklog.setItems(sprint_titles);
	}

}
