package cacheOverflow2;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/* 
 * A wrapper of the scene.control.Alert object that makes
 * it easier to display a pop-up message to the user.
 */
public class AlertHelper {
	
	public static void showAlert(Alert.AlertType alertType, 
							     Window owner,
								 String title,
								 String message){

		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}
}

