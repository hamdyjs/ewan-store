package ewanstore;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class DatabaseHandler {
	private static Connection connection;
	
	public static void connect() {
		try {
			File dir = EwanStore.getProjectDirectory();
			String path = dir.getPath();
			connection = DriverManager.getConnection("jdbc:sqlite:"+ path +"/items.db");
			
			Statement st = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT, kilo INTEGER NOT NULL, weight INTEGER NOT NULL, price INTEGER NOT NULL, date TEXT NOT NULL, comment TEXT NOT NULL, name TEXT NOT NULL, paid BOOLEAN NOT NULL)";
			st.execute(sql);
			st.close();
		} catch (SQLException ex) {Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);}
		System.out.println("Successfully connected to the database");
	}
	
	public static ObservableList<Item> getItems() {
		ObservableList<Item> items = FXCollections.observableArrayList(new Callback<Item, Observable[]>() {
			@Override
			public Observable[] call(Item item) {
				return new Observable[]{item.kiloProperty(), item.weightProperty(), item.priceProperty(), item.dateProperty(), item.commentProperty(), item.nameProperty(), item.paidProperty()};
			}
		});
		
		try {
			Statement st = connection.createStatement();
			String sql = "SELECT * FROM items";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt(1);
				int kilo = rs.getInt(2);
				int weight = rs.getInt(3);
				int price = rs.getInt(4);
				String date = rs.getString(5);
				String comment = rs.getString(6);
				String name = rs.getString(7);
				boolean paid = rs.getBoolean(8);
				Item item = new Item(id, kilo, weight, price, date, comment, name, paid);
				items.add(item);
			}
			st.close();
		} catch (SQLException ex) {Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);}
		
		return items;
	}
	
	public static int add(int kilo, int weight, int price, String date, String comment, String name, boolean paid) {
		try {
			String sql = "INSERT INTO items (kilo, weight, price, date, comment, name, paid) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, kilo);
			st.setInt(2, weight);
			st.setInt(3, price);
			st.setString(4, date);
			st.setString(5, comment);
			st.setString(6, name);
			st.setBoolean(7, paid);
			st.execute();
			ResultSet rs = st.getGeneratedKeys();
			st.close();
			int id = rs.getInt(1);
			return id;
		} catch (SQLException ex) {Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);}
		return 0;
	}
	
	public static void delete(int id) {
		try {
			String sql = "DELETE FROM items WHERE id = ?";
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, id);
			st.execute();
			st.close();
		} catch (SQLException ex) {Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);}
	}
	
	public static void update(Item item) {
		try {
			String sql = "UPDATE items SET kilo = ?, weight = ?, price = ?, date = ?, comment = ?, name = ?, paid = ? WHERE id = ?";
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, item.getKilo());
			st.setInt(2, item.getWeight());
			st.setInt(3, item.getPrice());
			st.setString(4, item.getDate());
			st.setString(5, item.getComment());
			st.setString(6, item.getName());
			st.setBoolean(7, item.isPaid());
			st.setInt(8, item.getId());
			st.execute();
			st.close();
		} catch (SQLException ex) {Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);}
	}
	
	public static void update() {
		ObservableList<Item> items = ItemsHandler.getAllItems();
		try {
			String sql = "UPDATE items SET kilo = ?, weight = ?, price = ?, date = ?, comment = ?, name = ?, paid = ? WHERE id = ?";
			PreparedStatement st = connection.prepareStatement(sql);
			
			for (Item item : items) {
				st.setInt(1, item.getKilo());
				st.setInt(2, item.getWeight());
				st.setInt(3, item.getPrice());
				st.setString(4, item.getDate());
				st.setString(5, item.getComment());
				st.setString(6, item.getName());
				st.setBoolean(7, item.isPaid());
				st.setInt(8, item.getId());
				st.addBatch();
			}
			
			st.executeBatch();
			st.close();
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}