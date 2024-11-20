package com.giancarlos.dto;

import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private UserRole role;
}
