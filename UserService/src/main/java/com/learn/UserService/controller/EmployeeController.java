package com.learn.UserService.controller;

import com.learn.UserService.dto.EmployeeDto;
import com.learn.UserService.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private final EmployeeService service;


    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping(value = "/get-all-profiles")
    public ResponseEntity<?> getAllEmployeeProfiles(){
        return new ResponseEntity<>(service.getAllEmployeeProfiles(), HttpStatus.OK);
    }

    @GetMapping(value = "get-profile/{email}")
    public ResponseEntity<?> getProfileByEmail(String email){
        return new ResponseEntity<>(service.getEmployeeByEmail(email),HttpStatus.OK);
    }

    @PostMapping(value = "/create-profile")
    public ResponseEntity<?> createEmployeeProfile(@RequestBody EmployeeDto employeeDto){
        return new ResponseEntity<>(service.createEmployeeProfile(employeeDto),HttpStatus.CREATED);
    }

    @PutMapping(value = "/update-profile")
    public ResponseEntity<?> updateEmployeeProfile(@RequestBody EmployeeDto employeeDto){
        return new ResponseEntity<>(service.updateEmployeeProfile(employeeDto),HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete-profile/{email}")
    public ResponseEntity<?> deleteEmployeeProfile(@PathVariable String email){
        return new ResponseEntity<>(service.deleteEmployeeProfile(email),HttpStatus.NO_CONTENT);
    }

}
