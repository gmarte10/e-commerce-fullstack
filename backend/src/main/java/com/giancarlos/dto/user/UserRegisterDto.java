package com.giancarlos.dto.user;

import com.giancarlos.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private String name;
    private String email;
    private UserRole role;
    private String address;
    private String phone;
    private String password;
}
