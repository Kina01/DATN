package com.example.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.DTO.RegistrationRequest;
import com.example.backend.DTO.VerificationRequest;
import com.example.backend.Service.EmailVerificationService;
import com.example.backend.Service.RegisterService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.0.107:4200" }, 
            allowedHeaders = "*", 
            allowCredentials = "true", 
            methods = { RequestMethod.GET, 
                        RequestMethod.POST, 
                        RequestMethod.PUT, 
                        RequestMethod.DELETE,
                        RequestMethod.OPTIONS })
public class RegisterController {
    
    @Autowired 
    private RegisterService registerService;
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    // Gửi mã xác thực OTP đến email
    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody VerificationRequest request) {
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
    
    // Xác thực mã OTP nha
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody VerificationRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            if (request.getVerificationCode() == null || request.getVerificationCode().trim().isEmpty()) {
                return createErrorResponse("Mã xác thực không được để trống");
            }
            
            boolean isValid = emailVerificationService.verifyCode(
                request.getEmail().trim().toLowerCase(), 
                request.getVerificationCode().trim()
            );
            
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
    
    // Đăng ký tài khoản (sau khi đã xác thực OTP thành công nha)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegistrationRequest request) {
        try {
            if (request.getFullname() == null || request.getFullname().trim().isEmpty()) {
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
            
            boolean isRegistered = registerService.register(
                request.getFullname().trim(), 
                request.getEmail().trim().toLowerCase(), 
                request.getPassword()
            );
            
            if (isRegistered) {
                // Xóa mã OTP và trạng thái xác thực
                emailVerificationService.clearVerificationOTPStatus(request.getEmail().trim().toLowerCase());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Đăng ký thành công!");
                response.put("status", "success");
                response.put("data", Map.of(
                    "email", request.getEmail(),
                    "fullname", request.getFullname()
                ));
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