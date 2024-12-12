package com.giancarlos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemRequestDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.mapper.product.ProductRequestMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderResponseMapper orderResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void OrderController_GetOrderByUserEmail_ReturnMoreThanOneOrderResponse() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).email("test@email.com").build();
        OrderDto orderDto = OrderDto.builder().id(2L).build();
        List<OrderDto> orderDtoList = List.of(orderDto);
        OrderResponseDto orderResponseDto = OrderResponseDto.builder().id(2L).build();
        when(userService.getUserByEmail("test@email.com")).thenReturn(userDto);
        when(orderService.getOrdersByUserId(1L)).thenReturn(orderDtoList);
        when(orderResponseMapper.toResponse(orderDto)).thenReturn(orderResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/orders/get/{email}", "test@email.com")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
        verify(userService, times(1)).getUserByEmail("test@email.com");
        verify(orderService, times(1)).getOrdersByUserId(1L);
        verify(orderResponseMapper, times(1)).toResponse(orderDto);
    }

    @Test
    public void OrderController_CreateOrder_ReturnOrderResponse() throws Exception {
        String email = "test@email.com";
        ZonedDateTime createdAt = ZonedDateTime.now();

        OrderItemRequestDto orderItemRequestDto = OrderItemRequestDto.builder()
                .productId(1L)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .email(email)
                .createdAt(createdAt)
                .totalAmount(BigDecimal.valueOf(100.00))
                .orderItemRequestDtos(List.of(orderItemRequestDto))
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email(email)
                .build();
        User user = User.builder()
                .id(1L)
                .email(email)
                .build();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .createdAt(createdAt)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();

        OrderDto savedOrderDto = OrderDto.builder()
                .id(1L)
                .userId(1L)
                .createdAt(createdAt)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .price(BigDecimal.valueOf(100.00))
                .build();

        OrderItemDto orderItemDto = OrderItemDto.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .build();

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .id(1L)
                .totalAmount(BigDecimal.valueOf(100.00))
                .createdAt(createdAt)
                .build();

        when(userService.getUserByEmail(email)).thenReturn(userDto);
        when(userService.getUserById(1L)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);
        when(orderService.createOrder(any(Order.class))).thenReturn(savedOrderDto);
        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderItemService.createOrderItem(any(OrderItem.class))).thenReturn(orderItemDto);
        when(orderItemService.getOrderItemById(1L)).thenReturn(orderItem);
        when(orderResponseMapper.toResponse(savedOrderDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.00));

        verify(userService).getUserByEmail(email);
        verify(orderService, times(2)).createOrder(any(Order.class));
        verify(orderItemService).createOrderItem(any(OrderItem.class));
    }

}
