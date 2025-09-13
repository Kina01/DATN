package com.example.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.Model.UserEntity;
import com.example.backend.Service.LoginService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.0.107:4200" }, 
            allowedHeaders = "*", 
            allowCredentials = "true", 
            methods = { RequestMethod.GET, 
                        RequestMethod.POST, 
                        RequestMethod.PUT, 
                        RequestMethod.DELETE })
public class LoginController {
    
    @Autowired private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserEntity login) {
        try {
            boolean check = loginService.login(login.getEmail(), login.getPassword());
            Map<String, String> response = new HashMap<>();

            if(check)
            {
                UserEntity user = loginService.findByAccount(login.getEmail());
                response.put("message", "Đăng nhập thành công!");
                response.put("id", String.valueOf(user.getId()));
                response.put("fullname", user.getFullname());
                response.put("role", user.getRole());
                return ResponseEntity.ok(response);
            }
            response.put("message", "Đăng nhập thất bại!");
            response.put("status", "error");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi hệ thống!");
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }
}
