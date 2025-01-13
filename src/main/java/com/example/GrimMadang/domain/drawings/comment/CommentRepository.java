package com.example.GrimMadang.domain.drawings.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.example.GrimMadang.domain.drawings.Drawing;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    // 특정 그림에 대한 모든 코멘트를 생성일시 기준 내림차순으로 조회
    List<Comment> findByDrawingOrderByCreatedAtDesc(Drawing drawing);
    
    // 특정 그림에 대한 모든 코멘트 조회
    List<Comment> findByDrawing(Drawing drawing);
    
    // 특정 그림의 코멘트 삭제
    void deleteByDrawing(Drawing drawing);
    
    // 특정 그림의 코멘트 수 조회
    long countByDrawing(Drawing drawing);
}