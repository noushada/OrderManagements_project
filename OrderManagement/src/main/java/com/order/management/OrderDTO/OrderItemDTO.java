package com.order.management.OrderDTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemDTO {

	@Id
	private Long itemId;

	// Product Name of the item not null
	@NotNull(message = "Product name is required")
	private String productName;

	// Quntity of the item must be positive
	@Positive(message = "Quantity must be greater than zero")
	private int quantity;

	// Price amount of the item must be positive
	@Positive(message = "Price must be positive")
	private double price;

	// Getter and setter methods for all fields

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

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

}
