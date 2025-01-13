package com.example.GrimMadang.domain.drawings.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.example.GrimMadang.domain.drawings.Drawing;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByDrawing(Drawing drawing);
    Feedback findByDrawing_Id(UUID drawingId);
    void deleteByDrawing(Drawing drawing);
} 