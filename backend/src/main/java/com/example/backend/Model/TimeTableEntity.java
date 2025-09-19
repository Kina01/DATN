package com.example.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// import java.time.LocalDate;

@Data
@Entity
@Table(name = "TimeTable")
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTimeTable;

    @Column(name = "subject_name")
    private String subjectName; //Môn

    @Column(name = "class_name")
    private String className; // tên lớp

    @Column(name = "room")
    private String room; //phòng

    @Column(name = "day_of_week")
    private int dayOfWeek; //Thứ

    @Column(name = "period_start")
    private int periodStart; //tiết bắt đầu

    @Column(name = "period_end")
    private int periodEnd; //Tiết kết thúc


    // sẽ bàn sau
    // @Column(name = "week_start")
    // private LocalDate weekStart; //Tuần bắt đầu học

    // @Column(name = "week_end")
    // private LocalDate weekEnd; //Tuần kết thúc học

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}