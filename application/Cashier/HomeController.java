package application.Cashier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	
	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private ResultSet result2;
	private Statement statement;


   
    @FXML
    private Button logout;

    @FXML
    private Button makeReturn;

    @FXML
    private Button makeSale;

    
    public void DivertToMakeSale()
    {
    	makeSale.getScene().getWindow().hide();
		
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Sale.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void DivertToMakeReturn()
    {
    	makeReturn.getScene().getWindow().hide();
		
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Return.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void DivertToLogin()
    {
    	logout.getScene().getWindow().hide();
		
        Parent root;
		try {
			
			URL url = getClass().getResource("/application/login.fxml");
			root = FXMLLoader.load(url);
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
	public HomeController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		// TODO Auto-generated method stub
		
	}

}
