package application.Classes;

public class Product {

	private int productId;
	private String productName;
	private String category;
	private double price;
	private int quantity;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Product(int productId, String productName, String category, double price, int quantity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
	}



	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void printProductSpec()
	{
		System.out.print("\n Product Id: " + productId);
		System.out.print("\n Product Name: " + productName);
		System.out.print("\n Category: " + category);
		System.out.print("\n Price: " + price);
		System.out.print("\n Quantity: " + quantity);

	}
	
}
