package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.UserMapperImpl;
import com.giancarlos.mapper.order.OrderMapper;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.User;
import com.giancarlos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private CartItemService cartItemService;
    private final UserService userService;
    private OrderMapper orderMapper;
    private UserMapper userMapper;

    public OrderService(OrderRepository orderRepository, CartItemService cartItemService, UserService userService, OrderMapper orderMapper, UserMapper userMapper) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    public List<OrderDto> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderDto orderDto = orderMapper.orderToOrderDto(order);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public OrderDto getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found");
        }
        return orderMapper.orderToOrderDto(order.get());
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        User user = userMapper.userDtoToUser(userService.getUserById(orderDto.getUserId()));


        Order order = new Order();
        User user = userService.getUserModelById(orderDto.getUserId());
        order.setCreatedAt(orderDto.getCreatedAt());
        order.setOrderItems(orderDto.getOrderItems());
        order.setUser(user);
        order.setTotalAmount(orderDto.getTotalAmount());
        Order saved = orderRepository.save(order);
        OrderDto retOrder = new OrderDto();
        retOrder.setOrderItems(saved.getOrderItems());
        retOrder.setCreatedAt(saved.getCreatedAt());
        retOrder.setTotalAmount(saved.getTotalAmount());
        retOrder.setUserId(saved.getUser().getId());
        retOrder.setId(saved.getId());
        return retOrder;
    }

}
