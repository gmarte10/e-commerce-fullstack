package com.giancarlos.controller;

import com.giancarlos.dto.OrderDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @MockBean
    private CartItemService cartItemService;

    @Test
    void testGetAllOrdersByUserEmail() throws Exception {
        // Test data
        UserDto user = new UserDto();
        user.setId(1L);
        user.setAddress("123 Main St");
        user.setEmail("test@example.com");

        OrderDto order = new OrderDto();
        order.setId(1L);
        order.setCreatedAt(ZonedDateTime.now());
        order.setTotalAmount(BigDecimal.valueOf(100.0));

        List<OrderDto> orders = Collections.singletonList(order);

        // Mocking services
        Mockito.when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        Mockito.when(orderService.findByUserId(1L)).thenReturn(orders);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalAmount").value(100.0))
                .andExpect(jsonPath("$[0].address").value("123 Main St"));

        // Verify interactions
        Mockito.verify(userService).getUserByEmail("test@example.com");
        Mockito.verify(orderService).findByUserId(1L);
    }


}
