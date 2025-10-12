package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    // Quan hệ nhiều-1 với Class
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classObj;

    // Quan hệ nhiều-1 với User (Sinh viên)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Quan hệ nhiều-1 với Subject (Môn học)
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Double processScore; // Điểm quá trình

    @Column(nullable = false)
    private Double midtermScore; // Điểm giữa kỳ

    @Column(length = 500)
    private String comments; // Nhận xét của giáo viên

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}