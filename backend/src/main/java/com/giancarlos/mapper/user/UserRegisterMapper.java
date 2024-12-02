package com.giancarlos.mapper.user;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.dto.user.UserRegisterDto;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.model.User;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterMapper {
    public UserDto toDto(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            throw new UserNotFoundException("UserRegisterDto is null in UserRegisterMapper in toDto");
        }
        return UserDto.builder()
                .email(userRegisterDto.getEmail())
                .phone(userRegisterDto.getPhone())
                .role(userRegisterDto.getRole())
                .name(userRegisterDto.getName())
                .address(userRegisterDto.getAddress())
                .cartItems(null)
                .orders(null)
                .build();
    }
}
