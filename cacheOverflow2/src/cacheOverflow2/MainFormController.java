package cacheOverflow2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainFormController {
	@FXML
	private Button addButton;
	
	@FXML
	private Button sprintButton;
	
	@FXML
	private Button finishButton;
	
	@FXML
	private Button removeButton;
	
	@FXML
	protected void handleAddButtonAction(ActionEvent event){
		System.out.println("gg");
		
	}
	
	@FXML
	protected void handleSprintButtonAction(ActionEvent event){
		System.out.println("aa");
		
	}
	
	@FXML
	protected void handleFinishButtonAction(ActionEvent event){
		System.out.println("dd");
		
	}
	
	@FXML
	protected void handleRemoveButtonAction(ActionEvent event){
		System.out.println("ff");
		
	}

}
