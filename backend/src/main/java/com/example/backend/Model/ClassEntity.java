package com.example.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @Column(nullable = false, unique = true, length = 50)
    private String classCode;

    @Nationalized
    @Column(nullable = false, length = 255)
    private String className;

    @Nationalized
    @Column(columnDefinition = "NTEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @OneToMany(mappedBy = "classObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "classObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassStudent> classStudents = new ArrayList<>();

    @OneToMany(mappedBy = "classObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    // === QUAN HỆ MỚI THÊM CHO ĐIỂM DANH VÀ CHẤM ĐIỂM ===
    
    // Quan hệ 1-nhiều với Attendance
    @OneToMany(mappedBy = "classObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    // Quan hệ 1-nhiều với Grade
    @OneToMany(mappedBy = "classObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();
}