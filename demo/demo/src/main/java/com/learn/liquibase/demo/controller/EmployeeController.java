package com.learn.liquibase.demo.controller;

import com.learn.liquibase.demo.model.employee.Employee;
import com.learn.liquibase.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {


    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("addProfile")
    public ResponseEntity<?> addProfile(@RequestBody Employee employee){
        return new ResponseEntity<>(service.addProfile(employee),HttpStatus.CREATED);
    }

    @GetMapping("getAllProfile")
    public ResponseEntity<?> getAllProfiles(){
        return new ResponseEntity<>(service.allProfiles(), HttpStatus.OK);
    }

    @GetMapping("getProfileByEmail/{email}")
    public ResponseEntity<?> getProfileByEmail(@PathVariable String email){
        return new ResponseEntity<>(service.getProfileByEmail(email),HttpStatus.OK);
    }

    @PutMapping("updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Employee employee){
        service.updateProfile(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("deleteProfile/{email}")
    public ResponseEntity<?> deleteProfile(@PathVariable String email){
        return new ResponseEntity<>(service.deleteProfile(email),HttpStatus.NO_CONTENT);
    }

}
