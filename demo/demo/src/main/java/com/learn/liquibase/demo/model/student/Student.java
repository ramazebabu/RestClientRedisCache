package com.learn.liquibase.demo.model.student;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "stud_name")
    private String name;

    @Column(name = "stud_gender")
    private String gender;

    @Column(name = "stud_email")
    private String email;

    @Column(name = "stud_dob")
    private String dob;

    @Column(name = "stud_group")
    private String group;

    @Column(name = "address")
    private String address;
}
