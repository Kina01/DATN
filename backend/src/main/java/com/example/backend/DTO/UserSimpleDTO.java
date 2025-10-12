package com.example.backend.DTO;

import com.example.backend.Model.User;
import lombok.Data;

@Data
public class UserSimpleDTO {
    private Long userId;
    private String email;
    private String fullName;
    private User.Role role;

    public static UserSimpleDTO fromEntity(User user) {
        UserSimpleDTO dto = new UserSimpleDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        return dto;
    }
}