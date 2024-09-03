package application;
import java.sql.*;

public class database {

	public static void main(String[] args) {
			
		connect();
	            
	}

	public static Connection connect() {
	    String connectionString = "jdbc:sqlserver://localhost\\SQLEXPRESS;DatabaseName=RetailXpress;integratedSecurity=true;encrypt=false;";

	    try {
	        System.out.print("Making Connection...");
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

	        Connection conn = DriverManager.getConnection(connectionString);
	        System.out.print("\nConnection Success!");
	        return conn;
	    } catch (ClassNotFoundException | SQLException e) {
	        System.out.print("\nConnection Error!");
	        e.printStackTrace();
	        return null;
	    }
	}

}
