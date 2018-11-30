package cacheOverflow2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;

public class UI extends Application {
	private AnchorPane anchorPane;
	private Scene scene;

	public UI() {
	}
	
	public static void run(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		anchorPane = new AnchorPane();
		
		//create scene
		scene = new Scene(anchorPane, 1000,600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SCRUM Board");
		
		//setup the gui
		createGUI();
		
		//display ui
		primaryStage.show();
		//start reading user input
		readInput();
	}
	
	public void createGUI(){
		//add buttons
		Button backlogButton = new Button("Backlog");
		backlogButton.setMinWidth(50);
		backlogButton.setMaxWidth(150);
		backlogButton.setPrefWidth(250);
						
		backlogButton.setMinHeight(25);
		backlogButton.setMaxHeight(50);
		backlogButton.setPrefHeight(50);
						
		backlogButton.setLayoutY(25);
		backlogButton.setLayoutX(50);
					
		backlogButton.setOnAction(value ->  {
			System.out.println("yo");
		});
		
		Button sprintButton = new Button("Sprint");
		sprintButton.setMinWidth(50);
		sprintButton.setMaxWidth(150);
		sprintButton.setPrefWidth(250);
				
		sprintButton.setMinHeight(25);
		sprintButton.setMaxHeight(50);
		sprintButton.setPrefHeight(50);
				
		sprintButton.setLayoutY(25);
		sprintButton.setLayoutX(425);
			
		sprintButton.setOnAction(value ->  {
			System.out.println("yo");
		});
		
		Button burndownButton = new Button("Burndown");
		burndownButton.setMinWidth(50);
		burndownButton.setMaxWidth(150);
		burndownButton.setPrefWidth(250);
						
		burndownButton.setMinHeight(25);
		burndownButton.setMaxHeight(50);
		burndownButton.setPrefHeight(50);
						
		burndownButton.setLayoutY(25);
		burndownButton.setLayoutX(800);
					
		burndownButton.setOnAction(value ->  {
			System.out.println("yo");
		});
		
		anchorPane.getChildren().add(sprintButton);
		anchorPane.getChildren().add(burndownButton);
		anchorPane.getChildren().add(backlogButton);
		
		//add backlog area
		Label backlogLabel = new Label("Backlog");
		backlogLabel.setLayoutX(50);
		backlogLabel.setLayoutY(75);
		Rectangle backlogArea = new Rectangle(50,100,200,450);
		backlogArea.setFill(Color.WHITE);
		anchorPane.getChildren().add(backlogArea);
		anchorPane.getChildren().add(backlogLabel);
		
		//add sprint backlog area
		Label sprintBacklogLabel = new Label("Sprint Backlog");
		sprintBacklogLabel.setLayoutX(300);
		sprintBacklogLabel.setLayoutY(75);
		Rectangle sprintBacklogArea = new Rectangle(300,100,200,450);
		sprintBacklogArea.setFill(Color.WHITE);
		anchorPane.getChildren().add(sprintBacklogArea);
		anchorPane.getChildren().add(sprintBacklogLabel);


	}
	
	public void readInput() {
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {	 
		    }
		});

	}

}
