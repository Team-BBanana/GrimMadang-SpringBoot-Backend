package com.example.GrimMadang.domain.drawings;

import jakarta.persistence.*;


import java.time.LocalDateTime;

import com.example.GrimMadang.domain.user.User;

@Entity
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageUrl1;

    @Column(nullable = false)
    private String imageUrl2;

    @Lob
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
} 