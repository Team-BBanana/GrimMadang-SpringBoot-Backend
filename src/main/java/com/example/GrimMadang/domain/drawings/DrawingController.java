package com.example.GrimMadang.domain.drawings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GrimMadang.dto.DrawingRequestDTO;


@RestController
@RequestMapping("/drawings")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDrawing(@CookieValue(value = "jwt", required = false) String token, @RequestBody DrawingRequestDTO drawingRequestDTO) {
        

        drawingService.saveDrawing(
            token, 
            drawingRequestDTO.getTitle(), 
            drawingRequestDTO.getImageUrl1(), 
            drawingRequestDTO.getImageUrl2(), 
            drawingRequestDTO.getDescription()
        );

        return ResponseEntity.ok("Drawing saved successfully");
    }

}
