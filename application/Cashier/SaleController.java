package application.Cashier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


import application.database;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import net.sf.jasperreports.engine.JRException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import application.*;
import application.Classes.*;
public class SaleController implements Initializable {

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private ResultSet result2;
	private Statement statement;

	Alert alert;

	@FXML
	private TextField total2;

	@FXML
	private TableView<SaleLineView> mu_tableView;

	@FXML
	private TextField pro_id;

	@FXML
	private TextField pro_quantity;

	@FXML
	private Button sale_add;

	@FXML
	private TextField sale_amount;

	@FXML
	private TextField sale_balance;

	@FXML
	private TableColumn<SaleLineView, String> sale_col_category;

	@FXML
	private TableColumn<SaleLineView, String> sale_col_id;

	@FXML
	private TableColumn<SaleLineView, String> sale_col_name;

	@FXML
	private TableColumn<SaleLineView, String> sale_col_price;

	@FXML
	private TableColumn<SaleLineView, String> sale_col_quantity;

	@FXML
	private Button sale_home;

	@FXML
	private Button sale_pay;

	@FXML
	private Button sale_receipt;

	@FXML
	private Button sale_reset;

	@FXML
	private TextField sale_total;

	@FXML
	private ComboBox<String> payment_method;

	@FXML
	private Label label_amount;

	@FXML
	private Label label_balance;

	@FXML
	private Label label_total;

	@FXML
	private TextField card_cvv;

	@FXML
	private DatePicker card_expiry;

	@FXML
	private TextField card_number;

	@FXML
	private Label label_cardNo;

	@FXML
	private Label label_cvv;

	@FXML
	private Label label_expiry;

	private double grandTotal;

	@FXML
	private TextField customer_cnic;

	@FXML
	private TextField customer_name;

	private LocalDateTime now;

	private Product product = new Product();

	Sale sale = new Sale();

	private ObservableList<SaleLineView> productList = FXCollections.observableArrayList();

	private ObservableList<SaleLineItem> saleLineList = FXCollections.observableArrayList();

	public void addSaleButton() {

		// GenrateSaleLineId();
		// GenrateSaleId();

		boolean miss = false;

		if (pro_id.getText().isEmpty() || pro_quantity.getText().isEmpty()) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Product Id or Quantity is missing!");
			alert.showAndWait();

			miss = true;
		}

		if (!miss) {

			SaleLineItem saleLine;

			boolean check = false;
			boolean check2 = true;

			String productId = pro_id.getText();

			System.out.print("\n Product ID: " + productId);

			String quan = pro_quantity.getText();

			int quantity = 0;

			if (!quan.isEmpty()) {
				quantity = Integer.parseInt(quan);
			} else {

				System.out.print("\n Quantity NOT ENTERED");
			}

			System.out.print("\n Quantity: " + quantity);

			String prodSpec = "Select *from Product where productId = " + productId;

			connect = database.connect();

			try {

				prepare = connect.prepareStatement(prodSpec);
				result = prepare.executeQuery();

				while (result.next()) {

					product = new Product(result.getInt("productId"), result.getString("productName"),
							result.getString("category"), result.getDouble("price"), result.getInt("quantity"));

					if (product.getQuantity() <= 0) {
						check2 = false;

						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Out of Stock!");
						alert.showAndWait();

						pro_id.setText("");
						pro_quantity.setText("");
					}

				}

				product.printProductSpec();

			}

			catch (Exception e) {
				e.printStackTrace();
			}

			if (product.getProductId() == 0) {

				check = false;
				System.out.print("\n Product NOT Found!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Product not Found!");
				alert.showAndWait();

				pro_id.setText("");
				pro_quantity.setText("");

			} else {
				check = true;
				System.out.print("\n Product Found!");

			}

			if (check && check2) {

				double itemTotal = quantity * product.getPrice();

				System.out.print("\nTotal: " + itemTotal);

				saleLine = new SaleLineItem(saleLineId, Integer.parseInt(productId), saleId, quantity, itemTotal);

				saleLineList.add(saleLine);

				now = LocalDateTime.now();

				// sale = new Sale(saleId,now,itemTotal);

				sale.setSaleId(saleId);
				sale.setSaleTime(now);
				sale.setTotal(itemTotal);

				sale.addSale(saleLine);
				sale.printList();

				grandTotal = sale.calculateTotal();

				total2.setText(String.valueOf(grandTotal));
				sale_total.setText(String.valueOf(grandTotal));

				SaleLineView saleView = new SaleLineView(product.getProductId(), product.getProductName(),
						product.getCategory(), quantity, itemTotal);
				productList.add(saleView);

				pro_id.setText("");
				pro_quantity.setText("");
				showSaleLineData();

				saleLineId++;

			}

		}
	}

	public void payButton() {
		Payment cash;
		Payment credit;

		boolean check = false;

		boolean check2 = true;

		connect = database.connect();

		String insertIntoCustomer = "Insert into Customer Values (?,?,?,?,?,?,?,?)";
		String insertIntoSale = "Insert into Sale values (?,?,?)";

		System.out.println("\n\n SIZE2: " + productList.size());

		try {

			// INSERTING INTO SALE

			now = LocalDateTime.now();

			prepare = connect.prepareStatement(insertIntoSale);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String formattedDateTime = now.format(formatter);

			prepare.setInt(1, saleId);
			prepare.setString(2, formattedDateTime);
			prepare.setDouble(3, grandTotal);

			int rowsAffected = prepare.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("SALE added successfully.");
			} else {
				System.out.println("Failed to add Sale.");
			}

			// PAYMENT METHODS

			System.out.println("\n\n SIZE3: " + productList.size());

			if ("Cash".equals(payment_method.getSelectionModel().getSelectedItem())) {
				if (customer_name.getText().isEmpty() || customer_cnic.getText().isEmpty()
						|| sale_amount.getText().isEmpty()) {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("Please fill all the fields");
					alert.showAndWait();
					check2 = false;

				} else {

					double amount = Double.parseDouble(sale_amount.getText());

					if (amount < grandTotal) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Amount Error!");
						alert.showAndWait();
						check2 = false;
					} else {

						cash = new Cash(customer_name.getText(), customer_cnic.getText(), saleId, grandTotal,
								Double.parseDouble(sale_amount.getText()));

						// Inserting into Customer:

						prepare = connect.prepareStatement(insertIntoCustomer);

						prepare.setInt(1, saleId);
						prepare.setString(2, customer_name.getText());
						prepare.setString(3, customer_cnic.getText());
						prepare.setString(4, "NA");
						prepare.setString(5, "NA");
						prepare.setString(6, "NA");
						prepare.setDouble(7, grandTotal);
						prepare.setString(8, "Cash");

						rowsAffected = prepare.executeUpdate();

						if (rowsAffected > 0) {
							alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Information!");
							alert.setHeaderText(null);
							alert.setContentText("Sale Success");
							alert.showAndWait();

							System.out.println("Customer Info added successfully.");

							check = true;

						} else {
							System.out.println("Failed to add Sale.");
						}

					}
				}
			} else if ("Credit Card".equals(payment_method.getSelectionModel().getSelectedItem())) {
				if (customer_name.getText().isEmpty() || customer_cnic.getText().isEmpty()
						|| card_number.getText().isEmpty() || card_cvv.getText().isEmpty()
						|| card_expiry.getValue() == null) {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("Please fill all the fields");
					alert.showAndWait();

					check2 = false;

				} else {

					credit = new CreditCard(customer_name.getText(), customer_cnic.getText(), saleId, grandTotal,
							card_number.getText(), card_expiry.getValue(), card_cvv.getText());

					// Inserting into Customer:

					prepare = connect.prepareStatement(insertIntoCustomer);

					prepare.setInt(1, saleId);
					prepare.setString(2, customer_name.getText());
					prepare.setString(3, customer_cnic.getText());
					prepare.setString(4, card_number.getText());
					prepare.setString(5, card_expiry.getValue().toString());
					prepare.setString(6, card_cvv.getText());
					prepare.setDouble(7, grandTotal);
					prepare.setString(8, "Credit Card");

					rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information!");
						alert.setHeaderText(null);
						alert.setContentText("Sale Success");
						alert.showAndWait();

						System.out.println("Customer Info added successfully.");

						check = true;

					} else {
						System.out.println("Failed to add Sale.");
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nCheck: " + check);
		System.out.println("\n\n SIZE4: " + productList.size());

		if (check) {

			GenrateSaleLineId();

			///////////////////////////////////////////////////////////////////////////////////////////////////
			/////////////////////////////// INSERTING INTO SALELINE ////////////////////////

			SaleLineItem items1;

			for (int i = 0; i < saleLineList.size(); i++) {

				items1 = saleLineList.get(i);

				String insertIntoSaleLineItem = "Insert into SalelineItem values (?,?,?,?,?)";

				connect = database.connect();

				try {

					prepare = connect.prepareStatement(insertIntoSaleLineItem);

					prepare.setInt(1, items1.getSaleLineItemId());
					prepare.setInt(2, items1.getProductId());
					prepare.setInt(3, items1.getSaleId());
					prepare.setInt(4, items1.getQuantity());
					prepare.setDouble(5, items1.getAmount());

					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {

						System.out.println("Product added to SaleLine successfully.");
					} else {
						System.out.println("Failed to add to SaleLine.");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			System.out.println("\n\n SIZE: " + saleLineList.size());

			///////////////////////////////////////////////

			System.out.println("\n\n Updating Quantity Block");

			connect = database.connect();

			SaleLineView items;

			System.out.println("\n\n SIZE5: " + productList.size());

			for (int i = 0; i < productList.size(); i++) {
				items = productList.get(i);
				int total = product.getQuantity();
				int ordered = items.getQuantity();

				System.out.println("\n i: " + i);

				String updateQuantiy = "UPDATE Product " + "SET quantity = " + (total - ordered) + " where productId = "
						+ items.getProductId();

				try {

					statement = connect.createStatement();
					statement.executeUpdate(updateQuantiy);

//					prepare = connect.prepareStatement(updateQuantiy);
//					result = prepare.executeQuery();

				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}

			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/////////////////////////////// INSERTING INTO RECEIPT
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////

			String receipt = "Insert into Receipt values (?,?,?,?,?,?,?,?,?) ";

			for (int i = 0; i < productList.size(); i++) {
				items = productList.get(i);
				items1 = saleLineList.get(i);

				try {

					prepare = connect.prepareStatement(receipt);

					prepare.setInt(1, items1.getSaleId());
					prepare.setString(2, customer_name.getText());
					prepare.setString(3, customer_cnic.getText());
					prepare.setInt(4, items.getProductId());
					prepare.setString(5, items.getProductName());
					prepare.setString(6, items.getCategory());
					prepare.setInt(7, items.getQuantity());
					prepare.setDouble(8, items.getPrice());
					prepare.setDouble(9, grandTotal);

					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						System.out.print("Receipt Added");
					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (check2) {
			resetButton();
			grandTotal = 0;
			sale.clearList();
			GenrateSaleId();
			GenrateSaleLineId();
			productList.clear();
			saleLineList.clear();
		}

	}

	public void BalanceCalculation() {

		if ("Cash".equals(payment_method.getSelectionModel().getSelectedItem())) {
			double amount = Double.parseDouble(sale_amount.getText());

			double balance = amount - grandTotal;

			if (balance <= 0 || amount <= 0)
				sale_balance.setText("0.00");

			sale_balance.setText(String.valueOf(balance));
		}

	}

	String[] methods = { "Cash", "Credit Card" };

	public void addMethodsList() {
		List<String> rL = new ArrayList<>();

		for (String data : methods) {
			rL.add(data);
		}

		ObservableList<String> ListData = FXCollections.observableArrayList(rL);

		payment_method.setItems(ListData);

		System.out.print("\nPayment Methods Set");
	}

	public void resetButton() {
		pro_id.setText("");
		pro_quantity.setText("");
		sale_balance.setText("");
		sale_amount.setText("");
		sale_total.setText("0.00");
		total2.setText("0.00");
		customer_name.setText("");
		customer_cnic.setText("");
		customer_name.setText("");
		card_number.setText("");
		card_cvv.setText("");
		card_expiry.setPromptText("");

		GenrateSaleId();

		String query = "delete from SaleLineItem where saleId = " + saleId;

		connect = database.connect();

		try {

			statement = connect.createStatement();
			statement.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		productList.clear();

	}

	public void paymentMethod() {

		System.out.print("\n" + payment_method.getSelectionModel().getSelectedItem());

		if ("Cash".equals(payment_method.getSelectionModel().getSelectedItem())) {
			System.out.print("\n In Cash Block!");

			// CASH

			sale_amount.setVisible(true);
			sale_balance.setVisible(true);
			sale_total.setVisible(true);

			label_total.setVisible(true);
			label_balance.setVisible(true);
			label_amount.setVisible(true);

			// CREDIT CARD:
			card_number.setVisible(false);
			card_expiry.setVisible(false);
			card_cvv.setVisible(false);

			label_expiry.setVisible(false);
			label_cvv.setVisible(false);
			label_cardNo.setVisible(false);

//            double grandTotal = sale.calculateTotal();

//            sale_total.setText(String.valueOf(grandTotal));
		} else if ("Credit Card".equals(payment_method.getSelectionModel().getSelectedItem())) {
			System.out.print("\n In Credit Card Block!");

			// CREDIT CARD:
			card_number.setVisible(true);
			card_expiry.setVisible(true);
			card_cvv.setVisible(true);

			label_expiry.setVisible(true);
			label_cvv.setVisible(true);
			label_cardNo.setVisible(true);

			// CASH
			sale_amount.setVisible(false);
			sale_balance.setVisible(false);
			sale_total.setVisible(false);

			label_total.setVisible(false);
			label_balance.setVisible(false);
			label_amount.setVisible(false);
		}
	}

	private int saleLineId;
	private int saleId;

	public void GenrateSaleLineId() {
		String query = "Select * from SaleLineItem";

		connect = database.connect();

		int id = 0;

		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();

			while (result.next()) {
				id = result.getInt("saleLineItemId");
			}

			if (id == 0) {
				saleLineId += 1;
			}

			saleLineId = id;
			saleLineId++;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void GenrateSaleId() {
		String query = "Select * from Sale";

		connect = database.connect();

		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();

			while (result.next()) {
				saleId = result.getInt("saleId");
			}

			if (saleId == 0) {
				saleId += 1;
			} else {
				saleId += 1;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showSaleLineData() {

		sale_col_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
		sale_col_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		sale_col_category.setCellValueFactory(new PropertyValueFactory<>("category"));
		sale_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		sale_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		mu_tableView.setItems(productList);

	}

	public void generateReceipt() {
		GenrateSaleId();

		HashMap hash = new HashMap();

		// Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("Invoice", (saleId - 1));

		try {

//			if(grandTotal == 0)
//			{
//				alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Error!");
//				alert.setHeaderText(null);
//				alert.setContentText("Invalid!");
//				alert.showAndWait();
//			}
//			else
//			{
			JasperDesign jDesign = JRXmlLoader
					.load("C:\\Users\\irtiq\\eclipse-workspace\\Project\\src\\application\\Cashier\\Receipt.jrxml");
			JasperReport jReport = JasperCompileManager.compileReport(jDesign);

			JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, connect);

			JasperViewer.viewReport(jPrint, false);
			// }

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void DivertToHome()
    {
		sale_home.getScene().getWindow().hide();
		
        Parent root;
		try {
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
		// TODO Auto-generated method stub

		addMethodsList();

		GenrateSaleLineId();
		GenrateSaleId();

		System.out.print("\n Sale ID: " + saleId);
		System.out.print("\n SaleLine ID: " + saleLineId);

		// System.setProperty("java.library.path",
		// System.getProperty("java.library.path") + ";C:\\Program
		// Files\\Java\\sqljdbc_12.4\\enu\\auth\\x64");

		String javaLibraryPath = System.getProperty("java.library.path");
		System.out.println("\n\njava.library.path: " + javaLibraryPath);

		// System.load("C:\\Program Files (x86)\\Microsoft SQL
		// Server\\160\\Tools\\Binn\\ntlmauth.dll");

//		card_number = new TextField();
//		card_expiry = new DatePicker();
//		card_cvv = new TextField();
//		
//		label_expiry = new Label();
//		label_cvv = new Label();
//		label_cardNo = new Label();

	}

}
