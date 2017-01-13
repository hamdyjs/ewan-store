package ewanstore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import java.util.Calendar;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

public class ItemsHandler {
	private static ObservableList<Item> items;
	
	public static void initItems() {
		items = DatabaseHandler.getItems();
		items.addListener(new ListChangeListener<Item>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Item> c) {
				while (c.next()) {
					if (c.wasUpdated()) {
						int id = c.getFrom();
						Item item = items.get(id);
						DatabaseHandler.update(item);
					}
				}
			}
		});
	}
	
	public static ObservableList<Item> getAllItems() {
		return items;
	}
	
	public static ObservableList<Item> getItems() {
		return getItems(null);
	}
	
	public static ObservableList<Item> getItems(String date) {
		ObservableList<Item> filteredItems = FXCollections.observableArrayList();
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Calendar c = Calendar.getInstance();
			if (date != null) {
				date += " 00:00";
				c.setTime(format.parse(date));
			}
			Calendar cItem = Calendar.getInstance();
			for (Item item : items) {
				cItem.setTime(format.parse(item.getDate()));
				if (isDateSame(cItem, c)) {
					filteredItems.add(item);
				}
			}
				} catch (ParseException ex) {
			Logger.getLogger(ItemsHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return filteredItems;
	}
		
	public static void add(Item item) {
		items.add(item);
	}
	
	public static void remove(Item item) {
		items.remove(item);
	}
	
	private static boolean isDateSame(Calendar c1, Calendar c2) {
    return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && 
            c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
            c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}
}