package cacheOverflow2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("XML/registration_form.fxml"));
		primaryStage.setTitle("Registration Form FXML Application");
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();
	}

	public static void run(String[] args){
		launch(args);
	}
}
