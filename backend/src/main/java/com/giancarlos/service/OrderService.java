package com.giancarlos.service;

import com.giancarlos.dto.OrderDto;
import com.giancarlos.dto.OrderItemDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.User;
import com.giancarlos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CartItemService cartItemService;
    private UserService userService;

    public OrderService(OrderRepository orderRepository, CartItemService cartItemService, UserService userService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    public List<OrderDto> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order o : orders) {
            Long orderId = o.getId();
            Long uId = o.getUser().getId();
            OrderDto orderDto = new OrderDto();
            orderDto.setId(orderId);
            orderDto.setUserId(uId);
            orderDto.setCreatedAt(o.getCreatedAt());
            orderDto.setTotalAmount(o.getTotalAmount());
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public OrderDto findOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found");
        }
        OrderDto orderDto = new OrderDto();
        Order o = order.get();
        orderDto.setTotalAmount(o.getTotalAmount());
        orderDto.setId(o.getId());
        orderDto.setUserId(o.getUser().getId());
        orderDto.setCreatedAt(o.getCreatedAt());
        return orderDto;
    }

    public Order findOrderById2(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found");
        }
        Order ret = order.get();
        System.out.println("Inside order service: " + ret.getId());
        return order.get();
    }

    public OrderDto createOrder(OrderDto orderDto) {
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
