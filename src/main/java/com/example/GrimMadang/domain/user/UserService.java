package com.example.GrimMadang.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GrimMadang.shared.utils.JwtTokenProvider;
import com.example.GrimMadang.dto.ElderInfoResponseDTO;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void registerElder(String username, String phoneNumber) {
        User user = new User();
        user.createElder(phoneNumber, username, "ROLE_ELDER", phoneNumber, username, passwordEncoder);
        userRepository.save(user);
    }

    public boolean registerFamily(String username, String phoneNumber, String elderPhoneNumber) {
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

    public ElderInfoResponseDTO getElderInfo(String token) {
        String phoneNumber = jwtTokenProvider.getUsername(token);

        User elder = userRepository.findByPhoneNumber(phoneNumber).orElse(null);

        ElderInfoResponseDTO elderInfoResponseDTO = ElderInfoResponseDTO.builder()
            .name(elder.getName())
            .phoneNumber(elder.getPhoneNumber())
            .role(elder.getRole())
            .attendance_streak(elder.getAttendanceStreak())
            .attendance_total(elder.getAttendanceTotal())
            .elderid(elder.getId().toString())
            .build();

        return elderInfoResponseDTO;
    }
}