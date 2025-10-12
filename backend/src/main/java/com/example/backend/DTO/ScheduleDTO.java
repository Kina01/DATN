package com.example.backend.DTO;

import lombok.Data;

@Data
public class ScheduleDTO {

    // DTO cho tạo lịch học
    public static class CreateScheduleRequest {
        private Long subjectId;
        private String room;
        private Integer dayOfWeek; // 2=Thứ 2, 3=Thứ 3, ..., 7=Chủ nhật
        private Integer startPeriod; // Tiết bắt đầu (1-10)
        private Integer endPeriod;   // Tiết kết thúc (1-10)
        private Integer startWeek;   // Tuần bắt đầu
        private Integer endWeek;     // Tuần kết thúc

        // Getters and Setters
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        public Integer getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Integer getStartPeriod() { return startPeriod; }
        public void setStartPeriod(Integer startPeriod) { this.startPeriod = startPeriod; }
        public Integer getEndPeriod() { return endPeriod; }
        public void setEndPeriod(Integer endPeriod) { this.endPeriod = endPeriod; }
        public Integer getStartWeek() { return startWeek; }
        public void setStartWeek(Integer startWeek) { this.startWeek = startWeek; }
        public Integer getEndWeek() { return endWeek; }
        public void setEndWeek(Integer endWeek) { this.endWeek = endWeek; }
    }

    // DTO cho cập nhật lịch học
    public static class UpdateScheduleRequest {
        private Long subjectId;
        private String room;
        private Integer dayOfWeek;
        private Integer startPeriod;
        private Integer endPeriod;
        private Integer startWeek;
        private Integer endWeek;

        // Getters and Setters
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        public Integer getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Integer getStartPeriod() { return startPeriod; }
        public void setStartPeriod(Integer startPeriod) { this.startPeriod = startPeriod; }
        public Integer getEndPeriod() { return endPeriod; }
        public void setEndPeriod(Integer endPeriod) { this.endPeriod = endPeriod; }
        public Integer getStartWeek() { return startWeek; }
        public void setStartWeek(Integer startWeek) { this.startWeek = startWeek; }
        public Integer getEndWeek() { return endWeek; }
        public void setEndWeek(Integer endWeek) { this.endWeek = endWeek; }
    }

    // DTO cho response lịch học
    public static class ScheduleResponse {
        private Long scheduleId;
        private String room;
        private Integer dayOfWeek;
        private Integer startPeriod;
        private Integer endPeriod;
        private String startTime;
        private String endTime;
        private Integer totalPeriods;
        private Integer startWeek;
        private Integer endWeek;
        private String session;
        private String timeDescription;
        private SubjectDTO.SubjectResponse subject;
        private ClassSimpleDTO classInfo;
        private UserDTO teacher;

        public static ScheduleResponse fromEntity(com.example.backend.Model.Schedule schedule) {
            ScheduleResponse response = new ScheduleResponse();
            response.setScheduleId(schedule.getScheduleId());
            response.setRoom(schedule.getRoom());
            response.setDayOfWeek(schedule.getDayOfWeek());
            response.setStartPeriod(schedule.getStartPeriod());
            response.setEndPeriod(schedule.getEndPeriod());
            response.setStartTime(schedule.getStartTime().toString());
            response.setEndTime(schedule.getEndTime().toString());
            response.setTotalPeriods(schedule.getTotalPeriods());
            response.setStartWeek(schedule.getStartWeek());
            response.setEndWeek(schedule.getEndWeek());
            response.setSession(schedule.getSession());
            response.setTimeDescription(schedule.getTimeDescription());
            
            // Thông tin môn học
            if (schedule.getSubject() != null) {
                response.setSubject(SubjectDTO.SubjectResponse.fromEntity(schedule.getSubject()));
            }
            
            // Thông tin lớp học
            if (schedule.getClassObj() != null) {
                response.setClassInfo(ClassSimpleDTO.fromEntity(schedule.getClassObj()));
            }
            
            // Thông tin giáo viên
            if (schedule.getTeacher() != null) {
                response.setTeacher(UserDTO.fromEntity(schedule.getTeacher()));
            }
            
            return response;
        }

        // Getters and Setters
        public Long getScheduleId() { return scheduleId; }
        public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        public Integer getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Integer getStartPeriod() { return startPeriod; }
        public void setStartPeriod(Integer startPeriod) { this.startPeriod = startPeriod; }
        public Integer getEndPeriod() { return endPeriod; }
        public void setEndPeriod(Integer endPeriod) { this.endPeriod = endPeriod; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public Integer getTotalPeriods() { return totalPeriods; }
        public void setTotalPeriods(Integer totalPeriods) { this.totalPeriods = totalPeriods; }
        public Integer getStartWeek() { return startWeek; }
        public void setStartWeek(Integer startWeek) { this.startWeek = startWeek; }
        public Integer getEndWeek() { return endWeek; }
        public void setEndWeek(Integer endWeek) { this.endWeek = endWeek; }
        public String getSession() { return session; }
        public void setSession(String session) { this.session = session; }
        public String getTimeDescription() { return timeDescription; }
        public void setTimeDescription(String timeDescription) { this.timeDescription = timeDescription; }
        public SubjectDTO.SubjectResponse getSubject() { return subject; }
        public void setSubject(SubjectDTO.SubjectResponse subject) { this.subject = subject; }
        public ClassSimpleDTO getClassInfo() { return classInfo; }
        public void setClassInfo(ClassSimpleDTO classInfo) { this.classInfo = classInfo; }
        public UserDTO getTeacher() { return teacher; }
        public void setTeacher(UserDTO teacher) { this.teacher = teacher; }
    }
}