package ewanstore;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatsController implements Initializable {
	private Stage stage = new Stage();
	@FXML private DatePicker fromDatePicker;
	@FXML private DatePicker toDatePicker;
	@FXML private Label totalWeightLabel;
	@FXML private Label totalPriceLabel;
	@FXML private LineChart weightChart;
	@FXML private LineChart priceChart;
	
	public void display(Parent root, ObservableList<Item> items) {
		updateStats(items);
		stage.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("ewanstore/res/style.css");
		root.getStyleClass().add("big");
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		stage.setTitle(rb.getString("stage"));
		
//		XYChart.Series quantSeries = new XYChart.Series();
		XYChart.Series weightSeries = new XYChart.Series();
		XYChart.Series priceSeries = new XYChart.Series();
		
		weightSeries.setName("");
		priceSeries.setName("");
		
		Calendar c = Calendar.getInstance();
		for (int i = 1; i <= 31; i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
			ObservableList<Item> items = ItemsHandler.getItems(date);
//			int sold = 0, weightGrams = 0, price = 0;
			int weightGrams = 0, price = 0;
			for (Item item : items) {
				if (!item.isPaid()) continue;
//				sold++;
				weightGrams += item.getWeight();
				price += item.getPrice();
			}
			double weight = weightGrams / 1000.0;
//			quantSeries.getData().add(new XYChart.Data(i, sold));
			weightSeries.getData().add(new XYChart.Data(i, weight));
			priceSeries.getData().add(new XYChart.Data(i, price));
		}
		
//		quantChart.getData().addAll(quantSeries);
		weightChart.getData().addAll(weightSeries);
		priceChart.getData().addAll(priceSeries);
	}
	
	public void updateStats(ObservableList<Item> items) {
//		int sold = 0, totalWeightGrams = 0, totalPrice = 0;
		int totalWeightGrams = 0, totalPrice = 0;
		double totalWeightKilos;
		for (Item item : items) {
			if (item.getPrice() < 0) {
				totalPrice += item.getPrice();
				continue;
			}
			if (!item.isPaid()) continue;
//			sold++;
			totalWeightGrams += item.getWeight();
			totalPrice += item.getPrice();
		}
		totalWeightKilos = totalWeightGrams / 1000.0;
		
		String locale = EwanStore.getLocale();
		if (locale.equals("en")) {
//			soldLabel.setText("Items Sold: "+ sold);
			totalWeightLabel.setText("Total Weight: "+ String.format("%.2f", totalWeightKilos) +" Kg ("+ totalWeightGrams +" gm)");
			totalPriceLabel.setText("Total Price: "+ totalPrice +" Pounds");
		} else if (locale.equals("ar")) {
//			soldLabel.setText("عدد: "+ sold);
			totalWeightLabel.setText("الوزن: "+ String.format("%.2f", totalWeightKilos) +" كيلو"+ " ("+ totalWeightGrams +" جرام"+ ")");
			totalPriceLabel.setText("السعر: "+ totalPrice +" جنية");			
		}
	}
	
	public void onDateChanged() {
		LocalDate from = fromDatePicker.getValue();
		LocalDate to = toDatePicker.getValue();
		if (from != null && to != null) {
			ObservableList<Item> rangeItems = FXCollections.observableArrayList();
			long days = DAYS.between(from, to);
			for (int i = 0; i <= days; i++) {
				LocalDate current = from.plusDays(i);
				String date = current.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				for (Item item : ItemsHandler.getItems(date)) {
					rangeItems.add(item);
				}
			}
			updateStats(rangeItems);
		} else if (to == null) {
			String date = from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			updateStats(ItemsHandler.getItems(date));
		} else if (from == null) {
			String date = to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			updateStats(ItemsHandler.getItems(date));
		}
	}
}
