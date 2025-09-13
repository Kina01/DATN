package com.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend.Repository.UserRepository;

import java.security.SecureRandom;
// import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final String CACHE_NAME = "verificationCodes";
    private static final long CODE_EXPIRATION_MINUTES = 10;
    // private static final int OTP_LENGTH = 6;
    
    // Sử dụng SecureRandom để bảo mật hơn
    private final SecureRandom secureRandom = new SecureRandom();
    
    public String generateAndSendVerificationCode(String email) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được đăng ký");
        }
        
        String code = generateSecureRandomCode();
        sendVerificationEmail(email, code);
        
        // Lưu mã vào cache
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(email, code);
        }
        
        return code;
    }
    
    public boolean verifyCode(String email, String code) {
        System.out.println("DEBUG: Verifying code for: " + email);
        System.out.println("DEBUG: Input code: " + code);

        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            System.out.println("DEBUG: Cache is null!");
            return false;
        }
        
        String storedCode = cache.get(email, String.class);
        System.out.println("DEBUG: Stored code: " + storedCode);
        return code != null && code.equals(storedCode);
    }
    
    public void clearVerificationCode(String email) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.evict(email);
        }
    }
    
    /**
     * Tạo mã OTP ngẫu nhiên 6 số bảo mật
     * Sử dụng SecureRandom để đảm bảo tính ngẫu nhiên mạnh
     */
    private String generateSecureRandomCode() {
        // Tạo số ngẫu nhiên từ 0 đến 999999
        int randomNumber = secureRandom.nextInt(1000000);
        
        // Format thành 6 chữ số, thêm số 0 ở đầu nếu cần
        return String.format("%06d", randomNumber);
    }
    
    // /**
    //  * Alternative: Tạo mã OTP bằng cách random từng chữ số
    //  * Đảm bảo luôn có 6 chữ số và không bị thiếu số 0 ở đầu
    //  */
    // private String generateDigitByDigitCode() {
    //     StringBuilder code = new StringBuilder(OTP_LENGTH);
    //     for (int i = 0; i < OTP_LENGTH; i++) {
    //         code.append(secureRandom.nextInt(10)); // Random từ 0-9
    //     }
    //     return code.toString();
    // }
    
    // /**
    //  * Alternative: Tạo mã OTP với custom pattern
    //  * Có thể bao gồm cả chữ và số nếu muốn
    //  */
    // private String generateCustomPatternCode() {
    //     String characters = "0123456789"; // Chỉ số
    //     // String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Số và chữ hoa
    //     // String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Số và chữ
        
    //     StringBuilder code = new StringBuilder(OTP_LENGTH);
    //     for (int i = 0; i < OTP_LENGTH; i++) {
    //         int index = secureRandom.nextInt(characters.length());
    //         code.append(characters.charAt(index));
    //     }
    //     return code.toString();
    // }
    
    private void sendVerificationEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🔐 Mã xác thực đăng ký - DataClass");
            message.setText("Xin chào!\n\n" +
                           "Mã xác thực của bạn là: 🔢 " + code + 
                           "\n\nMã có hiệu lực trong " + CODE_EXPIRATION_MINUTES + " phút." +
                           "\n\n⚠️ Lưu ý: Không chia sẻ mã này với bất kỳ ai." +
                           "\n\nNếu bạn không yêu cầu mã này, vui lòng bỏ qua email này." +
                           "\n\nTrân trọng,\nĐội ngũ DataClass");
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
        }
    }
    
    // Helper method để test mã OTP
    public String generateTestCode() {
        return generateSecureRandomCode();
    }
    
    @Scheduled(fixedRate = 300000) // Dọn dẹp mỗi 5 phút
    public void cleanupExpiredCodes() {
        // Cache sẽ tự động hết hạn theo cấu hình
    }
}