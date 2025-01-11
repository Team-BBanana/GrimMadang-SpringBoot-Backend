package com.example.GrimMadang.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ElderInfoResponseDTO {
    private String elderid;
    private String name;
    private String phoneNumber;
    private String role;
    private Integer attendance_streak;
    private Integer attendance_total;
} 