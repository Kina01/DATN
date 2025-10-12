package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    // Quan hệ nhiều-1 với Class
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classObj;

    // Quan hệ nhiều-1 với User (Sinh viên)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Quan hệ nhiều-1 với Schedule (Buổi học)
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private LocalDate attendanceDate; // Ngày điểm danh

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AttendanceStatus status; // Trạng thái điểm danh

    @Column(length = 255)
    private String notes; // Ghi chú (lý do vắng, v.v.)

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Enum trạng thái điểm danh
    public enum AttendanceStatus {
        PRESENT,    // Có mặt
        ABSENT,     // Vắng
        LATE,       // Muộn
        EXCUSED     // Có phép
    }
}