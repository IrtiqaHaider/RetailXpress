package application.Manager;

import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import application.*;
import application.Classes.*;

public class ManageInventoryController implements Initializable {

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private Statement statement;


    @FXML
    private TableView<Product> mu_tableView;

    @FXML
    private Button pro_add;

    @FXML
    private ComboBox<String> pro_category;

    @FXML
    private Button pro_clear;

    @FXML
    private TableColumn<Product, String> pro_col_category;

    @FXML
    private TableColumn<Product, String> pro_col_id;

    @FXML
    private TableColumn<Product, String> pro_col_name;

    @FXML
    private TableColumn<Product, String> pro_col_price;

    @FXML
    private TableColumn<Product, String> pro_col_quantity;

    @FXML
    private Button pro_delete;

    @FXML
    private Button pro_home;

    @FXML
    private TextField pro_id;

    @FXML
    private TextField pro_name;

    @FXML
    private TextField pro_price;

    @FXML
    private TextField pro_quantity;

    @FXML
    private Button pro_update;

   

	private String[] categories = { "Snacks", "Beverages", "Canned Goods" , "Dairy" , "Electronics" , "Personal Care" , "Toys and Games"};

	private Alert alert;

	public void addProductBtn() {

		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (pro_id.getText().isEmpty() || pro_name.getText().isEmpty() || pro_category.getSelectionModel().getSelectedItem() == null 
					||	pro_price.getText().isEmpty() || pro_quantity.getText().isEmpty()) {

				System.out.print("\nEmpty Field Block!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				String checkId = "Select productId from Product where productId = " + pro_id.getText();

				System.out.print("\nChecking Product Id...");

				prepare = connect.prepareStatement(checkId);
				result = prepare.executeQuery();

				if (result.next()) {
					System.out.print("\n Product ALREADY EXIST!!");

					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("Product" + pro_id.getText() + " arleady exists");
					alert.showAndWait();
				} else {
					System.out.print("\n ID OKAY!!!");
					System.out.print("\nChecking Product Name...");

					String checkProductname = "Select productName from Product where productName = '" + pro_name.getText() + "'";

					prepare = connect.prepareStatement(checkProductname);
					result = prepare.executeQuery();

					if (result.next()) {
						System.out.print("\n Product Name already exist!");

						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Product: " + pro_name.getText() + " arleady exists");
						alert.showAndWait();
					} else {
						System.out.print("\nProduct Okay!");

						System.out.print("\nInserting into DB!");

						String insert = "Insert into Product (productId,productName,category,price,quantity)" + " values(?,?,?,?,?)";

						prepare = connect.prepareStatement(insert);
						prepare.setString(1, pro_id.getText());
						prepare.setString(2, pro_name.getText());
						prepare.setString(3, pro_category.getSelectionModel().getSelectedItem());
						prepare.setString(4, pro_price.getText());
						prepare.setString(5, pro_quantity.getText());

						int rowsAffected = prepare.executeUpdate();

						if (rowsAffected > 0) {
							alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Information!");
							alert.setHeaderText(null);
							alert.setContentText("Product Added Successfully");
							alert.showAndWait();
							
							System.out.println("Product added successfully.");
						} else {
							System.out.println("Failed to add Product.");
						}

						showProductData();
						clearDataBtn();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateProductButton() {
		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (pro_id.getText().isEmpty() || pro_name.getText().isEmpty() || pro_category.getSelectionModel().getSelectedItem() == null 
					||	pro_price.getText().isEmpty() || pro_quantity.getText().isEmpty()) {

				System.out.print("\nEmpty Field Block!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation!");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to UPDATE Product with Id: " + pro_id.getText());
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					String update = "UPDATE Product " + "SET productName = '" + pro_name.getText() + "'" + ", category = '"
							+ pro_category.getSelectionModel().getSelectedItem() + "'" + ", price = " +  pro_price.getText() 
							+ ", quantity = " + pro_quantity.getText()  
							 + " where productId = " + pro_id.getText();
					prepare = connect.prepareStatement(update);
					
					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information!");
						alert.setHeaderText(null);
						alert.setContentText("Product Updated Successfully");
						alert.showAndWait();
						System.out.println("Product updated successfully.");
					} else {
						System.out.println("Failed to update Product.");
					}

					showProductData();
					clearDataBtn();
				}
				else
				{
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Information!");
					alert.setHeaderText(null);
					alert.setContentText("Update Aborted!");
					alert.showAndWait();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteProductBtn()
	{
		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (pro_id.getText().isEmpty() )
			{

				System.out.print("\nEmpty Field Block!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Please enter Product Id");
				alert.showAndWait();
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation!");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to DELETE Product Id: " + pro_id.getText());
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					String delete = "DELETE from Product where productId = " + pro_id.getText();
					prepare = connect.prepareStatement(delete);
					
					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information!");
						alert.setHeaderText(null);
						alert.setContentText("Product Deleted Successfully");
						alert.showAndWait();
						System.out.println("Product Deleted successfully.");
					} else {
						System.out.println("Failed to delete Product.");
					}

					showProductData();
					clearDataBtn();
				}
				else
				{
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Information!");
					alert.setHeaderText(null);
					alert.setContentText("Deletion Aborted!");
					alert.showAndWait();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearDataBtn()
	{
		pro_id.setText("");
		pro_name.setText("");
		pro_category.getSelectionModel().clearSelection();
		pro_category.setPromptText("");
		pro_price.setText("");
		pro_quantity.setText("");
}

	public void CategoryList() {
		List<String> rL = new ArrayList<>();

		for (String data : categories) {
			rL.add(data);
		}

		ObservableList<String> ListData = FXCollections.observableArrayList(rL);

		pro_category.setItems(ListData);

		System.out.print("\nItems Set");

	}

	public ObservableList<Product> ProductList() {

		ObservableList<Product> listData = FXCollections.observableArrayList();

		String selectData = "Select *from Product";

		connect = database.connect();

		try {

			prepare = connect.prepareStatement(selectData);
			result = prepare.executeQuery();

			Product Product;

			while (result.next()) 
			{
				Product = new Product (result.getInt("productId"), result.getString("productName"), result.getString("category") 
						  , result.getDouble("price") , result.getInt("quantity") );

				listData.add(Product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listData;

	}

	private ObservableList<Product> ProductData;

	public void showProductData() {
		ProductData = ProductList();

		pro_col_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
		pro_col_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		pro_col_category.setCellValueFactory(new PropertyValueFactory<>("category"));
		pro_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		pro_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));		

		mu_tableView.setItems(ProductData);
		
	}

	public void ProductSelectData() {

		System.out.print("\n Selected a Row in the Table!");

		Product uData = mu_tableView.getSelectionModel().getSelectedItem();
		int num = mu_tableView.getSelectionModel().getSelectedIndex();

		System.out.print("\n Selected Index: " + num);

		if ((num - 1) < -1)
			return;

		pro_id.setText(String.valueOf(uData.getProductId()));
		pro_name.setText(uData.getProductName());
		pro_category.setPromptText(uData.getCategory());
		pro_price.setText(String.valueOf(uData.getPrice())); 
		pro_quantity.setText(String.valueOf(uData.getQuantity())); 	
	}

	 public void DivertToHome()
	    {
		 pro_home.getScene().getWindow().hide();
			
	        Parent root;
			try {
				
				System.out.println("Working Directory: " + System.getProperty("user.dir"));

				root = FXMLLoader.load(getClass().getResource("Home.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CategoryList();
		showProductData();
	}

}
