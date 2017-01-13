package ewanstore;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DateController implements Initializable {
	@FXML private DatePicker datePicker;
	private final Stage stage = new Stage();
	private static String date;
	
	public void display(Parent root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add("ewanstore/res/style.css");
		root.getStyleClass().add("huge");
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		datePicker.setValue(LocalDate.now());
		stage.setTitle(rb.getString("stage"));
	}	
	
	public void onAcceptButtonClick() {
		LocalDate d = datePicker.getValue();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		date = formatter.format(d);
		stage.close();
	}
	
	public void onCancelButtonClick() {
		stage.close();
	}
	
	public static String getDate() {
		return date;
	}
}
