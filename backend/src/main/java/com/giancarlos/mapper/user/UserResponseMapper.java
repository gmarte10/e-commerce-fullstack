package com.giancarlos.mapper.user;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.dto.user.UserRegisterDto;
import com.giancarlos.dto.user.UserResponseDto;
import com.giancarlos.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponseMapper() {

    }
    public UserResponseDto toResponse(UserDto userDto) {
        if (userDto == null) {
            throw new UserNotFoundException("UserDto is null in UserResponseMapper in toResponse");
        }
        return UserResponseDto.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .role(userDto.getRole().toString())
                .name(userDto.getName())
                .build();
    }
}
