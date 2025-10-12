package com.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.Model.User;
import com.example.backend.Repository.UserRepository;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean register(String fullName, String email, String password, User.Role role) {
        try {
            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(email)) {
                return false;
            }

            String encodedPassword = passwordEncoder.encode(password);
            User newUser = new User(email, encodedPassword, fullName, role);
            userRepository.save(newUser);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public boolean registerStudent(String fullName, String email, String password) {
        return register(fullName, email, password, User.Role.STUDENT);
    }

    @Transactional
    public boolean registerTeacher(String fullName, String email, String password) {
        return register(fullName, email, password, User.Role.TEACHER);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}