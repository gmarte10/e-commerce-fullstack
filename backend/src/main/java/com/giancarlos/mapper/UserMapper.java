package com.giancarlos.mapper;

import com.giancarlos.dto.UserDto;
import com.giancarlos.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDTO(User user);
    User toEntity(UserDto userDto);
}
