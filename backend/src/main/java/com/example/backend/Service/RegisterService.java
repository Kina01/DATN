// package com.example.backend.Service;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.backend.Model.UserEntity;
// import com.example.backend.Repository.UserRepository;

// @Service
// public class RegisterService {
    
//     @Autowired private UserRepository register;
//     public boolean register(String fullname, String email, String password) {
//         Optional<UserEntity> optionalRegister = register.findByEmail(email);

//         if(optionalRegister.isPresent()) {
//             return false;
//         }

//         UserEntity newUser = new UserEntity(fullname, email, password);
//         register.save(newUser);

//         return true;
//     }
// }
package com.example.backend.Service;

// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.Model.UserEntity;
import com.example.backend.Repository.UserRepository;

@Service
public class RegisterService {
    
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    // Trong RegisterService, thêm debug
@Transactional
public boolean register(String fullname, String email, String password, String verificationCode) {
    try {
        System.out.println("DEBUG: Starting registration for: " + email);
        
        // Xác thực mã OTP trước
        if (!emailVerificationService.verifyCode(email, verificationCode)) {
            System.out.println("DEBUG: Verification failed for: " + email);
            throw new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn");
        }
        
        System.out.println("DEBUG: Verification passed");
        
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(email)) {
            System.out.println("DEBUG: Email already exists: " + email);
            return false;
        }
        
        // Mã hóa password
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("DEBUG: Password encoded");
        
        // Tạo user mới
        UserEntity newUser = new UserEntity(fullname, email, encodedPassword);
        userRepository.save(newUser);
        System.out.println("DEBUG: User saved to database");
        
        // Xóa mã xác thực
        emailVerificationService.clearVerificationCode(email);
        System.out.println("DEBUG: Verification code cleared");
        
        return true;
        
    } catch (Exception e) {
        System.err.println("ERROR in register: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}
    
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}