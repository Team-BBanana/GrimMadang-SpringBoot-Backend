package com.example.GrimMadang.domain.drawings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.GrimMadang.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageUrl1;

    @Column(nullable = false)
    private String imageUrl2;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void createDrawing(User user, String title, String imageUrl1, String imageUrl2, String description) {
        this.user = user;
        this.title = title;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.description = description;
    }

    // Getters and Setters
} 