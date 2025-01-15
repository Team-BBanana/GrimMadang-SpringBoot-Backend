package com.example.GrimMadang.domain.drawings;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrimMadang.domain.drawings.feedback.Feedback;
import com.example.GrimMadang.domain.user.User;
import com.example.GrimMadang.domain.user.UserRepository;
import com.example.GrimMadang.dto.MetaDataRequestDTO;
import com.example.GrimMadang.shared.utils.JwtTokenProvider;
import com.example.GrimMadang.domain.drawings.feedback.FeedbackRepository;
import com.example.GrimMadang.domain.drawings.comment.Comment;
import com.example.GrimMadang.domain.drawings.comment.CommentRepository;
import com.example.GrimMadang.domain.drawings.metadata.MetaData;
import com.example.GrimMadang.domain.drawings.metadata.MetaDataRepository;


@Service
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentRepository commentRepository;
    private final MetaDataRepository metaDataRepository;

    @Autowired
    public DrawingService(
            DrawingRepository drawingRepository, 
            UserRepository userRepository,
            FeedbackRepository feedbackRepository,
            JwtTokenProvider jwtTokenProvider,
            CommentRepository commentRepository,
            MetaDataRepository metaDataRepository) {
        this.drawingRepository = drawingRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.commentRepository = commentRepository;
        this.metaDataRepository = metaDataRepository;
    }

    public void saveDrawing(String token, String title, String imageUrl1, String imageUrl2, 
            String description, String feedback1, String feedback2) {
        try {
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            Drawing drawing = new Drawing();
            drawing.createDrawing(user, title, imageUrl1, imageUrl2, description);
            drawingRepository.save(drawing);  // 먼저 Drawing을 저장

            // Feedback 생성 및 저장
            Feedback feedback = new Feedback();
            feedback.createFeedback(drawing, feedback1, feedback2);
            feedbackRepository.save(feedback);

        } catch (Exception e) {
            throw new RuntimeException("그림 저장에 실패했습니다: " + e.getMessage(), e);
        }
    }
    
    public List<Drawing> getDrawings(String token) {
        try {
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            // 사용자가 존재하는지 먼저 확인
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            // 사용자의 역할에 따라 다른 처리
            if ("ROLE_FAMILY".equals(user.getRole())) {
                // 가족 사용자인 경우, 연결된 노인의 그림을 조회
                User elder = user.getElder();
                if (elder == null) {
                    throw new RuntimeException("연결된 노인 사용자를 찾을 수 없습니다");
                }
                return drawingRepository.findAllByUser_PhoneNumber(elder.getPhoneNumber());
            } else {
                // 노인 사용자인 경우, 자신의 그림을 조회
                return drawingRepository.findAllByUser_PhoneNumber(phoneNumber);
            }
        } catch (Exception e) {
            throw new RuntimeException("그림 목록을 가져오는데 실패했습니다: " + e.getMessage(), e);
        }
    }

    public Drawing getDrawing(String token, String drawingId) {
        try {
            // 토큰으로 사용자 확인
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            // 사용자가 존재하는지 확인
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            // 그림 조회
            Drawing drawing = drawingRepository.findById(UUID.fromString(drawingId))
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 그림을 찾을 수 없습니다: " + drawingId));

            // 사용자의 역할에 따라 다른 접근 권한 체크
            if ("ROLE_FAMILY".equals(user.getRole())) {
                // 가족 사용자인 경우, 연결된 노인의 그림인지 확인
                User elder = user.getElder();
                if (elder == null || !drawing.getUser().getId().equals(elder.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            } else {
                // 노인 사용자인 경우, 자신의 그림인지 확인
                if (!drawing.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            }

            return drawing;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("그림을 가져오는데 실패했습니다: " + e.getMessage(), e);
        }
    }

    public Feedback getFeedbacks(String token, String drawingId) {
        try {
            // 토큰으로 사용자 확인
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            // 사용자가 존재하는지 확인
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            // 그림 조회
            Drawing drawing = drawingRepository.findById(UUID.fromString(drawingId))
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 그림을 찾을 수 없습니다: " + drawingId));

            // 사용자의 역할에 따라 다른 접근 권한 체크
            if ("ROLE_FAMILY".equals(user.getRole())) {
                // 가족 사용자인 경우, 연결된 노인의 그림인지 확인
                User elder = user.getElder();
                if (elder == null || !drawing.getUser().getId().equals(elder.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            } else {
                // 노인 사용자인 경우, 자신의 그림인지 확인
                if (!drawing.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            }

            // 피드백 조회
            return feedbackRepository.findByDrawing_Id(drawing.getId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("피드백을 가져오는데 실패했습니다: " + e.getMessage(), e);
        }
    }

    public void saveComment(String token, String drawingId, String title, String content) {
        try {
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            // 사용자가 존재하는지 확인하고 가족 사용자인지 확인
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            if (!"ROLE_FAMILY".equals(user.getRole())) {
                throw new IllegalArgumentException("가족 사용자만 코멘트를 작성할 수 있습니다");
            }

            // 그림 조회
            Drawing drawing = drawingRepository.findById(UUID.fromString(drawingId))
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 그림을 찾을 수 없습니다: " + drawingId));

            // 연결된 노인의 그림인지 확인
            User elder = user.getElder();
            if (elder == null || !drawing.getUser().getId().equals(elder.getId())) {
                throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
            }

            // 코멘트 생성 및 저장
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setDrawing(drawing);
            comment.setTitle("가족댓글");
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

        } catch (Exception e) {
            throw new RuntimeException("코멘트 저장에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public List<Comment> getComments(String token, String drawingId) {
        try {
            String phoneNumber = jwtTokenProvider.getUsername(token);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 토큰: 전화번호를 찾을 수 없습니다");
            }

            // 사용자가 존재하는지 확인
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("해당 전화번호로 등록된 사용자를 찾을 수 없습니다: " + phoneNumber));

            // 그림 조회
            Drawing drawing = drawingRepository.findById(UUID.fromString(drawingId))
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 그림을 찾을 수 없습니다: " + drawingId));

            // 사용자의 역할에 따라 다른 접근 권한 체크
            if ("ROLE_FAMILY".equals(user.getRole())) {
                // 가족 사용자인 경우, 연결된 노인의 그림인지 확인
                User elder = user.getElder();
                if (elder == null || !drawing.getUser().getId().equals(elder.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            } else {
                // 노인 사용자인 경우, 자신의 그림인지 확인
                if (!drawing.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("해당 그림에 대한 접근 권한이 없습니다");
                }
            }

            // 코멘트 목록 조회
            return commentRepository.findByDrawingOrderByCreatedAtDesc(drawing);

        } catch (Exception e) {
            throw new RuntimeException("코멘트 목록 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public void saveMetaData(MetaDataRequestDTO metaDataRequestDTO) {
        try {
            MetaData metaData = new MetaData();
            metaData.setTopicName(metaDataRequestDTO.getTopicName());
            metaData.setDescription(metaDataRequestDTO.getDescription());
            metaDataRepository.save(metaData);
        } catch (Exception e) {
            throw new RuntimeException("메타데이터 저장에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public MetaData getMetaDataByTopic(String topicName) {
        try {

            return metaDataRepository.findByTopicName(topicName);
            
        } catch (Exception e) {

            throw new RuntimeException("메타데이터 조회에 실패했습니다: " + e.getMessage(), e);
        }
    }

}
