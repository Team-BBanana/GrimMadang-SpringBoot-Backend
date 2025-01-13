package com.example.GrimMadang.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.example.GrimMadang.dto.ElderInfoResponseDTO;
import com.example.GrimMadang.dto.ElderSignupDTO;
import com.example.GrimMadang.dto.FamilySignupDTO;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/elder")
    public ResponseEntity<?> registerElder(@RequestBody ElderSignupDTO elderSignupDTO) {
        try {
            userService.registerElder(
                elderSignupDTO.getUsername(), 
                elderSignupDTO.getPhoneNumber()
            );
            return ResponseEntity.ok("노인 사용자가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/register/family")
    public ResponseEntity<?> registerFamily(@RequestBody FamilySignupDTO familySignupDTO) {
        try {
            userService.registerFamily(
                familySignupDTO.getUsername(), 
                familySignupDTO.getPhoneNumber(), 
                familySignupDTO.getElderPhoneNumber()
            );
            return ResponseEntity.ok("가족 사용자가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/getelderinfo")
    public ResponseEntity<?> getElderInfo(@CookieValue(value = "jwt", required = false) String token) {
        try {
            ElderInfoResponseDTO elderInfo = userService.getElderInfo(token);
            return ResponseEntity.ok(elderInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
