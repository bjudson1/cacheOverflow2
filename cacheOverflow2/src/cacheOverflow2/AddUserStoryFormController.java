package cacheOverflow2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AddUserStoryFormController {
	@FXML
	private Button createButton;

	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField titleField;
	
	@FXML
	private TextField authorField;
	
	@FXML
	private TextArea descriptionField;
	
	@FXML
	private Slider pointSlider;
	
	@FXML
	public void handleCreateButton(ActionEvent event) {
		Window owner = createButton.getScene().getWindow();

		//check if required fields empty
		if(titleField.getText().isEmpty()) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please a title.");
			return;
		}
		
		//check if required fields empty
		if(authorField.getText().isEmpty()) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please an author.");
			return;
		}
		
		
		//create new story
		StoryFactory.getInstance().addStory(titleField.getText(), authorField.getText(), descriptionField.getText(), (int)pointSlider.getValue());

		//close window
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	// close the window on cancel click
	public void handleCancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}


}
