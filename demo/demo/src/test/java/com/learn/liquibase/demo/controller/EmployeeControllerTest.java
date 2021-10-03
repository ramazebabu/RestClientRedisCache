package com.learn.liquibase.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.liquibase.demo.model.employee.Employee;
import com.learn.liquibase.demo.model.student.Student;
import com.learn.liquibase.demo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    public static final String GET_AND_PUT_URL = "/api/v1/employee";
    private Employee employee;
    private List<Employee> employeeList;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        employee = new Employee();
        employee.setId(1);
        employee.setName("Ramesh");
        employee.setDob("23/03/1996");
        employee.setEmail("ramesh23@gmail.com");
        employee.setGender("male");
        employee.setRole("BackendDev");
        employee.setAddress("No.8, 1stStreet, Vellore");
        employeeList = new ArrayList<>();
        employeeList.add(employee);
    }

    @AfterEach
    public void tearDown(){
        employee = null;
    }

    @Test
    public void givenProfileToSaveThenShouldReturnSavedProfileName() throws Exception {
        when(employeeService.addProfile(any(Employee.class))).thenReturn(employee);
        mockMvc.perform(post(GET_AND_PUT_URL+"/addProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(employee)))
                .andDo(MockMvcResultHandlers.print());
        verify(employeeService).addProfile(any(Employee.class));
    }

    @Test
    public void givenGetAllProfilesShouldReturnListOfAllProfiles() throws Exception {
        when(employeeService.allProfiles()).thenReturn(employeeList);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_AND_PUT_URL+"/getAllProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeList)))
                .andDo(MockMvcResultHandlers.print());
        verify(employeeService).allProfiles();
        verify(employeeService,times(1)).allProfiles();
    }

    @Test
    public void givenStudentEmailToDeleteThenShouldReturnDeletedEmployeeName() throws Exception {
        when(employeeService.deleteProfile(employee.getEmail())).thenReturn(employee.getName());
        mockMvc.perform(delete(GET_AND_PUT_URL+"/deleteProfile/ramesh23@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(content().string(employee.getName()))
                .andDo(MockMvcResultHandlers.print());
        verify(employeeService).deleteProfile(employee.getEmail());
    }

    @Test
    public void givenProfileToUpdateThenShouldReturnUpdatedProfileName() throws Exception {
        when(employeeService.updateProfile(any())).thenReturn(employee);
        mockMvc.perform(put(GET_AND_PUT_URL+"/updateProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

}
