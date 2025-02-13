package com.order.management.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order.management.Entity.Order;

public interface OrderManagementRepository extends JpaRepository<Order, Long> {

	/**
	 * Retrieves orders based on the provided filters for status and date range.
	 * 
	 * This method uses a custom query to filter orders by their status and a date
	 * range. If either the status or the date range is not provided (null), it will
	 * not be used in the filtering condition. Both `startDate` and `endDate` must
	 * be provided for the date filter to be applied.
	 * 
	 * @param status    The status of the orders to filter by (e.g., "Pending",
	 *                  "Completed").
	 * @param startDate The start date for filtering orders.
	 * @param endDate   The end date for filtering orders.
	 * @return A list of orders that match the provided filters.
	 */
	@Query(value = "SELECT * FROM `orders` o WHERE " + "(:status IS NULL OR o.status = :status) AND "
			+ "(:startDate IS NULL OR :endDate IS NULL OR o.order_date BETWEEN :startDate AND :endDate)", nativeQuery = true)
	List<Order> findByFilters(@Param("status") String status, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

}
