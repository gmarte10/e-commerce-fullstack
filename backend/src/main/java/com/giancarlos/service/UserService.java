package com.giancarlos.service;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public UserDto register(UserDto userDto, String encodedPassword) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User was not found by email");
        }
        return userMapper.toDto(user.get());
    }

    public String verify(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "Failed to authenticate. User email does not exist";
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, rawPassword));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(email);
        } else {
            return "Failed to authenticate. User password is incorrect";
        }
    }

    public UserRole getUserRole(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User was not found in UserService in getUserRole");
        }
        return user.get().getRole();
    }

    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User u : userList) {
            userDtoList.add(userMapper.toDto(u));
        }
        return userDtoList;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User could not be found in UserService in GetUserById"));
        return userMapper.toDto(user);
    }
}