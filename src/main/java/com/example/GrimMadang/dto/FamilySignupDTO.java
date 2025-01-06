package com.example.GrimMadang.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilySignupDTO {

    private String username;

    private String phoneNumber;

    private String elderPhoneNumber;
}
