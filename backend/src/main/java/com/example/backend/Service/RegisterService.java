package com.example.backend.Service;

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

    // @Autowired
    // private EmailVerificationService emailVerificationService;

    @Transactional
    public boolean register(String fullname, String email, String password) {
        try {
            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(email)) {
                return false;
            }

            String encodedPassword = passwordEncoder.encode(password);
            UserEntity newUser = new UserEntity(fullname, email, encodedPassword);
            userRepository.save(newUser);

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