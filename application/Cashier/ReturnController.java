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
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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

public class ReturnController implements Initializable {

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private ResultSet result2;
	private Statement statement;

	Alert alert;

	@FXML
	private TextField invoice_id;

	@FXML
	private TableView<Customer> mu_tableView;

	@FXML
	private Button receiptBtn;

	@FXML
	private Button resetBtn;

	@FXML
	private Button returnBtn;

	@FXML
	private TableColumn<Customer, String> return_col_amount;

	@FXML
	private TableColumn<Customer, String> return_col_cnic;

	@FXML
	private TableColumn<Customer, String> return_col_id;

	@FXML
	private TableColumn<Customer, String> return_col_name;

	@FXML
	private Button return_home;

	@FXML
	private Button sale_add;

	@FXML
	private TextField total2;

	private ObservableList<Customer> saleList = FXCollections.observableArrayList();

	private Customer customer = new Customer();

	private int returnId = 0;

	public void addButton() {

		boolean miss = false;

		if (invoice_id.getText().isEmpty()) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Enter Sale Id!");
			alert.showAndWait();

			miss = true;
		}

		if (!miss) {

			boolean check = false;
			boolean check2 = true;

			String saleId = invoice_id.getText();

			System.out.print("\n Sale ID: " + invoice_id);

			String prodSpec = "Select *from Customer where saleId = " + saleId;
			String returnMatch = "Select *from ReturnSale where InvoiceId = " + saleId;

			connect = database.connect();

			try {

				prepare = connect.prepareStatement(returnMatch);
				result = prepare.executeQuery();

				if (result.next()) {

					check2 = false;

					System.out.print("\n INVOICE ALREADY RETURNED: " + invoice_id);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (check2) {

				try {

					prepare = connect.prepareStatement(prodSpec);
					result = prepare.executeQuery();

					while (result.next()) {

						customer = new Customer(result.getInt("saleId"), result.getString("customerName"),
								result.getString("customerCNIC"), result.getDouble("totalAmount"));

					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}

				if (customer.getSaleId() == 0) {
					check = false;

					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("Invoice doesn't exist!");
					alert.showAndWait();

					invoice_id.setText("");

				} else {
					check = true;
					System.out.print("\n Invoice Found!");

				}

				System.out.print("\n Sale ID: " + customer.getSaleId());

				if (check) {

					saleList.add(customer);
					total2.setText(String.valueOf(customer.getTotalAmount()));
					showSaleLineData();
					invoice_id.setEditable(false);
					invoice_id.setText("");
					sale_add.setDisable(true);
				}

			} else {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Invoice already returned!");
				alert.showAndWait();
				invoice_id.setText("");
				
			}

		}
	}

	public void returnButton() {

		connect = database.connect();

		String insertIntoReturn = "Insert into ReturnSale Values (?,?,?,?,?,?)";

		boolean check = true;

		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation!");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to Return Product from Invoice: " + customer.getSaleId());
		Optional<ButtonType> option = alert.showAndWait();

		if (option.get().equals(ButtonType.OK)) {

			try {

				LocalDateTime now = LocalDateTime.now();

				prepare = connect.prepareStatement(insertIntoReturn);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				String formattedDateTime = now.format(formatter);

				prepare.setInt(1, returnId);
				prepare.setInt(2, customer.getSaleId());
				prepare.setString(3, customer.getCustomerName());
				prepare.setString(4, customer.getCustomerCNIC());
				prepare.setDouble(5, customer.getTotalAmount());
				prepare.setString(6, formattedDateTime);

				int rowsAffected = prepare.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("Return added successfully.");
				} else {
					check = false;
					System.out.println("Failed to add Sale.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		if (check) {
			resetButton();
		}

	}

	public void GenrateReturnId() {
		String query = "Select * from ReturnSale";

		connect = database.connect();

		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();

			while (result.next()) {
				returnId = result.getInt("ReturnId");
			}

			if (returnId == 0) {
				returnId += 1;
			} else {
				returnId += 1;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resetButton() {
		invoice_id.setText("");
		total2.setText("0.00");
		invoice_id.setEditable(true);
		sale_add.setDisable(false);
		saleList.clear();

	}

	public void showSaleLineData() {

		return_col_id.setCellValueFactory(new PropertyValueFactory<>("saleId"));
		return_col_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		return_col_cnic.setCellValueFactory(new PropertyValueFactory<>("customerCNIC"));
		return_col_amount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

		mu_tableView.setItems(saleList);

	}

	public void generateReceipt() {

		HashMap hash = new HashMap();

		// Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("Invoice", (returnId));

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
					.load("C:\\Users\\irtiq\\eclipse-workspace\\Project\\src\\application\\Cashier\\ReturnReceipt.jrxml");
			JasperReport jReport = JasperCompileManager.compileReport(jDesign);

			JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, connect);

			JasperViewer.viewReport(jPrint, false);
			// }

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DivertToHome() {
		return_home.getScene().getWindow().hide();

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
		GenrateReturnId();

	}

}
