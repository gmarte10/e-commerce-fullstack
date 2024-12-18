package com.giancarlos.service;

import com.giancarlos.model.User;
import com.giancarlos.model.UserDetail;
import com.giancarlos.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserDetailService is a service implementation of the Spring Security's `UserDetailsService` interface.
 * It is responsible for loading user details based on the email, which is used as the unique identifier.
 * This service retrieves the user from the repository and maps it to a `UserDetails` object, which is required
 * for authentication and authorization in the Spring Security context.
 *
 * The service provides the following key functionality:
 * 1. Loading a user by email for authentication purposes.
 * 2. Throwing an exception if the user is not found based on the email.
 *
 * This service is annotated with @Service to be managed by the Spring container.
 *
 * @Service
 */
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email Not Found");
        }
        return new UserDetail(user.get());
    }
}
