package cacheOverflow2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class RegistrationFormController {
	@FXML
	private TextField nameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Button submitButton;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event){
		Window owner = submitButton.getScene().getWindow();
		/* 
		 * Make sure the user entered all of the text fields. If they
		 * did not, display an error message.
		 */
		if(nameField.getText().isEmpty()){
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
							 	  "Please enter your name");
			return;
		}
		
		if(passwordField.getText().isEmpty()){
			AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
							 	  "Please enter your password");
			return;
		}
	
		/* The user enetered all of their information correctly */	
		AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Authentication Successfull!",
							  "Welcome " + nameField.getText());

	}
}
