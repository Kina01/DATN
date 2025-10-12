package com.example.backend.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamDTO {

    // DTO cho tạo lịch thi
    public static class CreateExamRequest {
        private Long subjectId;
        private LocalDate examDate;
        private LocalTime examTime;
        private String room;
        private String notes;

        // Getters and Setters
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public LocalDate getExamDate() { return examDate; }
        public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
        public LocalTime getExamTime() { return examTime; }
        public void setExamTime(LocalTime examTime) { this.examTime = examTime; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}