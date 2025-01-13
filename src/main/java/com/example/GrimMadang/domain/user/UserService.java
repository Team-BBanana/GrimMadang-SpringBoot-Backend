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
        try {
            // 전화번호 중복 검사
            if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 전화번호입니다: " + phoneNumber);
            }

            User user = new User();
            user.createElder(phoneNumber, username, "ROLE_ELDER", phoneNumber, username, passwordEncoder);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("노인 사용자 등록에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public boolean registerFamily(String username, String phoneNumber, String elderPhoneNumber) {
        try {
            // 전화번호 중복 검사
            if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 전화번호입니다: " + phoneNumber);
            }

            Optional<User> elder = userRepository.findByPhoneNumber(elderPhoneNumber);
            if (elder.isEmpty()) {
                throw new IllegalArgumentException("해당 전화번호로 등록된 노인을 찾을 수 없습니다: " + elderPhoneNumber);
            }

            User user = elder.get();
            if (!"ROLE_ELDER".equals(user.getRole())) {
                throw new IllegalArgumentException("해당 사용자는 노인이 아닙니다: " + elderPhoneNumber);
            }

            User familyUser = new User();
            familyUser.createFamily(phoneNumber, username, "ROLE_FAMILY", phoneNumber, username, passwordEncoder, user);
            userRepository.save(familyUser);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("가족 사용자 등록에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public ElderInfoResponseDTO getElderInfo(String token) {
        try {
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            User elder = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));


            return ElderInfoResponseDTO.builder()
                .name(elder.getName())
                .phoneNumber(elder.getPhoneNumber())
                .role(elder.getRole())
                .attendance_streak(elder.getAttendanceStreak())
                .attendance_total(elder.getAttendanceTotal())
                .elderId(elder.getId().toString())
                .build();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("노인 정보 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }
}