package com.order.management.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;
	
	@NotNull(message ="Product name cannot be null")
    private String productName;
	
	@Column(nullable = false)
	@Positive(message = "Quantity must be greater than zero")
	private int quantity;
	
	@Column(nullable = false)
	@Positive(message = "Price must be greater than zero")
	private double price;
	
	

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	@JsonBackReference
	private Order order;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderItem(Long itemId, String productName, int quantity, double price, Order order) {
		super();
		this.itemId = itemId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderItem [itemId=" + itemId + ", productName=" + productName + ", quantity=" + quantity + ", price="
				+ price + ", order=" + order + "]";
	}

	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

}
