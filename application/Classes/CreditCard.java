package application.Classes;

import java.time.LocalDate;

public class CreditCard extends Payment {
	
	private String cardNumber;
	private LocalDate Date;
	private String cvv;

	public CreditCard() {
		// TODO Auto-generated constructor stub
	}

	public CreditCard(String customerName, String customerCNIC, int saleId, double amount, String cardNumber , LocalDate Date , String cvv) {
		super(customerName, customerCNIC, saleId, amount);
		// TODO Auto-generated constructor stub
		
		this.cardNumber = cardNumber;
		this.Date = Date;
		this.cvv = cvv;
		
	}
	
	public double PorcessPayment()
	{
		return 1;	
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public LocalDate getDate() {
		return Date;
	}

	public void setDate(LocalDate date) {
		Date = date;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	
	

}
