package com.example.GrimMadang.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //시큐리티가 쓰는식별자라 이필드는 삭별자인 전화번호가 들어감으로 , name 이라는 필드가 별도로 존재 
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // 'elder' or 'family'

    @Column(nullable = false)
    private String phoneNumber; // Required for 'elder'

    @ManyToOne
    @JoinColumn(name = "elder_id")
    private User elder; // Parent elder ID for family users

    private Integer attendanceStreak; // Only for 'elder'

    private Integer attendanceTotal; // Only for 'elder'

    @Column(nullable = false)
    private boolean isComment = false; // Only for 'elder'

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void createElder(String username ,String password, String role, String phoneNumber, String eldername, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.password = passwordEncoder.encode(password);
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.name = eldername;
        this.attendanceStreak = 0;
        this.attendanceTotal = 0;
        this.isComment = false; 
    }

    public void createFamily(String username ,String password, String role, String phoneNumber, String eldername, PasswordEncoder passwordEncoder, User elder) {
        this.username = username;
        this.password = passwordEncoder.encode(password);
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.name = eldername;
        this.elder = elder;
    }

    // Getters and Setters
} 