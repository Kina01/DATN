// package com.example.backend.Controller;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.backend.Model.UserEntity;
// import com.example.backend.Service.LoginService;

// @RestController
// @RequestMapping("/user")
// @CrossOrigin(origins = { "http://localhost:4200",
//         "http://192.168.0.107:4200" }, allowedHeaders = "*", allowCredentials = "true", methods = { RequestMethod.GET,
//                 RequestMethod.POST,
//                 RequestMethod.PUT,
//                 RequestMethod.DELETE })
// public class LoginController {

//     @Autowired
//     private LoginService loginService;

//     @PostMapping("/login")
//     public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity login) {
//         try {
//             boolean check = loginService.login(login.getEmail(), login.getPassword());
//             Map<String, String> response = new HashMap<>();

//             if (check) {
//                 UserEntity user = loginService.findByAccount(login.getEmail());
//                 response.put("message", "Đăng nhập thành công!");
//                 response.put("id", String.valueOf(user.getId()));
//                 response.put("fullname", user.getFullname());
//                 response.put("role", user.getRole());
//                 return ResponseEntity.ok(response);
//             }
//             response.put("message", "Đăng nhập thất bại!");
//             response.put("status", "error");
//             return ResponseEntity.status(400).body(response);
//         } catch (Exception e) {
//             e.printStackTrace();
//             Map<String, String> response = new HashMap<>();
//             response.put("message", "Lỗi hệ thống!");
//             response.put("status", "error");
//             return ResponseEntity.status(500).body(response);
//         }
//     }
// }


package com.example.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.DTO.AuthDTO;
import com.example.backend.Model.User;
import com.example.backend.Service.LoginService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200", "http://192.168.0.107:4200" }, 
             allowedHeaders = "*", allowCredentials = "true")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO.LoginRequest loginRequest) {
        try {
            boolean check = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
            Map<String, Object> response = new HashMap<>();

            if (check) {
                User user = loginService.findByEmail(loginRequest.getEmail());
                response.put("message", "Đăng nhập thành công!");
                response.put("status", "success");
                response.put("data", Map.of(
                    "userId", user.getUserId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "role", user.getRole().toString()
                ));
                return ResponseEntity.ok(response);
            }
            response.put("message", "Email hoặc mật khẩu không đúng!");
            response.put("status", "error");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lỗi hệ thống!");
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }
}