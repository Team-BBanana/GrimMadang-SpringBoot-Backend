package com.example.GrimMadang.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerElder(String username, String phoneNumber) {
        User user = new User();
        user.createElder(phoneNumber, username, "ROLE_ELDER", phoneNumber, username, passwordEncoder);
        userRepository.save(user);
    }

    public boolean registerFamily(String username,String phoneNumber, String elderPhoneNumber) {
        Optional<User> elder = userRepository.findByPhoneNumber(elderPhoneNumber);

        if (elder.isPresent()) {
            User user = elder.get();
            User familyUser = new User();
            familyUser.createFamily(phoneNumber, username,  "ROLE_FAMILY",phoneNumber, username ,passwordEncoder, user);
            userRepository.save(familyUser);
            return true;
        }
        return false;
    }
}
