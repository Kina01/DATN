package com.example.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.Model.UserEntity;
import com.example.backend.Repository.UserRepository;

@Service
public class LoginService {
    
    @Autowired private UserRepository login;
    @Autowired private PasswordEncoder passwordEncoder;

    public boolean login(String email, String password)
    {
        Optional<UserEntity> optionalLogin = login.findByEmail(email);

        if(optionalLogin.isPresent()){
            UserEntity user = optionalLogin.get();
            
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public UserEntity findByAccount(String email) {
        return login.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    }
}