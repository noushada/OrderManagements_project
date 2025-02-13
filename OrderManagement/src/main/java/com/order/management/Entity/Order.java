package com.order.management.Entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
	@SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 1)
	@Column(name = "order_id")
	private Long OrderId;

	@Column(nullable = false)
	private String customerName;

	@Temporal(TemporalType.DATE)
	private Date orderDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	@JsonManagedReference
	private List<OrderItem> items;

	@Column(nullable = false)
	private double totalAmount;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public Long getId() {
		return OrderId;
	}

	public void setId(Long OrderId) {
		this.OrderId = OrderId;
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

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [OrderId=" + OrderId + ", customerName=" + customerName + ", orderDate=" + orderDate + ", totalAmount="
				+ totalAmount + "]";
	}

	public Order(Long id, String customerName, Date orderDate, List<OrderItem> items, double totalAmount,
			OrderStatus status) {
		super();
		this.OrderId = OrderId;
		this.customerName = customerName;
		this.orderDate = orderDate;
		this.items = items;
		this.totalAmount = totalAmount;
		this.status = status;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

}
