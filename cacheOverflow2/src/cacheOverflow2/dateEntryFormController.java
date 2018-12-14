package cacheOverflow2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class dateEntryFormController implements Initializable{
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
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		dateSlider.valueProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
			    dateLabel.textProperty().setValue(String.format("Day: %d",(int) dateSlider.getValue()));			    
			}
	    });
	}
}

	
