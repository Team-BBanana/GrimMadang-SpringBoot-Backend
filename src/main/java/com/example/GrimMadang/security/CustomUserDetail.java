package com.example.GrimMadang.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.GrimMadang.domain.user.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetail implements UserDetails {

    private final User user;


    public CustomUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getUsername();
    }

    @Override
    public String getUsername() {
        return user.getPhoneNumber();
    }
}
