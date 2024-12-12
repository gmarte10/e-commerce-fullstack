package com.giancarlos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemResponseDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.mapper.orderItem.OrderItemResponseMapper;
import com.giancarlos.service.JwtService;
import com.giancarlos.service.OrderItemService;
import com.giancarlos.service.OrderService;
import com.giancarlos.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderItemControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;


    @MockBean
    private JwtService jwtService;

    @MockBean
    private OrderItemResponseMapper orderItemResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void OrderItemController_GetOrderItemsByOrderId_ReturnMoreThanOneOrderItemResponse() throws Exception {
        Long orderId = 1L;
        OrderItemDto orderItemDto = OrderItemDto.builder().id(2L).build();
        List<OrderItemDto> orderItemDtoList = List.of(orderItemDto);
        OrderItemResponseDto orderItemResponseDto = OrderItemResponseDto.builder().id(2L).orderId(orderId).build();
        when(orderItemService.getOrderItemsByOrderId(orderId)).thenReturn(orderItemDtoList);
        when(orderItemResponseMapper.toResponse(orderItemDto)).thenReturn(orderItemResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/order-items/get/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
        verify(orderItemService, times(1)).getOrderItemsByOrderId(orderId);
        verify(orderItemResponseMapper, times(1)).toResponse(orderItemDto);
    }
}
