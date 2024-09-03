package application.Classes;

public class Cash extends Payment {

	private double cashTendered;
	
	public Cash() {
		
	}

	public Cash(String customerName, String customerCNIC, int saleId, double amount , double cashTendered) 
	{
		super(customerName, customerCNIC, saleId, amount);
		// TODO Auto-generated constructor stub
		
		this.cashTendered = cashTendered;
	}
	
	public double PorcessPayment()
	{
		double balance = amount - cashTendered;
		
		return balance;
	}

	public double getCashTendered() {
		return cashTendered;
	}

	public void setCashTendered(double cashTendered) {
		this.cashTendered = cashTendered;
	}
	
	
	
	
}
