package com.learn.liquibase.demo.service;

import com.learn.liquibase.demo.exception.UserExistsException;
import com.learn.liquibase.demo.exception.UserNotFoundException;
import com.learn.liquibase.demo.model.employee.Employee;
import com.learn.liquibase.demo.repository.employee.EmployeeRepo;
import com.learn.liquibase.demo.service.EmployeeServiceImpl;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    private Employee employeeOne,employeeTwo;
    private List<Employee> empList;
    private Optional optional;

    @Mock
    private EmployeeRepo repo;

    @InjectMocks
    private EmployeeServiceImpl empService;

    @BeforeEach
    public void setUp(){
        employeeOne = new Employee(1, "Ramesh", "Male", "23/03/1996", "BackendDev", "ramesh23@gmail.com", "No.8,1st Street");
        employeeTwo = new Employee(2, "Swathi", "Female", "10/02/1996", "FrontendDev", "swathi10@gmail.com", "No.10,5th Street");
    }

    @AfterEach
    public void tearDown(){
        employeeOne = null;
        employeeTwo = null;
    }

    @Test
    public void givenProfileToSaveThenShouldReturnSavedProfileName(){
        when(repo.save(any(Employee.class))).thenReturn(employeeOne);
        assertEquals(employeeOne, empService.addProfile(employeeOne));
        verify(repo,times(1)).save(any(Employee.class));
    }

    @Test
    public void givenProfileToSaveAlreadyExistsThenShouldThrowException(){
        when(repo.save(any(Employee.class))).thenThrow(UserExistsException.class);
        assertThrows(UserExistsException.class, ()->{ empService.addProfile(employeeOne); });
        verify(repo,times(1)).save(any(Employee.class));
    }

    @Test
    public void givenGetAllProfilesShouldReturnListOfAllProfiles(){
        repo.save(employeeOne);
        repo.save(employeeTwo);
        when(repo.findAll()).thenReturn(empList);
        List<Employee> employees = empService.allProfiles();
        assertEquals(empList,employees);
        verify(repo, times(1)).save(employeeOne);
        verify(repo,times(1)).findAll();
    }

    @Test
    public void givenEmployeeEmailToDeleteThenShouldReturnDeletedEmployeeName(){
        when(repo.existsByEmail(employeeTwo.getEmail())).thenReturn(true);
        String s = empService.deleteProfile("swathi10@gmail.com");
        assertEquals(null,s);
        verify(repo,times(1)).existsByEmail(employeeTwo.getEmail());
        verify(repo,times(1)).deleteByEmail(employeeTwo.getEmail());
    }

    @Test
    public void givenEmployeeEmailToDeleteIfNotPresentThenShouldThrowException(){
        when(repo.existsByEmail(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,()->{empService.deleteProfile("suresh21@gmail.com");});
        verify(repo,times(1)).existsByEmail(anyString());
    }

    @Test
    public void givenProfileToUpdateThenShouldReturnUpdatedProfileName(){
        when(repo.findByEmail(employeeOne.getEmail())).thenReturn(Optional.of(employeeOne));
        when(repo.save(employeeOne)).thenReturn(employeeOne);
        employeeOne.setName("Ramesh");
        Employee employee = empService.updateProfile(employeeOne);
        assertEquals(employee.getName(),"Ramesh");
        verify(repo,times(1)).save(employeeOne);
        verify(repo,times(2)).findByEmail(employeeOne.getEmail());

    }

    @Test
    public void givenProfileToUpdateIfNotPresentThenShouldThrowException(){
        when(repo.findByEmail(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,()->{empService.updateProfile(employeeOne);});
        verify(repo,times(1)).findByEmail(employeeOne.getEmail());
    }


}
