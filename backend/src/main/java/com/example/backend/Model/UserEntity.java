package com.example.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullname;
    private String email;
    private String password;
    private String role;

    public UserEntity(String fullname, String email, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = "SV"; // Mặc định là sinh viên
    }
}


// package com.example.backend.Model;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.Data;

// @Data
// @Entity
// @Table(name = "Users")
// public class UserEntity {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private int id;
//     private String fullname;
//     private String email;
//     private String password;
//     private String role;

//     public UserEntity(String fullname, String email, String password) {
//         this.fullname = fullname;  
//         this.email = email;   
//         this.password = password;
//         this.role = "SV";
//     }

//     public UserEntity() {
//         this.fullname = "";        
//         this.password = "";
//         this.email = "";
//         this.role = "SV";
//     }  
// }


// // package com.example.backend.Model;

// // import jakarta.persistence.*;
// // import lombok.*;

// // @Data
// // @Entity
// // @AllArgsConstructor
// // @NoArgsConstructor
// // @Table(name = "User")
// // public class UserEntity {
// //     @Id
// //     @GeneratedValue(strategy = GenerationType.IDENTITY)
// //     @Column(name="UserId")
// //     private int id;

// //     @Column(name="Email")
// //     private String email;

// //     @Column(name="Fullname")
// //     private String fullname;

// //     @Column(name="Username")
// //     private String username;

// //     @Column(name="Password")
// //     private String password;
    
// //     @Column(name="Role")
// //     private UserEntity.Role role = Role.SINHVIEN; 

// //     public enum Role {
// //         GIAOVIEN, SINHVIEN
// //     }
// // }