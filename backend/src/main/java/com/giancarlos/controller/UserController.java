package com.giancarlos.controller;

import com.giancarlos.dto.LoginRequestDto;
import com.giancarlos.dto.RegisterRequestDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.dto.UserResponseDto;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        String encodedPassword=  bCryptPasswordEncoder.encode(registerRequest.getPassword());
        UserDto registeredUser = userService.register(registerRequest.getUserDto(), encodedPassword);
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .address(registeredUser.getAddress())
                .phone(registeredUser.getPhone())
                .build();
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        return new ResponseEntity<>(userService.verify(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @GetMapping("/role/{email}")
    public ResponseEntity<String> getRole(@PathVariable String email) {
        UserRole userRole = userService.getUserRole(email);
        String role = userRole.toString();
        return new ResponseEntity<>(role,HttpStatus.OK);
    }

    @GetMapping("/id/{email}")
    public ResponseEntity<Long> getUserId(@PathVariable String email) {
        UserDto user  = userService.getUserByEmail(email);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @GetMapping("/address/{email}")
    public ResponseEntity<String> getUserAddress(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user.getAddress(), HttpStatus.OK);
    }
}
