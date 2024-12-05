package com.giancarlos.controller;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.dto.user.UserLoginDto;
import com.giancarlos.dto.user.UserRegisterDto;
import com.giancarlos.dto.user.UserResponseDto;
import com.giancarlos.mapper.user.UserRegisterMapper;
import com.giancarlos.mapper.user.UserResponseMapper;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.service.UserService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRegisterMapper userRegisterMapper;
    private final UserResponseMapper userResponseMapper;
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRegisterMapper userRegisterMapper, UserResponseMapper userResponseMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRegisterMapper = userRegisterMapper;
        this.userService = userService;
        this.userResponseMapper= userResponseMapper;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        String encodedPassword=  bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        UserDto saved = userService.register(userRegisterMapper.toDto(userRegisterDto), encodedPassword);
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
