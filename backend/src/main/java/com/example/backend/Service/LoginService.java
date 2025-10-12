package com.example.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.Model.User;
import com.example.backend.Repository.UserRepository;

@Service
public class LoginService {
    
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    public boolean login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    }
}