package com.example.GrimMadang.security;

import com.example.GrimMadang.domain.user.UserRepository;
import com.example.GrimMadang.shared.exceptions.CustomUsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username)
                .map(userData -> {
                    userData.setUsername(userData.getPhoneNumber());
                    return new CustomUserDetail(userData);
                })
                .orElseThrow(() -> new CustomUsernameNotFoundException(username));
    } 

}