package com.learn.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {

    @Id
    private int id;
    private String name;
    private String gender;
    private String dob;
    private String role;
    private String email;
    private String address;
}
