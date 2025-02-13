package com.order.management.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.order.management.Entity.Order;
import com.order.management.Entity.OrderItem;
import com.order.management.Entity.OrderStatus;
import com.order.management.OrderDTO.OrderDTO;
import com.order.management.Repository.OrderManagementRepository;
import com.order.management.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
public class OrderManagementServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(OrderManagementServiceTest.class);

	@Mock
	private OrderManagementRepository orderRepository; // Mock Repository

	@InjectMocks
	private OrderManagementServiceImpl orderService; // Inject Mocks into Service

	private Order order;
	private OrderDTO orderDTO;

	@BeforeEach
	void setUp() {
		logger.info("Setting up test data...");

		order = new Order();
		order.setId(1L);
		order.setCustomerName("John Doe");
		order.setOrderDate(new Date());
		order.setStatus(OrderStatus.valueOf("NEW"));
		order.setTotalAmount(150.75);
		order.setItems(Arrays.asList(new OrderItem(4L, "Laptop", 1, 150.75, order)));

		orderDTO = new OrderDTO(1L, "John Doe", 2, 150.75, "NEW", new Date(), order.getItems());
	}

	// Test Case - Create Order Successfully
	@Test
	void testCreateOrder_Success() {
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		OrderDTO createdOrder = orderService.createOrder(orderDTO);

		assertNotNull(createdOrder);
		assertEquals(orderDTO.getCustomerName(), createdOrder.getCustomerName());
		verify(orderRepository, times(1)).save(any(Order.class)); // Ensure repository was used once
		logger.info("testCreateOrder_Success passed!");
	}

	// Test Case - Get Order By ID (Exists)
	@Test
	void testGetOrderById_Success() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		OrderDTO fetchedOrder = orderService.getOrderById(2L);

		assertNotNull(fetchedOrder);
		assertEquals("John Doe", fetchedOrder.getCustomerName());
		verify(orderRepository, times(1)).findById(1L);
		logger.info("testGetOrderById_Success passed!");
	}

	// Test Case - Get Order By ID (Not Found)
	@Test
	void testGetOrderById_NotFound() {
		when(orderRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(2L));
		verify(orderRepository, times(1)).findById(2L);
		logger.info("testGetOrderById_NotFound passed!");
	}

	// Test Case - Update Order Successfully
	@Test
	void testUpdateOrder_Success() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		OrderDTO updatedOrder = orderService.updateOrder(1L, orderDTO);

		assertNotNull(updatedOrder);
		assertEquals("John Doe", updatedOrder.getCustomerName());
		verify(orderRepository, times(1)).findById(1L);
		verify(orderRepository, times(1)).save(any(Order.class));
		logger.info("testUpdateOrder_Success passed!");
	}

	// Test Case - Update Order (Not Found)
	@Test
	void testUpdateOrder_NotFound() {
		when(orderRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(2L, orderDTO));
		verify(orderRepository, times(1)).findById(2L);
		logger.info("testUpdateOrder_NotFound passed!");
	}

	// Test Case - Delete Order Successfully
	@Test
	void testDeleteOrder_Success() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		assertDoesNotThrow(() -> orderService.deleteOrder(1L));
		verify(orderRepository, times(1)).delete(order);
		logger.info("testDeleteOrder_Success passed!");
	}

	// ❌ Test Case - Delete Order (Not Found)
	@Test
	void testDeleteOrder_NotFound() {
		when(orderRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(2L));
		verify(orderRepository, times(1)).findById(2L);
		logger.info("testDeleteOrder_NotFound passed!");
	}

	// Test Case - List Orders (All Orders)
	@Test
	void testListOrders_All() {
		when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

		List<OrderDTO> orders = orderService.listOrders(null, null, null);

		assertFalse(orders.isEmpty());
		assertEquals(1, orders.size());
		verify(orderRepository, times(1)).findAll();
		logger.info("testListOrders_All passed!");
	}

	// Test Case - List Orders with Filters
	@Test
	void testListOrders_WithFilters() {
		when(orderRepository.findByFilters("NEW", null, null)).thenReturn(Arrays.asList(order));

		List<OrderDTO> orders = orderService.listOrders("NEW", null, null);

		assertFalse(orders.isEmpty());
		assertEquals(1, orders.size());
		verify(orderRepository, times(1)).findByFilters("NEW", null, null);
		logger.info("testListOrders_WithFilters passed!");
	}
}
