package com.example.GrimMadang.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println( "username: " + elderSignupDTO.getUsername());
        System.out.println( "phoneNumber: " + elderSignupDTO.getPhoneNumber());

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


    @GetMapping("/getelderinfo")
    public ResponseEntity<?> getElderInfo(@CookieValue(value = "jwt", required = false) String token) {
        ElderInfoResponseDTO elderInfo = userService.getElderInfo(token);
        return ResponseEntity.ok(elderInfo);
    }
}
