package com.example.GrimMadang.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.GrimMadang.dto.ElderSignupDTO;
import com.example.GrimMadang.dto.FamilySignupDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/elder")
    public ResponseEntity<?> registerElder(@RequestBody ElderSignupDTO elderSignupDTO) {

        userService.registerElder(
            elderSignupDTO.getUsername(), 
            elderSignupDTO.getPhoneNumber()
        );

        return ResponseEntity.ok("Elder registered successfully");
    }

    @PostMapping("/register/family")
    public ResponseEntity<String> registerFamily(@RequestBody FamilySignupDTO familySignupDTO) {

        userService.registerFamily(
            familySignupDTO.getUsername(), 
            familySignupDTO.getPhoneNumber(), 
            familySignupDTO.getElderPhoneNumber()
        );
        return ResponseEntity.ok("Family registered successfully");
    }
}
