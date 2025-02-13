package com.order.management.TestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.management.Controller.OrderManagementController;
import com.order.management.Entity.OrderStatus;
import com.order.management.Iservice.OrderManagementIService;
import com.order.management.OrderDTO.OrderDTO;

@WebMvcTest(OrderManagementController.class) // Load only the controller layer
@ExtendWith(MockitoExtension.class)
class OrderManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderManagementIService orderManagementIService; // Mock the service layer

    @Autowired
    private ObjectMapper objectMapper; // Convert objects to JSON

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
        orderDTO.setCustomerName("John Doe");
        orderDTO.setTotalAmount(100.50);
        orderDTO.setStatus(OrderStatus.valueOf("NEW"));
        orderDTO.setOrderDate(new Date());
    }

    //  Create Order
    @Test
    void testCreateOrder() throws Exception {
        when(orderManagementIService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(orderManagementIService, times(1)).createOrder(any(OrderDTO.class));
    }

    // Get Order by ID
    @Test
    void testGetOrderById() throws Exception {
        when(orderManagementIService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(orderManagementIService, times(1)).getOrderById(1L);
    }

    // List Orders
    @Test
    void testListOrders() throws Exception {
        List<OrderDTO> orders = Arrays.asList(orderDTO);
        when(orderManagementIService.listOrders(anyString(), any(), any())).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                .param("status", "NEW")
                .param("startDate", "2025-01-01")
                .param("endDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(orderManagementIService, times(1)).listOrders(anyString(), any(), any());
    }

    // Update Order
    @Test
    void testUpdateOrder() throws Exception {
        when(orderManagementIService.updateOrder(eq(1L), any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(put("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(orderManagementIService, times(1)).updateOrder(eq(1L), any(OrderDTO.class));
    }

    // Delete Order
    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderManagementIService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderManagementIService, times(1)).deleteOrder(1L);
    }
}
