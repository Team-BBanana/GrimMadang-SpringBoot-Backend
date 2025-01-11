package com.example.GrimMadang.domain.drawings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrimMadang.domain.user.User;
import com.example.GrimMadang.domain.user.UserRepository;
import com.example.GrimMadang.shared.utils.JwtTokenProvider;

@Service
public class DrawingService {

    private DrawingRepository drawingRepository;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DrawingService(DrawingRepository drawingRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.drawingRepository = drawingRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void saveDrawing(String token, String title, String imageUrl1, String imageUrl2, String description) {
        String phoneNumber = jwtTokenProvider.getUsername(token);

        System.out.println(phoneNumber);

        User user = userRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(() -> new RuntimeException("User not found"));

        Drawing drawing = new Drawing();
        drawing.createDrawing(user, title, imageUrl1, imageUrl2, description);
        
        drawingRepository.save(drawing);
    }
    
    public List<Drawing> getDrawings() {
        return drawingRepository.findAll();
    }
    
}
