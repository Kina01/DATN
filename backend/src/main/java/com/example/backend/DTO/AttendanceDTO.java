package com.example.backend.DTO;

import com.example.backend.Model.Attendance;

import java.time.LocalDate;
import java.util.Map;

public class AttendanceDTO {

    // DTO cho điểm danh
    public static class TakeAttendanceRequest {
        private LocalDate date;
        private Map<Long, Attendance.AttendanceStatus> attendanceMap;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public Map<Long, Attendance.AttendanceStatus> getAttendanceMap() { return attendanceMap; }
        public void setAttendanceMap(Map<Long, Attendance.AttendanceStatus> attendanceMap) { this.attendanceMap = attendanceMap; }
    }

    // DTO cho cập nhật điểm danh
    public static class UpdateAttendanceRequest {
        private Attendance.AttendanceStatus status;
        private String notes;

        public Attendance.AttendanceStatus getStatus() { return status; }
        public void setStatus(Attendance.AttendanceStatus status) { this.status = status; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}