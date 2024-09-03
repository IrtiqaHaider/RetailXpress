package application.Manager;

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
    private TextField Availproducts;

    @FXML
    private TextField NoOfOrders;

    @FXML
    private AreaChart<?, ?> OrderChart;

    @FXML
    private TextField income;

    @FXML
    private AreaChart<?, ?> incomeChart;

    @FXML
    private Button logout;

    @FXML
    private Button manageInventory;

    @FXML
    private TextField noOfReturns;
    
    public void homeDisplayTotalOrder()
    {
    	int countOrders = 0;
    	int countReturns = 0;
    	
    	String query = "SELECT COUNT(saleId) AS TotalOrders FROM Sale "
                + "WHERE CAST(saleTime AS DATE) = CAST(GETDATE() AS DATE)";
    	
    	String query2 = "SELECT COUNT(ReturnId) AS TotalReturns FROM ReturnSale "
                + "WHERE CAST(returnDate AS DATE) = CAST(GETDATE() AS DATE)";

    	connect = database.connect();
    	
    									/// count Returns
    	try {

			prepare = connect.prepareStatement(query2);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				countReturns = result.getInt("TotalReturns");
			}
			
			noOfReturns.setText(String.valueOf(countReturns));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    								// count oRDERS
    	
		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				countOrders = result.getInt("TotalOrders");
			}
			
			NoOfOrders.setText(String.valueOf(countOrders-countReturns));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void homeDisplayTotalIncome()
    {
    	
    	double total = 0;
    	double total2 = 0;
    	
    	String query = "SELECT SUM(total) as TotalIncome FROM Sale "
    			+ "WHERE CAST(Sale.saleTime AS DATE) = CAST(GETDATE() AS DATE)";
    	
    	String query2 = "SELECT SUM(totalAmount) as TotalReturnAmount FROM ReturnSale "
    			+ "WHERE CAST(returnDate AS DATE) = CAST(GETDATE() AS DATE)";

    	connect = database.connect();

    								/// RETURNED AMOUNT
    	
    	try {

			prepare = connect.prepareStatement(query2);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				total2 = result.getDouble("TotalReturnAmount");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    								// INCOME AMOUNT
    	
		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				total = result.getDouble("TotalIncome");
			}
			
			income.setText(String.valueOf(total-total2));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    

    public void homeDiplayAvailableProducts()
    {
    	int noOfProducts = 0;
    	
    	String query = "SELECT COUNT(productId) as NoOfProducts from Product where quantity > 0";

    	connect = database.connect();

		try {

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				noOfProducts = result.getInt("NoOfProducts");
			}
			
			Availproducts.setText(String.valueOf(noOfProducts));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public void homeIncomeChart()
    {
    	incomeChart.getData().clear();
    	
    	String query = "SELECT CONVERT(DATE, saleTime) as SaleDate, SUM(total) as TotalIncome FROM Sale GROUP BY CONVERT(DATE, saleTime) ORDER BY SaleDate ";
    
    	connect = database.connect();

		try {
			
			XYChart.Series chart = new XYChart.Series();

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				chart.getData().add(new XYChart.Data(result.getString(1) , result.getInt(2)));
			}
			
			incomeChart.getData().add(chart);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    
    }
    
    
    public void homeOrderChart()
    {
    	OrderChart.getData().clear();
    	
    	String query = "SELECT CONVERT(DATE, saleTime) as SaleDate, COUNT(saleId) AS TotalOrders FROM Sale GROUP BY CONVERT(DATE, saleTime) ORDER BY SaleDate ";
    
    	connect = database.connect();

		try {
			
			XYChart.Series chart = new XYChart.Series();

			prepare = connect.prepareStatement(query);
			result = prepare.executeQuery();
			
			while(result.next())
			{
				chart.getData().add(new XYChart.Data(result.getString(1) , result.getInt(2)));
			}
			
			OrderChart.getData().add(chart);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public void DivertToManageInventory()
    {
    	manageInventory.getScene().getWindow().hide();
		
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("ManageInventory.fxml"));
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
		homeDisplayTotalOrder();
		homeDisplayTotalIncome();
		homeDiplayAvailableProducts();
		homeIncomeChart();
		homeOrderChart();
	}

}
