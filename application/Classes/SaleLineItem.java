package application.Classes;

public class SaleLineItem {
	
	private int saleLineItemId;
	private int productId;
	private int saleId;
	private int quantity;
	private double amount;
	

	public SaleLineItem() {
		// TODO Auto-generated constructor stub
	}
	

	public SaleLineItem(int saleLineItemId, int productId, int saleId, int quantity, double amount) {
		super();
		this.saleLineItemId = saleLineItemId;
		this.productId = productId;
		this.saleId = saleId;
		this.quantity = quantity;
		this.amount = amount;
	}


	public int getSaleLineItemId() {
		return saleLineItemId;
	}


	public void setSaleLineItemId(int saleLineItemId) {
		this.saleLineItemId = saleLineItemId;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public int getSaleId() {
		return saleId;
	}


	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}


	
	

}
