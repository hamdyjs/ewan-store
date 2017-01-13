package ewanstore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class NameBox {
//	private static String name = "";
	private static StringProperty nameProperty = new SimpleStringProperty();
	private static NameBoxController nameBoxController;
	
	public static String getName() {
//		return name;
		return nameProperty.get();
	}
	
	public static StringProperty getNameProperty() {
		return nameProperty;
	}
	
	public static void setName(String newName) {
//		name = newName;
		nameProperty.set(newName);
	}

	public static void display() {
		try {
			FXMLLoader loader = EwanStore.loadFXML("namebox.fxml", "properties.namebox", EwanStore.getLocale());
			Parent root = loader.load();
			nameBoxController = loader.getController();
			nameBoxController.display(root);
		} catch (IOException ex) {
			Logger.getLogger(NameBox.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void save() {
		try {
			File dir = EwanStore.getProjectDirectory();
			File nameFile = new File(dir, "name");
			if (!nameFile.exists()) nameFile.createNewFile();
			FileWriter fw = new FileWriter(nameFile);
//			fw.write(name);
			fw.write(nameProperty.get());
			fw.close();
		} catch (IOException ex) {Logger.getLogger(NameBox.class.getName()).log(Level.SEVERE, null, ex);}
	}
	
	public static void load() {
		try {
			File dir = EwanStore.getProjectDirectory();
			File nameFile = new File(dir, "name");
			if (!nameFile.exists()) return;
			Scanner scanner = new Scanner(nameFile);
			String content = "";
			while(scanner.hasNext()) {
				content += scanner.next();
			}
//			name = content;
			nameProperty.set(content);
			scanner.close();
		} catch (IOException ex) {Logger.getLogger(NameBox.class.getName()).log(Level.SEVERE, null, ex);}
	}
}