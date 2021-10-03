package com.learn.UserService.service;

import com.learn.UserService.dto.EmployeeDto;
import com.learn.UserService.service.rest.client.EmployeeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeClient employeeClient;


    public EmployeeService(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    public List<EmployeeDto> getAllEmployeeProfiles(){
        return employeeClient.getAllEmployeeProfiles();
    }

    public EmployeeDto getEmployeeByEmail(String email){
        return employeeClient.getProfileByEmail(email);
    }

    public EmployeeDto createEmployeeProfile(EmployeeDto employeeDto){
        return employeeClient.createEmployeeProfile(employeeDto);
    }

    public EmployeeDto updateEmployeeProfile(EmployeeDto employeeDto){
        return employeeClient.updateEmployeeProfile(employeeDto);
    }

    public String deleteEmployeeProfile(String email){
        return employeeClient.deleteEmployeeProfile(email);
    }

}
