package com.giancarlos.controller;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.dto.user.UserLoginDto;
import com.giancarlos.dto.user.UserRegisterDto;
import com.giancarlos.dto.user.UserResponseDto;
import com.giancarlos.mapper.user.UserResponseMapper;
import com.giancarlos.model.User;
import com.giancarlos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * UserController handles the REST API endpoints related to user management.
 * It provides functionality for registering a new user, logging in, and fetching user information.
 *
 * This controller interacts with the following services:
 * 1. UserService: Manages the user-related business logic and data persistence.
 * 2. BCryptPasswordEncoder: Handles password encoding for user registration.
 * 3. UserResponseMapper: Maps UserDto objects to UserResponseDto objects for API responses.
 *
 * The available endpoints are:
 * - `POST /api/users/register`: Registers a new user with the provided details.
 * - `POST /api/users/login`: Authenticates a user by verifying the email and password.
 * - `GET /api/users/info/{email}`: Fetches user information by email.
 *
 * @RestController
 * @RequestMapping("/api/users")
 */

@RestController()
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserResponseMapper userResponseMapper;
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, UserResponseMapper userResponseMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.userResponseMapper= userResponseMapper;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        String encodedPassword=  bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        User user = User.builder()
                .name(userRegisterDto.getName())
                .role(userRegisterDto.getRole())
                .phone(userRegisterDto.getPhone())
                .address(userRegisterDto.getAddress())
                .email(userRegisterDto.getEmail())
                .cartItems(new ArrayList<>())
                .orders(new ArrayList<>())
                .password(encodedPassword)
                .build();
        UserDto saved = userService.register(user);
        UserResponseDto response = userResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        return new ResponseEntity<>(userService.verify(userLoginDto.getEmail(), userLoginDto.getPassword()), HttpStatus.OK);
    }

    @GetMapping("/info/{email}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        UserResponseDto response = userResponseMapper.toResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
