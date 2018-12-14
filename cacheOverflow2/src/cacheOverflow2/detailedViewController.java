package cacheOverflow2;

import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.fxml.Initializable;


public class detailedViewController implements Observer, Initializable{
	@FXML
	private Button saveButton;
		
	@FXML
	private Button addComment;
	
	@FXML
	private Button removeComment;

	@FXML
	private TextField titleField;
	
	@FXML
	private TextField authorField;
	
	@FXML
	private TextArea descriptionField;
	
	@FXML
	private Slider pointSlider;
	
	@FXML
	private ListView<String> commentLog;
	
	@FXML
	private Label statusLabel;
	
	@FXML
	private Label assigneeLabel;
	
	@FXML
	private Label dateLabel;
	
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		//add as observer
		StoryFactory.getInstance().addObserver(this);
		
		//update ui to load state
		UserStory story = StoryFactory.getInstance().getSelectedStory();
		updateUI(story);
    }
	
	public void handleSave(ActionEvent event) {
		//remove as observer
		StoryFactory.getInstance().deleteObserver(this);
				
		StoryFactory.getInstance().detailUpdate(titleField.getText(), authorField.getText(), descriptionField.getText(), (int)pointSlider.getValue());
		
		//close window
		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
	}

	public void handleAddComment(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCommentForm.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Add Comment");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			System.err.print(e);
		}
	}
	
	public void handleRemoveComment(ActionEvent event) {
		Window owner = removeComment.getScene().getWindow();

		String comment = commentLog.getSelectionModel().getSelectedItem();

		if(comment == null) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a comment to remove.");
			return;
		}
		
		StoryFactory.getInstance().removeCommentFromSelected(comment);
	}
	
	//wrapper function for observable class
	public void update(java.util.Observable o, Object arg) {
		updateUI(StoryFactory.getInstance().getSelectedStory());
	}
	
	public void updateUI(UserStory story) {
		titleField.setText(story.getTitle());
		authorField.setText(story.getAuthor());
		descriptionField.setText(story.getDescription());
		pointSlider.setValue((double)story.getScore());
		commentLog.setItems(story.getComments());
		
		switch(story.getSprintStatus()){
			case -1:
				statusLabel.setText("None");
			break;
		
			case 0:
				statusLabel.setText("Waiting");
			break;
			
			case 1:
				statusLabel.setText("Assigned");
			break;
				
			case 2:
				statusLabel.setText("In-Progress");
			break;
			
			case 3:
				statusLabel.setText("Done");
			break;
		}
		
		assigneeLabel.setText(story.getAssignee());
		
		if(story.getFinishDate() == -1) {
			dateLabel.setText("None");
		}
		
		else {
			dateLabel.setText(String.format("Day %d",story.getFinishDate()));
		}
	}
}

