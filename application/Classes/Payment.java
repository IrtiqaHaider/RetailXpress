package application.Classes;

public abstract class Payment {
	
	protected String CustomerName;
	protected String CustomerCNIC;
	protected int SaleId;
	protected double amount;
	
	public Payment()
	{
		
	}
	
	public Payment(String customerName, String customerCNIC, int saleId, double amount) {
		super();
		CustomerName = customerName;
		CustomerCNIC = customerCNIC;
		SaleId = saleId;
		this.amount = amount;
	}



	public double PorcessPayment() {
		return amount;
		// TODO Auto-generated constructor stub
	}

}

