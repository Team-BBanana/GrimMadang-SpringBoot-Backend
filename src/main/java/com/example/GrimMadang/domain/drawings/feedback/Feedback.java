package com.example.GrimMadang.domain.drawings.feedback;

import com.example.GrimMadang.domain.drawings.Drawing;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "drawing_id", nullable = false)
    @JsonIgnore
    private Drawing drawing;

    @Column(nullable = false)
    private String feedback1;

    @Column(nullable = false)
    private String feedback2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void createFeedback(Drawing drawing, String feedback1, String feedback2) {
        this.drawing = drawing;
        this.feedback1 = feedback1;
        this.feedback2 = feedback2;
    }

    // Getters and Setters
} 