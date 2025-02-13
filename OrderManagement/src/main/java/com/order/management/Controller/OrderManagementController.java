package com.order.management.Controller;

import java.util.Date;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.management.Iservice.OrderManagementIService;
import com.order.management.OrderDTO.OrderDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderManagementController {

	private static final Logger logger = LoggerFactory.getLogger(OrderManagementController.class);
	@Autowired
	private OrderManagementIService orderManagementIService;

	/**
	 * Endpoint to create a new order.
	 * 
	 * This method receives an OrderDTO in the request body, logs the received data,
	 * and calls the service layer to create the order. Upon success, it returns a
	 * ResponseEntity with a CREATED status and the created OrderDTO.
	 * 
	 * @param orderDTO The order data transfer object to create a new order.
	 * @return A ResponseEntity containing the created OrderDTO.
	 */
	@PostMapping
	public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
		logger.info("Received request to create order: {}", orderDTO);
		OrderDTO createdOrder = orderManagementIService.createOrder(orderDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);

	}

	/**
	 * Endpoint to list orders based on optional filters (status, start date, and
	 * end date).
	 * 
	 * This method receives optional query parameters (status, startDate, endDate),
	 * logs the request, and calls the service layer to fetch the list of orders. It
	 * returns the list of orders wrapped in a ResponseEntity.
	 * 
	 * @param status    The status to filter orders by (optional).
	 * @param startDate The start date to filter orders by (optional).
	 * @param endDate   The end date to filter orders by (optional).
	 * @return A ResponseEntity containing a list of filtered orders.
	 */
	@GetMapping
	public ResponseEntity<List<OrderDTO>> listOrders(@RequestParam(required = false) String status,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		logger.info("Received request to get order with Status,date: {}", status, startDate, endDate);
		List<OrderDTO> orders = orderManagementIService.listOrders(status, startDate, endDate);
		return ResponseEntity.ok(orders);

	}

	/**
	 * Endpoint to fetch an order by its ID.
	 * 
	 * This method receives the order ID as a path variable, logs the request, and
	 * calls the service layer to fetch the order details. The order details are
	 * returned wrapped in a ResponseEntity.
	 * 
	 * @param orderId The ID of the order to fetch.
	 * @return A ResponseEntity containing the OrderDTO with the specified ID.
	 */
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> getOrder(@Param("id") @PathVariable Long orderId) {
		logger.info("Received request to fetch order with ID: {}", orderId);
		OrderDTO orderDTO = orderManagementIService.getOrderById(orderId);
		return ResponseEntity.ok(orderDTO);

	}

	/**
	 * Endpoint to update an order.
	 * 
	 * This method receives the order ID as a path variable and the updated OrderDTO
	 * in the request body. It logs the request, calls the service layer to update
	 * the order, and returns the updated OrderDTO in the response.
	 * 
	 * @param orderId  The ID of the order to update.
	 * @param orderDTO The updated order details.
	 * @return A ResponseEntity containing the updated OrderDTO.
	 */
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
		logger.info("Received request to update order with ID: {}", orderId);
		OrderDTO updatedOrder = orderManagementIService.updateOrder(orderId, orderDTO);
		return ResponseEntity.ok(updatedOrder);

	}

	/**
	 * Endpoint to delete an order by its ID.
	 * 
	 * This method receives the order ID as a path variable, logs the request, and
	 * calls the service layer to delete the order. It returns a ResponseEntity with
	 * no content (HTTP 204) to indicate successful deletion.
	 * 
	 * @param id The ID of the order to delete.
	 * @return A ResponseEntity with no content to indicate successful deletion.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		logger.info("Received request to delete order with ID: {}", id);
		orderManagementIService.deleteOrder(id);
		return ResponseEntity.noContent().build();

	}

}
