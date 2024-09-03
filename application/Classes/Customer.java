package application.Classes;

public class Customer {

	private int saleId;
	private String customerName;
	private String customerCNIC;
	private double totalAmount;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public Customer(int saleId, String customerName, String customerCNIC, double totalAmount) 
	{
		super();
		this.saleId = saleId;
		this.customerName = customerName;
		this.customerCNIC = customerCNIC;
		this.totalAmount = totalAmount;
	}

	public int getSaleId() {
		return saleId;
	}

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCNIC() {
		return customerCNIC;
	}

	public void setCustomerCNIC(String customerCNIC) {
		this.customerCNIC = customerCNIC;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	
	
}
