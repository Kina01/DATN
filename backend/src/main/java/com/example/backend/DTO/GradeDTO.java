package com.example.backend.DTO;

public class GradeDTO {

    // DTO cho chấm điểm
    public static class GradeRequest {
        private Double processScore;
        private Double midtermScore;
        private String comments;

        public Double getProcessScore() { return processScore; }
        public void setProcessScore(Double processScore) { this.processScore = processScore; }
        public Double getMidtermScore() { return midtermScore; }
        public void setMidtermScore(Double midtermScore) { this.midtermScore = midtermScore; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    // DTO cho chấm điểm hàng loạt
    public static class GradeUpdateRequest {
        private Long studentId;
        private Double processScore;
        private Double midtermScore;
        private String comments;

        // Getters and Setters
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public Double getProcessScore() { return processScore; }
        public void setProcessScore(Double processScore) { this.processScore = processScore; }
        public Double getMidtermScore() { return midtermScore; }
        public void setMidtermScore(Double midtermScore) { this.midtermScore = midtermScore; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }
}