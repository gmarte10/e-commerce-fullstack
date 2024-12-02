package com.giancarlos.service;

import com.giancarlos.dto.OrderItemDto;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.model.*;
import com.giancarlos.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private ProductService productService;
    private ProductMapper productMapper;
    private OrderService orderService;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            ProductService productService,
                            ProductMapper productMapper,
                            OrderService orderService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.productMapper = productMapper;
        this.orderService = orderService;
    }
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem o : orderItems) {
            Long oId = o.getOrder().getId();
            Long pId = o.getProduct().getId();
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setOrderId(oId);
            orderItemDto.setProductId(pId);
            orderItemDto.setQuantity(o.getQuantity());
            orderItemDto.setPrice(o.getPrice());
            orderItemDtos.add(orderItemDto);
        }
        return orderItemDtos;
    }

    public List<OrderItemDto> getOrderItemsByProductId(Long productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem o : orderItems) {
            Long orderId = o.getOrder().getId();
            Long pId = o.getProduct().getId();
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setOrderId(orderId);
            orderItemDto.setProductId(pId);
            orderItemDto.setQuantity(o.getQuantity());
            orderItemDto.setPrice(o.getPrice());
            orderItemDtos.add(orderItemDto);
        }
        return orderItemDtos;
    }

    public OrderItemDto addOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        Order order = orderService.findOrderById2(orderItemDto.getOrderId());
        Product product = productMapper.toEntity(productService.findById(orderItemDto.getProductId()));
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(product.getPrice());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        OrderItemDto oiDto = new OrderItemDto();
        oiDto.setProductId(savedOrderItem.getProduct().getId());
        oiDto.setOrderId(savedOrderItem.getOrder().getId());
        oiDto.setQuantity(savedOrderItem.getQuantity());
        oiDto.setPrice(savedOrderItem.getPrice());
        return oiDto;
    }
}
