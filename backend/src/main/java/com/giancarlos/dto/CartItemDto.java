package com.giancarlos.dto;

import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private Long userId;

    private Long productId;

    private Integer quantity;
}
