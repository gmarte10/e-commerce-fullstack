package com.giancarlos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.order.OrderRequestMapper;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.mapper.product.ProductRequestMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.service.JwtService;
import com.giancarlos.service.OrderService;
import com.giancarlos.service.ProductService;
import com.giancarlos.service.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
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
    private UserService userService;

    @MockBean
    private OrderRequestMapper orderRequestMapper;

    @MockBean
    private JwtService jwtService;

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
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .email(email)
                .build();

        OrderDto orderDto = OrderDto.builder().id(1L).build();

        OrderResponseDto orderResponseDto = OrderResponseDto.builder().id(1L).build();

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderDto);
        when(orderRequestMapper.toDto(orderRequestDto)).thenReturn(orderDto);
        when(orderResponseMapper.toResponse(orderDto)).thenReturn(orderResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/api/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

}
