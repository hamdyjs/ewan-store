package ewanstore;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class EwanStore extends Application {
	/**
	 * Application's current version
	 */
	private static final String PROJECT_VERSION = "0.2.2";
	
	/**
	 * Application's directory where any data is stored
	 */
	private static File projectDirectory;
	
	/**
	 * Main stage
	 */
	private static Stage stage;
	private static String locale = "ar";
	private static MainController mainController;
	
	public static void main(String[] args) {
		String home = System.getProperty("user.home");
		projectDirectory = new File(home, "Ewan Store");
		projectDirectory.mkdir();
		
		DatabaseHandler.connect();
		ItemsHandler.initItems();
		NameBox.load();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(Locale.ENGLISH);
		try {
			stage = primaryStage;
			
			FXMLLoader loader = loadFXML("main.fxml", "properties.main", locale);
			Parent root = loader.load();
			mainController = loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("ewanstore/res/style.css");
			root.getStyleClass().add("huge");
			
			stage.setScene(scene);
			stage.setTitle("Ewan Store v"+ PROJECT_VERSION);
			stage.getIcons().add(new Image("ewanstore/res/img/icon.png"));
			stage.setOnCloseRequest(e -> {
				e.consume();
				close();
			});
			stage.show();
			
			mainController.displayNameBox();
		} catch (IOException ex) {
			Logger.getLogger(EwanStore.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Loads the FXML layout file with the path given
	 * @param path Path to the FXML file
	 * @return Loaded root object
	 */
	public static Parent loadFXML(String path) {
		try {
			Parent root = FXMLLoader.load(EwanStore.class.getResource(path));
			return root;
		} catch (Exception e) {}
		return null;
	}
	
	/**
	 * Loads the FXML layout file with the path given along with the resource and the locale
	 * @param path Path to the FXML file
	 * @param resource Resource to attach to the FXML file
	 * @param locale Two letters locale for localization
	 * @return Loaded root object
	 */
	public static FXMLLoader loadFXML(String path, String resource, String locale) {
		try {
			FXMLLoader loader = new FXMLLoader(EwanStore.class.getResource(path), ResourceBundle.getBundle("ewanstore.res."+ resource, new Locale(locale)));
			return loader;
		} catch (Exception e) {}
		return null;
	}
	
	/**
	 * Gets the current locale
	 * @return Two letter locale
	 */
	public static String getLocale() {
		return locale;
	}

	/**
	 * Changes the locale of the whole stage to the specified locale
	 * @param locale Two letter locale to change the stage to
	 */
	public static void setLocale(String locale) {
		EwanStore.locale = locale;
		Scene scene = stage.getScene();
		try {
			Parent root = loadFXML("main.fxml", "properties.main", locale).load();
			root.getStyleClass().add("huge");
			scene.setRoot(root);
		} catch (IOException ex) {}
	}
	
	/**
	 * Returns the directory where the project's files are stored
	 * @return The project's directory
	 */
	public static File getProjectDirectory() {
		return projectDirectory;
	}
	
	/**
	 * Closes the application manually in order to commit changes to the database
	 */
	public static void close() {
//		DatabaseHandler.update();
		NameBox.save();
		stage.close();
	}
}