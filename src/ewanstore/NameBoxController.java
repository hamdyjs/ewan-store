package ewanstore;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NameBoxController implements Initializable {
	private Stage stage = new Stage();
	@FXML private TextField nameText;
	
	public void display(Parent root) {
		nameText.setText(NameBox.getName());
		stage.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("ewanstore/res/style.css");
		root.getStyleClass().add("huge");
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		stage.setTitle(resources.getString("stage"));
	}
	
	public void onChangeButtonClick() {
		NameBox.setName(nameText.getText());
		stage.close();
	}
	
	public void onCancelButtonClick() {
		stage.close();
	}
}