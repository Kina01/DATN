package com.example.backend.Model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "Users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public UserEntity(String fullname, String email, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = "SV";
    }

    @OneToMany(mappedBy = "user")
    private List<TimeTableEntity> timeTables;
}