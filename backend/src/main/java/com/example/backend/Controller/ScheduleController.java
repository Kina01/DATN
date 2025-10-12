package com.example.backend.Controller;

import com.example.backend.DTO.ScheduleDTO;
import com.example.backend.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.0.107:4200" }, 
             allowedHeaders = "*", allowCredentials = "true")
public class ScheduleController {
 
    @Autowired
    private ScheduleService scheduleService;

    // Tạo lịch học mới - SỬA LẠI THÀNH DTO
    @PostMapping("/add/{classId}")
    public ResponseEntity<Map<String, Object>> createSchedule(@PathVariable Long classId,
                                                             @RequestBody ScheduleDTO.CreateScheduleRequest request, // SỬA THÀNH DTO
                                                             @RequestHeader("User-ID") Long teacherId) {
        try {
            var createdSchedule = scheduleService.createSchedule(request, classId, teacherId);
            var scheduleResponse = ScheduleDTO.ScheduleResponse.fromEntity(createdSchedule);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tạo lịch học thành công");
            response.put("status", "success");
            response.put("data", scheduleResponse);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Cập nhật lịch học - SỬA LẠI THÀNH DTO
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateSchedule(@PathVariable Long scheduleId,
                                                             @RequestBody ScheduleDTO.UpdateScheduleRequest request, // SỬA THÀNH DTO
                                                             @RequestHeader("User-ID") Long teacherId) {
        try {
            var updatedSchedule = scheduleService.updateSchedule(scheduleId, request, teacherId);
            var scheduleResponse = ScheduleDTO.ScheduleResponse.fromEntity(updatedSchedule);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cập nhật lịch học thành công");
            response.put("status", "success");
            response.put("data", scheduleResponse);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Xóa lịch học (giữ nguyên)
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@PathVariable Long scheduleId,
                                                             @RequestHeader("User-ID") Long teacherId) {
        try {
            scheduleService.deleteSchedule(scheduleId, teacherId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Xóa lịch học thành công");
            response.put("status", "success");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Lấy lịch học theo lớp - SỬA LẠI TRẢ VỀ DTO
    @GetMapping("/class/{classId}")
    public ResponseEntity<Map<String, Object>> getSchedulesByClass(@PathVariable Long classId) {
        try {
            var schedules = scheduleService.getSchedulesByClass(classId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lấy lịch học thành công");
            response.put("status", "success");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Lấy lịch dạy của giáo viên - SỬA LẠI TRẢ VỀ DTO
    @GetMapping("/teacher")
    public ResponseEntity<Map<String, Object>> getTeacherSchedules(@RequestHeader("User-ID") Long teacherId) {
        try {
            var schedules = scheduleService.getSchedulesByTeacher(teacherId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lấy lịch dạy thành công");
            response.put("status", "success");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Lấy lịch học theo tuần và ngày - SỬA LẠI TRẢ VỀ DTO
    @GetMapping("/week/{week}/day/{dayOfWeek}")
    public ResponseEntity<Map<String, Object>> getSchedulesByWeekAndDay(@PathVariable Integer week,
                                                                       @PathVariable Integer dayOfWeek) {
        try {
            var schedules = scheduleService.getSchedulesByWeekAndDay(week, dayOfWeek);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lấy lịch học thành công");
            response.put("status", "success");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lỗi khi lấy lịch học");
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Lấy lịch học theo lớp và tuần - SỬA LẠI TRẢ VỀ DTO
    @GetMapping("/class/{classId}/week/{week}")
    public ResponseEntity<Map<String, Object>> getSchedulesByClassAndWeek(@PathVariable Long classId,
                                                                         @PathVariable Integer week) {
        try {
            var schedules = scheduleService.getSchedulesByClassAndWeek(classId, week);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lấy lịch học thành công");
            response.put("status", "success");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}