package com.example.GrimMadang.domain.drawings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.GrimMadang.domain.drawings.comment.Comment;
import com.example.GrimMadang.domain.drawings.feedback.Feedback;
import com.example.GrimMadang.dto.DrawingRequestDTO;
import com.example.GrimMadang.dto.CommentRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingService drawingService;

    @Autowired
    public DrawingController(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDrawing(
            @CookieValue(value = "jwt", required = false) String token,
            @RequestBody DrawingRequestDTO drawingRequestDTO) {
        try {
            drawingService.saveDrawing(
                token,
                drawingRequestDTO.getTitle(),
                drawingRequestDTO.getImageUrl1(),
                drawingRequestDTO.getImageUrl2(),
                drawingRequestDTO.getDescription(),
                drawingRequestDTO.getFeedback1(),
                drawingRequestDTO.getFeedback2()
            );
            return ResponseEntity.ok("그림이 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/getDrawings")
    public ResponseEntity<?> getDrawings(@CookieValue(value = "jwt", required = false) String token) {
        try {

            List<Drawing> drawings = drawingService.getDrawings(token);
            return ResponseEntity.ok(drawings);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());

        }
    }

    @GetMapping("/{drawingId}")
    public ResponseEntity<?> getDrawing(
            @CookieValue(value = "jwt", required = false) String token,
            @PathVariable String drawingId) {
        try {
            Drawing drawing = drawingService.getDrawing(token, drawingId);
            return ResponseEntity.ok(drawing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/{drawingId}/getfeedbacks")
    public ResponseEntity<?> getFeedbacks(
            @CookieValue(value = "jwt", required = false) String token,
            @PathVariable String drawingId) {
        try {
            Feedback feedbacks = drawingService.getFeedbacks(token, drawingId);
            return ResponseEntity.ok(feedbacks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/{drawingId}/comment")
    public ResponseEntity<?> saveComment(
            @CookieValue(value = "jwt", required = false) String token,
            @PathVariable String drawingId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            drawingService.saveComment(token, drawingId, 
                commentRequestDTO.getTitle(), 
                commentRequestDTO.getContent());
            return ResponseEntity.ok("코멘트가 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/{drawingId}/comments")
    public ResponseEntity<?> getComments(
            @CookieValue(value = "jwt", required = false) String token,
            @PathVariable String drawingId) {
        try {
            List<Comment> comments = drawingService.getComments(token, drawingId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
