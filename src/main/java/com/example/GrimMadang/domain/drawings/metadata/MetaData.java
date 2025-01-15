package com.example.GrimMadang.domain.drawings.metadata;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.GrimMadang.domain.drawings.Drawing;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "drawing_id", nullable = false)
    @JsonIgnore
    private Drawing drawing;

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void createMetaData(Drawing drawing, String topicName, String description) {
        this.drawing = drawing;
        this.topicName = topicName;
        this.description = description;
    }
}
