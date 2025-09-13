package com.example.backend.DTO;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String verificationCode;
}