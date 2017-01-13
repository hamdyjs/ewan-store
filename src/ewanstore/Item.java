package ewanstore;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Item {
	private final int id;
	private final IntegerProperty kilo;
	private final IntegerProperty weight;
	private final IntegerProperty price;
	private final StringProperty date;
	private final StringProperty comment;
	private final StringProperty name;
	private final BooleanProperty paid;

	public Item(int id, int kilo, int weight, int price, String date, String comment, String name, boolean paid) {
		this.id = id;
		this.kilo = new SimpleIntegerProperty(kilo);
		this.weight = new SimpleIntegerProperty(weight);
		this.price = new SimpleIntegerProperty(price);
		this.date = new SimpleStringProperty(date);
		this.comment = new SimpleStringProperty(comment);
		this.name = new SimpleStringProperty(name);
		this.paid = new SimpleBooleanProperty(paid);
	}
	
	public void setComment(String comment) {
		this.comment.set(comment);
	}
	
	public void setPaid(boolean paid) {
		this.paid.set(paid);
	}
	
	public int getId() {
		return id;
	}
	
	public IntegerProperty kiloProperty() {
		return kilo;
	}
	
	public int getKilo() {
		return kilo.get();
	}
	
	public IntegerProperty weightProperty() {
		return weight;
	}
	
	public int getWeight() {
		return weight.get();
	}
	
	public IntegerProperty priceProperty() {
		return price;
	}
	
	public int getPrice() {
		return price.get();
	}
	
	public StringProperty dateProperty() {
		return date;
	}
	
	public String getDate() {
		return date.get();
	}
	
	public StringProperty commentProperty() {
		return comment;
	}
	
	public String getComment() {
		return comment.get();
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}
	
	public BooleanProperty paidProperty() {
		return paid;
	}
	
	public Boolean isPaid() {
		return paid.get();
	}
}