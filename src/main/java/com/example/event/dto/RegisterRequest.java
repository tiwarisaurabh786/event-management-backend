package com.example.event.dto;

import com.example.event.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
