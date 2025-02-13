package com.order.management.OrderDTO;

import java.util.Date;
import java.util.List;

import com.order.management.Entity.OrderItem;
import com.order.management.Entity.OrderStatus;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderDTO {

	@Id
	private Long orderId;

	// Customer's name associated with the order, it cannot be blank
	@NotBlank(message = "Customer name is required")
	private String customerName;

	// Order date, cannot be null
	@NotNull(message = "Order date is required")
	private Date orderDate;

	// List of order items; each item must be validated (e.g., valid quantity,
	// price)
	@Valid
	private List<OrderItem> items;

	// Total amount of the order; must be positive
	@Positive(message = "Total amount must be positive")
	private double totalAmount;

	// Status of the order; cannot be null (e.g., Pending, Completed)
	@NotNull(message = "Order status is required")
	private OrderStatus status;

	// Parameterized constructor for creating an OrderDTO with all required fields
	public OrderDTO(long l, String string, int i, double d, String string2, Date date, List<OrderItem> list) {
		super();
		// TODO Auto-generated constructor stub
	}

	// Default constructor
	public OrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Getter and setter methods for all fields
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> list) {
		this.items = list;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
