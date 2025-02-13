package com.order.management.Iservice;

import java.util.Date;
import java.util.List;

import com.order.management.OrderDTO.OrderDTO;

public interface OrderManagementIService {

	/**
	 * Creates a new order.
	 * 
	 * This method is responsible for creating a new order in the system. It accepts
	 * an OrderDTO object, which contains all the necessary details to create the
	 * order, and returns the created order as an OrderDTO.
	 * 
	 * @param orderDTO The data transfer object containing the details of the order
	 *                 to be created.
	 * @return The created OrderDTO with the generated order information.
	 */
	public OrderDTO createOrder(OrderDTO orderDTO);

	/**
	 * Retrieves an order by its ID.
	 * 
	 * This method fetches the details of an existing order using its unique
	 * identifier (orderId). It returns the order's details as an OrderDTO.
	 * 
	 * @param orderId The ID of the order to retrieve.
	 * @return The OrderDTO containing the details of the order.
	 */
	public OrderDTO getOrderById(Long orderId);

	/**
	 * Updates the details of an existing order.
	 * 
	 * This method updates an existing order's information. It accepts an order ID
	 * to identify the order and an OrderDTO object containing the new information.
	 * It returns the updated order as an OrderDTO.
	 * 
	 * @param orderId  The ID of the order to update.
	 * @param orderDTO The data transfer object containing the new information for
	 *                 the order.
	 * @return The updated OrderDTO with the modified order details.
	 */
	public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);

	/**
	 * Deletes an order by its ID.
	 * 
	 * This method is responsible for deleting an order from the system using its
	 * unique identifier (id). No return value is expected.
	 * 
	 * @param id The ID of the order to delete.
	 */
	public void deleteOrder(Long id);

	/**
	 * Retrieves a list of orders based on the provided criteria.
	 * 
	 * This method allows the user to search for orders by specifying their status
	 * and a date range. It returns a list of orders (OrderDTOs) that match the
	 * specified criteria.
	 * 
	 * @param status    The status of the orders to filter by (e.g., "Pending",
	 *                  "Completed").
	 * @param startDate The starting date for filtering orders.
	 * @param endDate   The ending date for filtering orders.
	 * @return A list of OrderDTOs that match the specified status and date range.
	 */
	public List<OrderDTO> listOrders(String status, Date startDate, Date endDate);

}
