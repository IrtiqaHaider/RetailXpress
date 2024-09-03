package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FxmlController implements Initializable {

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    @FXML
    private Button ip_login;

    @FXML
    private TextField ip_password;

    @FXML
    private TextField ip_username;

    @FXML
    private BorderPane login;

    public void loginAccount() {
        String sql = "SELECT *from Users where username = ? and password = ?";
        
        connect = database.connect();
        
        if (connect != null) {
            System.out.println("Connection n0t null");

        } else {
            System.out.println("Connection is null. Check your credentials or database settings.");
        }
      
        try {
            Alert alert;

            if (connect==null || ip_username.getText().isEmpty() || ip_password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields!");
                alert.showAndWait();
            } else {
            	
            	System.out.print("\n Entered Actual Block");

                prepare = connect.prepareStatement(sql);
                
            	System.out.print("\n Connect Prepare Statement");

                prepare.setString(1, ip_username.getText());
                prepare.setString(2, ip_password.getText());

            	System.out.print("\nExecuting Query");

                result = prepare.executeQuery();
                
            	System.out.print("\n Query Executed");

                if (result.next()) {
                	
                	String role = result.getString("role");
                	System.out.print("\n Role: " + role);

                	
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successfully!");
                    alert.showAndWait();
                    
                    ip_login.getScene().getWindow().hide();
                    
                    Parent root = null;
                    
                    if(role.equalsIgnoreCase("Manager"))
                    {
             
                    	URL url = getClass().getResource("Manager\\Home.fxml");
            			root = FXMLLoader.load(url);
            			
                        //root = FXMLLoader.load(getClass().getResource("Manager\\Home.fxml"));
                    }
                    else if(role.equalsIgnoreCase("Admin"))
                    {
                    	
                    	URL url = getClass().getResource("Admin\\Home.fxml");
            			root = FXMLLoader.load(url);
            			
                        //root = FXMLLoader.load(getClass().getResource("ManageUser.fxml"));
                    }
                    else if(role.equalsIgnoreCase("Cashier"))
                    {
                    	
                    	URL url = getClass().getResource("Cashier\\Home.fxml");
            			root = FXMLLoader.load(url);
            			
                    	//root = FXMLLoader.load(getClass().getResource("Sale.fxml"));
                    }
                    else
                    {
                    	System.out.print("\n Not Matched");
                    }
                    		
                    Stage stage = new Stage();
        			Scene scene = new Scene(root);
        			stage.setScene(scene);
        			stage.show();
        			
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
        	System.out.print("\nError loading data");
            e.printStackTrace();
        }
    }
    
    
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	
		
	}
    
}
