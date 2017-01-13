package ewanstore;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class MainController implements Initializable {
	@FXML private MenuItem enMenuItem;
	@FXML private MenuItem arMenuItem;
	@FXML private TableView<Item> itemsTable;
	@FXML private TableColumn kiloColumn;
	@FXML private TableColumn weightColumn;
	@FXML private TableColumn priceColumn;
	@FXML private TableColumn dateTimeColumn;
	@FXML private TableColumn commentColumn;
	@FXML private TableColumn nameColumn;
	@FXML private TableColumn paidColumn;
	@FXML private TextField kiloText;
	@FXML private TextField weightText;
	@FXML private TextField priceText;
	@FXML private TextField commentText;
	@FXML private Label nameLabel;
	@FXML private CheckBox paidCheckBox;
	@FXML private CheckBox retCheckBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		kiloColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(12));
		weightColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(12));
		priceColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(12));
		dateTimeColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(6));
		commentColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(2.5));
		nameColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(10));
		paidColumn.prefWidthProperty().bind(itemsTable.widthProperty().divide(15));
		
		kiloColumn.setCellValueFactory(new PropertyValueFactory<>("kilo"));
		kiloColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter() {
			@Override
			public String toString(Number value) {
				if (value.intValue() == 0) return "";
				else return super.toString(value);
			}

			@Override
			public Number fromString(String value) {
				if (value.equals("")) return 0;
				else return super.fromString(value);
			}
		}));
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
		weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		dateTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
		commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
		paidColumn.setCellFactory(new Callback<TableColumn<Item, Boolean>, TableCell<Item, Boolean>>() {
			@Override
			public TableCell<Item, Boolean> call(TableColumn<Item, Boolean> param) {
				return new CheckBoxTableCell<Item, Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						
						TableRow row = getTableRow();
						if (!isEmpty()) {
							if (item) {
								row.setStyle("");
							} else {
								row.setStyle("-fx-background-color: #FF9696;");
							}
						}
					}
				};
			}
		});

		weightText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> e, String o, String n) {
				try {
					String kiloString = kiloText.getText();
					if (n.isEmpty() || kiloString.isEmpty()) return;
					Double kilo = Double.parseDouble(kiloString);
					Double weight = Double.parseDouble(n);
					Double price = (weight / 1000) * kilo;
					if (retCheckBox.isSelected()) price = -price;
					priceText.setText(String.valueOf(roundPrice(price)));
				} catch (Exception exception){}
			}
		});
		
		kiloText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> e, String o, String n) {
				try {
					String weightString = weightText.getText();
					if (n.isEmpty() || weightString.isEmpty()) return;
					Double kilo = Double.parseDouble(n);
					Double weight = Double.parseDouble(weightString);
					Double price = (weight / 1000) * kilo;
					if (retCheckBox.isSelected()) price = -price;
					priceText.setText(String.valueOf(roundPrice(price)));
				} catch (Exception exception){}
			}
		});
		
		priceText.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.isEmpty()) return;
					int price = Integer.parseInt(newValue);
					if (price > 0 && retCheckBox.isSelected()) {
						price = -price;
						priceText.setText(String.valueOf(price));
					} else if (price < 0 && !retCheckBox.isSelected()) {
						price = Math.abs(price);
						priceText.setText(String.valueOf(price));
					}
				} catch (Exception ex) {}
			}
		});

//		ObservableList<Item> list = ItemsHandler.getItems(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		ObservableList<Item> list = ItemsHandler.getItems(DateController.getDate());
		itemsTable.setItems(list);
		sortItemsTable();
		
//		String locale = EwanStore.getLocale();
//		if (locale == "en") nameLabel.setText("Name: "+ NameBox.getName());
//		else if (locale == "ar") nameLabel.setText("الاسم: "+ NameBox.getName());
		
		nameLabel.textProperty().bind(NameBox.getNameProperty());
	}
	
	public void onLangMenuItemClick(ActionEvent e) {
		Object src = e.getSource();
		if (src == enMenuItem) {
			EwanStore.setLocale("en");
		} else if(src == arMenuItem) {
			EwanStore.setLocale("ar");
		}
	}
	
	public void onPricingKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			Object src = event.getSource();
			if (src == kiloText) {
				weightText.requestFocus();
			} else if (src == weightText) {
				priceText.requestFocus();
			} else if (src == priceText) {
				commentText.requestFocus();
			} else if (src == commentText) {
				kiloText.requestFocus();
			}
			event.consume();
		}
	}
	
	public void onStatisticsMenuItemClick() {
		try {
			FXMLLoader loader = EwanStore.loadFXML("stats.fxml", "properties.stats", EwanStore.getLocale());
			Parent root = loader.load();
			StatsController controller = loader.getController();
			controller.display(root, itemsTable.getItems());
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void onChangeNameMenuItemClick() {
		displayNameBox();
	}
	
	public void onExitMenuItemClick(ActionEvent e) {
		EwanStore.close();
	}
	
	public void onAddButtonClick() {
		try {
			String kiloString = kiloText.getText();
			String weightString = weightText.getText();
			String priceString = priceText.getText();
			if (weightString.isEmpty() || priceString.isEmpty()) return;
			int kilo = 0;
			if (!kiloString.isEmpty()) kilo = Integer.parseInt(kiloString);
			int weight = Integer.parseInt(weightString);
			int price = Integer.parseInt(priceString);
			String comment = commentText.getText();
			String name = NameBox.getName();
			String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			boolean paid = paidCheckBox.isSelected();
			
			int id = DatabaseHandler.add(kilo, weight, price, date, comment, name, paid);
			
			Item item = new Item(id, kilo, weight, price, date, comment, name, paid);
			ItemsHandler.add(item);
			itemsTable.getItems().add(item);
			itemsTable.scrollTo(item);
			sortItemsTable();
			
			kiloText.clear();
			weightText.clear();
			priceText.clear();
			commentText.clear();
			paidCheckBox.setSelected(true);
		} catch (Exception e) {Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);}
	}
	
	public void onDeleteButtonClick() {
		Item item = itemsTable.getSelectionModel().getSelectedItem();
		if (item == null) return;
		DatabaseHandler.delete(item.getId());
		ItemsHandler.remove(item);
		itemsTable.getItems().remove(item);
	}
	
	public void onItemsTableKeyPressed(KeyEvent e) {
		if (e.getCode().equals(KeyCode.DELETE)) {
			onDeleteButtonClick();
		}
	}
	
	public void onRetCheckBoxSelected() {
		String comment = commentText.getText();
		try {
			if (retCheckBox.isSelected()) {
				commentText.setText(comment.isEmpty() ? "استرجاع" : comment + " - استرجاع");
				String priceString = priceText.getText();
				int price = Integer.parseInt(priceString);
				if (price > 0) price = -price;
				priceText.setText(String.valueOf(price));
			} else {
				if (comment.equals("استرجاع")) commentText.setText("");
				else commentText.setText(comment.replace(" - استرجاع", ""));
				String priceString = priceText.getText();
				int price = Integer.parseInt(priceString);
				if (price < 0) price = Math.abs(price);
				priceText.setText(String.valueOf(price));
			}
		} catch (Exception e) {}
	}
	
	public void onDateMenuItemClick() {
		try {
			Stage stage = new Stage();
			stage.setTitle("Date");
			stage.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader loader = EwanStore.loadFXML("date.fxml", "properties.date", EwanStore.getLocale());
			Parent root = loader.load();
			DateController controller = loader.getController();
			controller.display(root);
			String date = controller.getDate();
			ObservableList<Item> items = ItemsHandler.getItems(date);
			itemsTable.setItems(items);
			itemsTable.refresh();
			sortItemsTable();
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void displayNameBox() {
		NameBox.display();
//		String locale = EwanStore.getLocale();
//		if (locale == "en") nameLabel.setText("Name: "+ NameBox.getName());
//		else if (locale == "ar") nameLabel.setText("الاسم: "+ NameBox.getName());
	}
	
	private int roundPrice(Double priceDouble) {
		int price = (int) Math.round(priceDouble);
		int rem = price % 10;
		if (rem < 3) price = price - rem;
		else if (rem < 5) price = price + (5 - rem);
		else if (rem > 5 && rem < 8) price = price - (rem - 5);
		else if (rem > 7 && rem < 10) price = price + (10 - rem);
		if (price == 0) price = 5;
		return price;
	}
	
	private void sortItemsTable() {
		itemsTable.getSortOrder().clear();
		itemsTable.getSortOrder().add(dateTimeColumn);
	}
	
	public ObservableList<Item> getItems() {
		return itemsTable.getItems();
	}
}