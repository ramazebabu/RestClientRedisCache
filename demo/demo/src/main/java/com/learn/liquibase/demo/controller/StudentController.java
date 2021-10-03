package com.learn.liquibase.demo.controller;

import com.learn.liquibase.demo.model.employee.Employee;
import com.learn.liquibase.demo.model.student.Student;
import com.learn.liquibase.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {


    private StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("addProfile")
    public ResponseEntity<?> addProfile(@RequestBody Student student){
        return new ResponseEntity<>(service.addProfile(student), HttpStatus.CREATED);
    }

    @GetMapping("getAllProfile")
    public ResponseEntity<?> getAllProfiles(){
        return new ResponseEntity<>(service.allProfiles(), HttpStatus.OK);
    }

    @PutMapping("updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Student student){
        service.updateProfile(student);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("deleteProfile/{email}")
    public ResponseEntity<?> deleteProfile(@PathVariable String email){
        return new ResponseEntity<>(service.deleteProfile(email),HttpStatus.NO_CONTENT);
    }

}
