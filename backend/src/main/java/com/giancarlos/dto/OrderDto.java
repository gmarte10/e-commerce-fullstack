package com.giancarlos.dto;

import com.giancarlos.model.OrderItem;
import com.giancarlos.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;

    private Long userId;

    private BigDecimal totalAmount;

    private ZonedDateTime createdAt;

    private List<OrderItem> orderItems;
}
