package com.giancarlos.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetail implements UserDetails {
    private final User user;

    public UserDetail(User user) {
        this.user = user;
    }

    // Currently sets all accounts as only having user authority
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Assuming account is not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Assuming account is not locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Assuming account credentials is not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Assuming account is already enabled
    @Override
    public boolean isEnabled() {
        return true;
    }
}
