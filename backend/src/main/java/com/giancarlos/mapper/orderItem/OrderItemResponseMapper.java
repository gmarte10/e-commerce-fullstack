package com.giancarlos.mapper.orderItem;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemResponseDto;
import com.giancarlos.exception.OrderItemNotFoundException;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.model.Product;
import com.giancarlos.repository.OrderItemRepository;
import com.giancarlos.service.ImageService;
import com.giancarlos.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class OrderItemResponseMapper {
    private final ProductService productService;
    private final ImageService imageService;
    public OrderItemResponseMapper(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;

    }
    public OrderItemResponseDto toResponse(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            throw new OrderItemNotFoundException("OrderItemDto is null in OrderItemResponseMapper in toResponse");
        }
        Product product = productService.getProductById(orderItemDto.getProductId());
        return OrderItemResponseDto.builder()
                .id(orderItemDto.getId())
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .orderId(orderItemDto.getOrderId())
                .name(product.getName())
                .description(product.getDescription())
                .imgBase64(imageService.getImageBase64(product.getImageURL()))
                .category(product.getCategory())
                .build();
    }
}
