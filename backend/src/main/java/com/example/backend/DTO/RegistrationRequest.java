package com.example.backend.DTO;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String fullname;
    private String email;
    private String password;
}