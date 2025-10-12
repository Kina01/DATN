package com.example.backend.DTO;

import java.time.LocalDateTime;

import com.example.backend.Model.Subject;
import lombok.Data;

@Data
public class SubjectDTO {

    // DTO cho tạo môn học
    public static class CreateSubjectRequest {
        private String subjectCode;
        private String subjectName;
        private Integer credits;

        // Getters and Setters
        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }
    }

    // DTO cho cập nhật môn học
    public static class UpdateSubjectRequest {
        private String subjectCode;
        private String subjectName;
        private Integer credits;

        // Getters and Setters
        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }
    }

    // DTO cho response môn học
    public static class SubjectResponse {
        private Long subjectId;
        private String subjectCode;
        private String subjectName;
        private Integer credits;
        private UserDTO createdBy; // Thêm thông tin người tạo
        private LocalDateTime createdAt;

        public static SubjectResponse fromEntity(Subject subject) {
            SubjectResponse response = new SubjectResponse();
            response.setSubjectId(subject.getSubjectId());
            response.setSubjectCode(subject.getSubjectCode());
            response.setSubjectName(subject.getSubjectName());
            response.setCredits(subject.getCredits());
            response.setCreatedAt(subject.getCreatedAt());

            // Thêm thông tin người tạo
            if (subject.getCreatedBy() != null) {
                response.setCreatedBy(UserDTO.fromEntity(subject.getCreatedBy()));
            }

            return response;
        }

        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }
        public UserDTO getCreatedBy() { return createdBy; }
        public void setCreatedBy(UserDTO createdBy) { this.createdBy = createdBy; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}