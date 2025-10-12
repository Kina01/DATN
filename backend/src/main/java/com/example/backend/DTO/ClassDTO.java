package com.example.backend.DTO;

import com.example.backend.Model.ClassEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClassDTO {

    // DTO cho tạo lớp học (Request)
    public static class CreateClassRequest {
        private String classCode;
        private String className;
        private String description;

        // Getters and Setters
        public String getClassCode() { return classCode; }
        public void setClassCode(String classCode) { this.classCode = classCode; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // DTO cho cập nhật lớp học (Request)
    public static class UpdateClassRequest {
        private String classCode;
        private String className;
        private String description;

        // Getters and Setters
        public String getClassCode() { return classCode; }
        public void setClassCode(String classCode) { this.classCode = classCode; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // DTO cho thông tin lớp học (Response - chi tiết đầy đủ)
    public static class ClassResponse {
        private Long classId;
        private String classCode;
        private String className;
        private String description;
        private UserDTO teacher;
        private List<UserDTO> students;
        private Integer studentCount;

        public static ClassResponse fromEntity(ClassEntity classEntity) {
            ClassResponse response = new ClassResponse();
            response.setClassId(classEntity.getClassId());
            response.setClassCode(classEntity.getClassCode());
            response.setClassName(classEntity.getClassName());
            response.setDescription(classEntity.getDescription());
            
            // Chỉ lấy thông tin cơ bản của teacher
            if (classEntity.getTeacher() != null) {
                response.setTeacher(UserDTO.fromEntity(classEntity.getTeacher()));
            }
            
            // Chuyển đổi danh sách sinh viên
            if (classEntity.getClassStudents() != null) {
                response.setStudents(classEntity.getClassStudents().stream()
                        .map(classStudent -> UserDTO.fromEntity(classStudent.getStudent()))
                        .collect(Collectors.toList()));
                response.setStudentCount(classEntity.getClassStudents().size());
            } else {
                response.setStudentCount(0);
            }
            
            return response;
        }

        // Getters and Setters
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        public String getClassCode() { return classCode; }
        public void setClassCode(String classCode) { this.classCode = classCode; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public UserDTO getTeacher() { return teacher; }
        public void setTeacher(UserDTO teacher) { this.teacher = teacher; }
        public List<UserDTO> getStudents() { return students; }
        public void setStudents(List<UserDTO> students) { this.students = students; }
        public Integer getStudentCount() { return studentCount; }
        public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    }

    // DTO cho danh sách lớp học (Response - tóm tắt)
    public static class ClassSummary {
        private Long classId;
        private String classCode;
        private String className;
        private String description;
        private String teacherName;
        private Integer studentCount;

        public static ClassSummary fromEntity(ClassEntity classEntity) {
            ClassSummary summary = new ClassSummary();
            summary.setClassId(classEntity.getClassId());
            summary.setClassCode(classEntity.getClassCode());
            summary.setClassName(classEntity.getClassName());
            summary.setDescription(classEntity.getDescription());
            
            if (classEntity.getTeacher() != null) {
                summary.setTeacherName(classEntity.getTeacher().getFullName());
            }
            
            if (classEntity.getClassStudents() != null) {
                summary.setStudentCount(classEntity.getClassStudents().size());
            } else {
                summary.setStudentCount(0);
            }
            
            return summary;
        }

        // Getters and Setters
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        public String getClassCode() { return classCode; }
        public void setClassCode(String classCode) { this.classCode = classCode; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getTeacherName() { return teacherName; }
        public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
        public Integer getStudentCount() { return studentCount; }
        public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    }

    // DTO cho thống kê lớp học
    public static class ClassStatistics {
        private Long classId;
        private String className;
        private Integer totalStudents;
        private Integer presentCount;
        private Integer absentCount;
        private Double averageScore;

        // Getters and Setters
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Integer getPresentCount() { return presentCount; }
        public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }
        public Integer getAbsentCount() { return absentCount; }
        public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }
        public Double getAverageScore() { return averageScore; }
        public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
    }
}