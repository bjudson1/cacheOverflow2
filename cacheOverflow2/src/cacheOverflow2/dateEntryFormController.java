package cacheOverflow2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class dateEntryFormController {
	@FXML
	private Button submitButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Slider dateSlider;
	
	@FXML
	private Label dateLabel;
	
	@FXML
	// close the window on cancel click
	public void handleSubmitButton(ActionEvent event) {
		StoryFactory.getInstance().setSelectedStoryDate((int)Math.round(dateSlider.getValue()));
				
		Stage stage = (Stage) submitButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	// close the window on cancel click
	public void handleCancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void updateDateLabel(){
		dateLabel.setText(String.valueOf(Math.round(dateSlider.getValue())));
		System.out.println("hii");
	}
	
	
}
