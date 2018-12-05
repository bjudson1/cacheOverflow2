package cacheOverflow2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


public class assigneeEntryFormController {
	@FXML
	private Button submitButton;

	@FXML
	private TextField assigneeField;
	
	@FXML
	public void handleSubmit(ActionEvent event) {
		Window owner = submitButton.getScene().getWindow();

		if(assigneeField.getText().isEmpty()) {
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Must select a assignee.");
			return;
		}
		
		StoryFactory.getInstance().setSelectedStoryAssignee(assigneeField.getText());
		
		//close window
		Stage stage = (Stage) submitButton.getScene().getWindow();
		stage.close();
	}
}

