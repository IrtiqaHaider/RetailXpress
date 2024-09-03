package application.Classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
	
	private int saleId;
	private LocalDateTime  saleTime;
	private double total;
	private List<SaleLineItem> saleLine;
	
	public Sale() {
		saleLine = new ArrayList<SaleLineItem>();

		// TODO Auto-generated constructor stub
	}

	public Sale(int saleId, LocalDateTime saleTime, double total) {
		super();
		this.saleId = saleId;
		this.saleTime = saleTime;
		this.total = total;
		saleLine = new ArrayList<SaleLineItem>();

	}
	
	public void addSale(SaleLineItem item)
	{
		saleLine.add(item);
	}

	public void clearList()
	{
		saleLine.clear();
	}
	
	public int getSaleId() {
		return saleId;
	}

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	public LocalDateTime getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(LocalDateTime saleTime) {
		this.saleTime = saleTime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public double calculateTotal()
	{
		double sum = 0;
		
		for(SaleLineItem items : saleLine)
		{
			sum += items.getAmount();
		}
		
		return sum;
	}
	
	public void printList()
	{
		for(SaleLineItem items : saleLine)
		{
			
			System.out.println("\n--------------------------");
			
            System.out.println("SaleLineItem ID: " +  items.getSaleLineItemId());
            System.out.println("Sale ID: " + items.getSaleId());
            System.out.println("Product ID: " + items.getProductId());
            System.out.println("Quantity: " + items.getQuantity());
            System.out.println("Amount: " + items.getAmount());
            
            System.out.println("\n--------------------------");

		}
	}
	
	

}
