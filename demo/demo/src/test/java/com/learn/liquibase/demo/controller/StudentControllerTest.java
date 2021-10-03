package com.learn.liquibase.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.liquibase.demo.exception.UserExistsException;
import com.learn.liquibase.demo.model.student.Student;
import com.learn.liquibase.demo.service.StudentServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {

    public static final String GET_AND_PUT_URL = "/api/v1/student";
    private Student student;
    private List<Student> studentList;

    @MockBean
    private StudentServiceImpl studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        student = new Student();
        student.setId(1);
        student.setName("Ramesh");
        student.setDob("23/03/1996");
        student.setEmail("ramesh23@gmail.com");
        student.setGender("male");
        student.setGroup("CS");
        student.setAddress("No.8, 1stStreet, Vellore");
        studentList = new ArrayList<>();
        studentList.add(student);
    }

    @AfterEach
    public void tearDown(){
        student = null;
    }

    @Test
    public void givenProfileToSaveThenShouldReturnSavedProfileName() throws Exception {
        when(studentService.addProfile(any(Student.class))).thenReturn(student);
        mockMvc.perform(post(GET_AND_PUT_URL+"/addProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(student)))
                .andDo(MockMvcResultHandlers.print());
        verify(studentService).addProfile(any(Student.class));
    }

    @Test
    public void givenGetAllProfilesShouldReturnListOfAllProfiles() throws Exception {
        when(studentService.allProfiles()).thenReturn(studentList);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_AND_PUT_URL+"/getAllProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentList)))
                .andDo(MockMvcResultHandlers.print());
        verify(studentService).allProfiles();
        verify(studentService,times(1)).allProfiles();
    }

    @Test
    public void givenStudentEmailToDeleteThenShouldReturnDeletedStudentName() throws Exception {
        when(studentService.deleteProfile(student.getEmail())).thenReturn(student.getName());
        mockMvc.perform(delete(GET_AND_PUT_URL+"/deleteProfile/ramesh23@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(content().string(student.getName()))
                .andDo(MockMvcResultHandlers.print());
        verify(studentService).deleteProfile(student.getEmail());
    }

    @Test
    public void givenProfileToUpdateThenShouldReturnUpdatedProfileName() throws Exception {
        when(studentService.updateProfile(any())).thenReturn(student);
        mockMvc.perform(put(GET_AND_PUT_URL+"/updateProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }


}
