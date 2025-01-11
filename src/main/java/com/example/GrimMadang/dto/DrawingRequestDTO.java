package com.example.GrimMadang.dto;

import lombok.Builder;
import lombok.Getter;

@Getter 
@Builder   
public class DrawingRequestDTO {
    
    private String description;

    private String imageUrl1;

    private String imageUrl2;

    private String title;
}
