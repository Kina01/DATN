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
    
    /**
     * Gửi mã xác thực OTP đến email
     * POST /user/send-verification
     */
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
    
    /**
     * Đăng ký tài khoản với xác thực OTP
     * POST /user/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegistrationRequest request) {
        try {
            // Validate input
            if (request.getFullname() == null || request.getFullname().trim().isEmpty()) {
                return createErrorResponse("Họ tên không được để trống");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return createErrorResponse("Mật khẩu không được để trống");
            }
            if (request.getVerificationCode() == null || request.getVerificationCode().trim().isEmpty()) {
                return createErrorResponse("Mã xác thực không được để trống");
            }
            if (request.getPassword().length() < 6) {
                return createErrorResponse("Mật khẩu phải có ít nhất 6 ký tự");
            }
            
            boolean isRegistered = registerService.register(
                request.getFullname().trim(), 
                request.getEmail().trim().toLowerCase(), 
                request.getPassword(), 
                request.getVerificationCode().trim()
            );
            
            if (isRegistered) {
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
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("Lỗi hệ thống khi đăng ký");
        }
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     * GET /user/check-email?email=test@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestParam String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            
            boolean exists = registerService.checkEmailExists(email.trim().toLowerCase());
            
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("message", exists ? "Email đã tồn tại" : "Email có thể sử dụng");
            response.put("status", exists ? "error" : "success");
            response.put("email", email);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return createErrorResponse("Lỗi hệ thống khi kiểm tra email");
        }
    }
    
    /**
     * API test generate mã OTP (chỉ dùng cho development)
     * GET /user/test-otp
     */
    @GetMapping("/test-otp")
    public ResponseEntity<Map<String, Object>> testGenerateOtp() {
        try {
            String testCode = emailVerificationService.generateTestCode();
            
            Map<String, Object> response = new HashMap<>();
            response.put("otp", testCode);
            response.put("message", "Mã OTP test generated");
            response.put("status", "success");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi generate OTP: " + e.getMessage());
        }
    }
    
    /**
     * API test verify mã OTP (chỉ dùng cho development)
     * POST /user/test-verify?email=test@example.com&code=123456
     */
    @PostMapping("/test-verify")
    public ResponseEntity<Map<String, Object>> testVerifyOtp(
            @RequestParam String email,
            @RequestParam String code) {
        
        try {
            if (email == null || email.trim().isEmpty()) {
                return createErrorResponse("Email không được để trống");
            }
            if (code == null || code.trim().isEmpty()) {
                return createErrorResponse("Mã OTP không được để trống");
            }
            
            boolean isValid = emailVerificationService.verifyCode(email.trim().toLowerCase(), code.trim());
            
            Map<String, Object> response = new HashMap<>();
            response.put("isValid", isValid);
            response.put("message", isValid ? "Mã hợp lệ" : "Mã không hợp lệ hoặc đã hết hạn");
            response.put("status", isValid ? "success" : "error");
            response.put("email", email);
            response.put("code", code);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return createErrorResponse("Lỗi verify OTP: " + e.getMessage());
        }
    }
    
    /**
     * Health check endpoint
     * GET /user/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Register service is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Helper method để tạo response lỗi
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "error");
        return ResponseEntity.badRequest().body(response);
    }
}