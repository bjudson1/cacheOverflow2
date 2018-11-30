package cacheOverflow2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
		
		//display ui
		primaryStage.show();
		//start reading user input
		readInput();
	}
	
	public void readInput() {
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {	 
		    }
		});

	}

}
