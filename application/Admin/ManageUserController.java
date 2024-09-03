package application.Admin;

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

public class ManageUserController implements Initializable {

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private Statement statement;

	@FXML
	private Button mu_add;

	@FXML
	private Button mu_clear;

	@FXML
	private TableColumn<Users, String> mu_col_id;

	@FXML
	private TableColumn<Users, String> mu_col_pass;

	@FXML
	private TableColumn<Users, String> mu_col_role;

	@FXML
	private TableColumn<Users, String> mu_col_user;

	@FXML
	private Button mu_delete;

	@FXML
	private Button mu_home;

	@FXML
	private TextField mu_id;

	@FXML
	private TextField mu_pass;

	@FXML
	private TableView<Users> mu_tableView;

	@FXML
	private Button mu_update;

	@FXML
	private TextField mu_user;

	@FXML
	private ComboBox<String> mu_roles;

	private String[] roles = { "Cashier", "Manager", "Admin" };

	private Alert alert;

	public void addUserBtn() {

		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (mu_id.getText().isEmpty() || mu_user.getText().isEmpty() || mu_pass.getText().isEmpty()
					|| mu_roles.getSelectionModel().getSelectedItem() == null) {

				System.out.print("\nEmpty Field Block!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				String checkId = "Select userId from Users where userId = " + mu_id.getText();

				System.out.print("\nChecking Id...");

				prepare = connect.prepareStatement(checkId);
				result = prepare.executeQuery();

				if (result.next()) {
					System.out.print("\n ID ALREADY EXIST!!");

					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("User Id: " + mu_id.getText() + " is arleady taken");
					alert.showAndWait();
				} else {
					System.out.print("\n ID OKAY!!!");
					System.out.print("\nChecking Username...");

					String checkUsername = "Select username from Users where username = '" + mu_user.getText() + "'";

					prepare = connect.prepareStatement(checkUsername);
					result = prepare.executeQuery();

					if (result.next()) {
						System.out.print("\nUser already exist!");

						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Username: " + mu_user.getText() + " is arleady taken");
						alert.showAndWait();
					} else {
						System.out.print("\nUser Okay!");

						System.out.print("\nInserting into DB!");

						String insert = "Insert into Users (userId,username,password,role)" + " values(?,?,?,?)";

						prepare = connect.prepareStatement(insert);
						prepare.setString(1, mu_id.getText());
						prepare.setString(2, mu_user.getText());
						prepare.setString(3, mu_pass.getText());
						prepare.setString(4, mu_roles.getSelectionModel().getSelectedItem());

						int rowsAffected = prepare.executeUpdate();

						if (rowsAffected > 0) {
							alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Information!");
							alert.setHeaderText(null);
							alert.setContentText("User Added Successfully");
							alert.showAndWait();
							
							System.out.println("User added successfully.");
						} else {
							System.out.println("Failed to add user.");
						}

						showUserData();
						clearDataBtn();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateButton() {
		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (mu_id.getText().isEmpty() || mu_user.getText().isEmpty() || mu_pass.getText().isEmpty()
					|| mu_roles.getSelectionModel().getSelectedItem() == null) {

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
				alert.setContentText("Are you sure you want to UPDATE User Id: " + mu_id.getText());
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					String update = "UPDATE Users " + "SET username = '" + mu_user.getText() + "'" + ", password = '"
							+ mu_pass.getText() + "'" + ", role = '" + mu_roles.getSelectionModel().getSelectedItem()
							+ "'" + " where userId = " + mu_id.getText();
					prepare = connect.prepareStatement(update);
					
					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information!");
						alert.setHeaderText(null);
						alert.setContentText("User Updated Successfully");
						alert.showAndWait();
						System.out.println("User updated successfully.");
					} else {
						System.out.println("Failed to add user.");
					}

					showUserData();
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
	
	public void DeleteUserBtn()
	{
		System.out.print("\nConnecting...");
		connect = database.connect();
		System.out.print("\nConnected!");

		try {

			if (mu_id.getText().isEmpty() )
			{

				System.out.print("\nEmpty Field Block!");

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Please enter User Id");
				alert.showAndWait();
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation!");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to DELETE User Id: " + mu_id.getText());
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					String update = "DELETE from Users where userId = " + mu_id.getText();
					prepare = connect.prepareStatement(update);
					
					int rowsAffected = prepare.executeUpdate();

					if (rowsAffected > 0) {
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information!");
						alert.setHeaderText(null);
						alert.setContentText("User Deleted Successfully");
						alert.showAndWait();
						System.out.println("User updated successfully.");
					} else {
						System.out.println("Failed to add user.");
					}

					showUserData();
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
		mu_id.setText("");
		mu_user.setText("");
		mu_pass.setText("");
		mu_roles.getSelectionModel().clearSelection();
		mu_roles.setPromptText("");
	}

	public void roleList() {
		List<String> rL = new ArrayList<>();

		for (String data : roles) {
			rL.add(data);
		}

		ObservableList<String> ListData = FXCollections.observableArrayList(rL);

		mu_roles.setItems(ListData);

		System.out.print("\nItems Set");

	}

	public ObservableList<Users> userList() {

		ObservableList<Users> listData = FXCollections.observableArrayList();

		String selectData = "Select *from Users";

		connect = database.connect();

		try {

			prepare = connect.prepareStatement(selectData);
			result = prepare.executeQuery();

			Users user;

			while (result.next()) {
				user = new Users(result.getInt("userId"), result.getString("username"), result.getString("password"),
						result.getString("role"));

				listData.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listData;

	}

	private ObservableList<Users> userData;

	public void showUserData() {
		userData = userList();

		mu_col_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
		mu_col_user.setCellValueFactory(new PropertyValueFactory<>("username"));
		mu_col_pass.setCellValueFactory(new PropertyValueFactory<>("password"));
		mu_col_role.setCellValueFactory(new PropertyValueFactory<>("role"));

		mu_tableView.setItems(userData);

	}
	
	public void DivertToHome()
    {
		mu_home.getScene().getWindow().hide();
		
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

	public void userSelectData() {

		System.out.print("\n Selected a Row in the Table!");

		Users uData = mu_tableView.getSelectionModel().getSelectedItem();
		int num = mu_tableView.getSelectionModel().getSelectedIndex();

		System.out.print("\n Selected Index: " + num);

		if ((num - 1) < -1)
			return;

		mu_id.setText(String.valueOf(uData.getUserId()));
		mu_user.setText(uData.getUsername());
		mu_pass.setText(uData.getPassword());
		mu_roles.setPromptText(uData.getRole()); 
		
		
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		roleList();
		showUserData();
	}

}
