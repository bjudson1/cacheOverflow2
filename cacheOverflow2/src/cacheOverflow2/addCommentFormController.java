package cacheOverflow2;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class addCommentFormController {
	@FXML
	private TextArea comment;

	@FXML
	private Button saveButton;
	
	@FXML
	private Button cancelButton;

	@FXML
	protected void handleSaveButtonAction(ActionEvent event) {		
		StoryFactory.getInstance().addCommentToSelected(comment.getText());
		
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	protected void handleCancelButtonAction(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
}
