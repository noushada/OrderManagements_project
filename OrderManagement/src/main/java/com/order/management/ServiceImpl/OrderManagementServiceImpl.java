package com.order.management.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.management.Entity.Order;
import com.order.management.Entity.OrderItem;
import com.order.management.Iservice.OrderManagementIService;
import com.order.management.OrderDTO.OrderDTO;
import com.order.management.Repository.OrderManagementRepository;
import com.order.management.exception.DatabaseException;
import com.order.management.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class OrderManagementServiceImpl implements OrderManagementIService {

	private static final Logger logger = LoggerFactory.getLogger(OrderManagementServiceImpl.class);

	@Autowired
	private OrderManagementRepository orderRepository;

	@Override
	@Transactional
	public OrderDTO createOrder(OrderDTO orderDTO) {

		try {
			logger.info("Creating order for customer: {}", orderDTO.getCustomerName());
			Order order = convertToEntity(orderDTO);
			// Ensure each OrderItem references back to this Order
			if (order.getItems() != null) {
				for (OrderItem item : order.getItems()) {
					item.setOrder(order); // Set the relationship manually
				}
			}
			order = orderRepository.save(order);
			logger.info("Order created successfully with ID: {}", order.getId());
			return convertToDTO(order);

		} catch (Exception e) {

			logger.error("Error saving order", e);
			throw new DatabaseException("Error saving order to database", e);

		}
	}

	@Override
	public OrderDTO getOrderById(Long orderId) {
		logger.info("Fetching order with ID: {}", orderId);
		Optional<Order> order = orderRepository.findById(orderId);
		return order.map(this::convertToDTO) // map the Optional value to DTO if present
				.orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));

	}

	@Override
	public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
		logger.info("Updating order with ID: {}", orderId);
		Order existingOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

		try {
			// Update fields from DTO to the entity (only fields present in the DTO)
			existingOrder.setCustomerName(orderDTO.getCustomerName());
			existingOrder.setStatus(orderDTO.getStatus());
			existingOrder.setOrderDate(orderDTO.getOrderDate());
			// updating item details
			if (orderDTO.getItems() != null) {
				List<OrderItem> updatedItems = orderDTO.getItems().stream().map(itemDTO -> {
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(itemDTO.getItemId()); // If existing, keep the same ID
					orderItem.setProductName(itemDTO.getProductName());
					orderItem.setQuantity(itemDTO.getQuantity());
					orderItem.setPrice(itemDTO.getPrice());
					orderItem.setOrder(existingOrder); // Ensure Order reference is set
					return orderItem;
				}).collect(Collectors.toList());

				existingOrder.getItems().clear(); // Remove old items
				existingOrder.getItems().addAll(updatedItems); // Add new items
			}
			existingOrder.setTotalAmount(orderDTO.getTotalAmount());
			Order updatedOrder = orderRepository.save(existingOrder);
			// Convert the updated entity to DTO and return
			return convertToDTO(updatedOrder);
		} catch (Exception e) {
			logger.error("Error updating order with ID: {}", orderId, e);
			throw new DatabaseException("Error updating order", e);
		}

	}

	@Override
	public void deleteOrder(Long id) {
		logger.info("Deleting order with ID: {}", id);
		Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

		try {
			orderRepository.delete(order);
			logger.info("Order deleted successfully with ID: {}", id);
		} catch (Exception e) {
			logger.error("Error deleting order with ID: {}", id, e);
			throw new DatabaseException("Error deleting order", e);
		}

	}

	@Override
	public List<OrderDTO> listOrders(String status, Date startDate, Date endDate) {
		List<Order> order = null;
		logger.info("Fetching orders with filters - Status: {}, Start Date: {}, End Date: {}", status, startDate,
				endDate);
		try {
			// Check if any filter parameter is provided (status, startDate, or endDate)
			if (status != null || startDate != null || endDate != null) {
				order = orderRepository.findByFilters(status, startDate, endDate);
			} else {
				// If no filters are provided, fetch all orders
				order = orderRepository.findAll();
			}
			return order.stream().map(this::convertToDTO).collect(Collectors.toList()); // Convert each Order entity to an OrderDTO
		} catch (Exception ex) {

			throw new ServiceException("An error occurred while fetching orders.", ex);

		}

	}

	// ----------------------- Conversion to DTO -------------------------------//

	/**
	 * Converts an Order entity to an OrderDTO.
	 * 
	 * This method maps the fields from the Order entity to an OrderDTO object.
	 * It transfers essential data such as order ID, customer name, total amount, 
	 * status, order date, and associated items from the entity to the DTO.
	 * 
	 * @param order The Order entity to convert.
	 * @return The corresponding OrderDTO with the same data as the Order entity.
	 */
    private OrderDTO convertToDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getId());

		orderDTO.setCustomerName(order.getCustomerName());
		orderDTO.setTotalAmount(order.getTotalAmount());
		orderDTO.setStatus(order.getStatus());
		orderDTO.setOrderDate(order.getOrderDate());
		orderDTO.setItems(order.getItems());

		return orderDTO;
	}

    /**
     * Converts an OrderDTO to an Order entity.
     * 
     * This method maps the fields from the OrderDTO to an Order entity.
     * It handles conversion of order details, including customer name, order date,
     * status, total amount, and the list of order items.
     * The method also links each item to the parent Order entity.
     * 
     * @param orderDTO The OrderDTO to convert.
     * @return The corresponding Order entity with data from the OrderDTO.
     */
	public Order convertToEntity(OrderDTO orderDTO) {
		Order order = new Order();
		order.setCustomerName(orderDTO.getCustomerName());
		order.setOrderDate(orderDTO.getOrderDate());
		order.setStatus(orderDTO.getStatus());
		// Convert the items from OrderDTO to OrderItem entities, if items are provided
		if (orderDTO.getItems() != null) {
			List<OrderItem> items = orderDTO.getItems().stream().map(itemDTO -> {
				OrderItem item = new OrderItem();
				item.setProductName(itemDTO.getProductName());
				item.setQuantity(itemDTO.getQuantity());
				item.setPrice(itemDTO.getPrice());
				item.setOrder(order); // Link to parent Order
				return item;
			}).collect(Collectors.toList());

			order.setItems(items);
		}

		order.setTotalAmount(orderDTO.getTotalAmount());
		return order;
	}

	// ------------------------- Conversion Completed -------------------------------//

}
