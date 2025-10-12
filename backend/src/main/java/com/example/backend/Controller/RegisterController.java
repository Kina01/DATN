package com.example.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.Service.EmailVerificationService;
import com.example.backend.Service.RegisterService;
import com.example.backend.DTO.AuthDTO;
import com.example.backend.Model.User;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.0.107:4200" }, 
             allowedHeaders = "*", allowCredentials = "true")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    // Gửi mã xác thực OTP đến email
    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody AuthDTO.VerificationRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Email không được để trống");
                response.put("status", "error");
                return ResponseEntity.badRequest().body(response);
            }
            emailVerificationService.generateAndSendVerificationCode(request.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Mã xác thực đã được gửi đến email");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lỗi hệ thống khi gửi mã xác thực");
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Xác thực mã OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody AuthDTO.VerificationRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            if (request.getVerificationCode() == null || request.getVerificationCode().trim().isEmpty()) {
                return createErrorResponse("Mã xác thực không được để trống");
            }

            boolean isValid = emailVerificationService.verifyCode(
                    request.getEmail().trim().toLowerCase(),
                    request.getVerificationCode().trim());

            Map<String, Object> response = new HashMap<>();
            if (isValid) {
                response.put("message", "Xác thực thành công");
                response.put("status", "success");
                response.put("verified", true);
                emailVerificationService.markEmailAsVerified(request.getEmail().trim().toLowerCase());
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Mã xác thực không hợp lệ hoặc đã hết hạn");
                response.put("status", "error");
                response.put("verified", false);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return createErrorResponse("Lỗi hệ thống khi xác thực mã OTP");
        }
    }

    // Đăng ký tài khoản sinh viên
    @PostMapping("/register/student")
    public ResponseEntity<Map<String, Object>> registerStudent(@RequestBody AuthDTO.RegistrationRequest request) {
        return registerUser(request, User.Role.STUDENT);
    }

    // Đăng ký tài khoản giáo viên
    @PostMapping("/register/teacher")
    public ResponseEntity<Map<String, Object>> registerTeacher(@RequestBody AuthDTO.RegistrationRequest request) {
        return registerUser(request, User.Role.TEACHER);
    }

    private ResponseEntity<Map<String, Object>> registerUser(AuthDTO.RegistrationRequest request, User.Role role) {
        try {
            // Validate input
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                return createErrorResponse("Họ tên không được để trống");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return createErrorResponse("Mật khẩu không được để trống");
            }
            if (request.getPassword().length() < 6) {
                return createErrorResponse("Mật khẩu phải có ít nhất 6 ký tự");
            }

            boolean isRegistered;
            if (role == User.Role.STUDENT) {
                isRegistered = registerService.registerStudent(
                    request.getFullName().trim(),
                    request.getEmail().trim().toLowerCase(),
                    request.getPassword());
            } else {
                isRegistered = registerService.registerTeacher(
                    request.getFullName().trim(),
                    request.getEmail().trim().toLowerCase(),
                    request.getPassword());
            }

            if (isRegistered) {
                // Xóa mã OTP và trạng thái xác thực
                emailVerificationService.clearVerificationOTPStatus(request.getEmail().trim().toLowerCase());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Đăng ký thành công!");
                response.put("status", "success");
                response.put("data", Map.of(
                        "email", request.getEmail(),
                        "fullName", request.getFullName(),
                        "role", role.name()));
                return ResponseEntity.ok(response);
            }

            return createErrorResponse("Email đã tồn tại trong hệ thống");

        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("Lỗi hệ thống khi đăng ký");
        }
    }

    // Helper method để tạo response lỗi
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "error");
        return ResponseEntity.badRequest().body(response);
    }
}