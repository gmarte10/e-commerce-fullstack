package com.giancarlos.service;

import com.giancarlos.model.User;
import com.giancarlos.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    public void UserDetailService_LoadByUsername_ReturnUserDetails() {
        String email = "johndoe@gmail.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        Assertions.assertThat(userDetails).isNotNull();
    }
    @Test
    public void UserDetailService_LoadByUsername_ThrowUsernameNotFoundException() {
        String email = "johndoe@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(email));

    }
}
