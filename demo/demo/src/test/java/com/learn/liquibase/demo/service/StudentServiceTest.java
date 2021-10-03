package com.learn.liquibase.demo.service;

import com.learn.liquibase.demo.exception.UserExistsException;
import com.learn.liquibase.demo.exception.UserNotFoundException;
import com.learn.liquibase.demo.model.student.Student;
import com.learn.liquibase.demo.repository.student.StudentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    private Student studentOne,studentTwo;
    private List<Student> studentList;
    private Optional optional;

    @Mock
    private StudentRepo repo;

    @InjectMocks
    private StudentServiceImpl studService;

    @BeforeEach
    public void setUp(){
        studentOne = new Student(1,"Ramesh","Male","ramesh23@gmail.com","23/03/1996","CS","No.8, 1stStreet, Vellore");
        studentTwo = new Student(2,"Swathi","Female","swathi10@gmail.com","10/02/1995","Bio","No.2, 5thStreet, Vellore");
    }

    @AfterEach
    public void tearDown(){
        studentOne = null;
        studentTwo = null;
    }

    @Test
    public void givenProfileToSaveThenShouldReturnSavedProfileName(){
        when(repo.save(any(Student.class))).thenReturn(studentOne);
        assertEquals(studentOne,studService.addProfile(studentOne));
        verify(repo,times(1)).save(any(Student.class));
    }

    @Test
    public void givenProfileToSaveAlreadyExistsThenShouldThrowException(){
        when(repo.save(any(Student.class))).thenThrow(UserExistsException.class);
        assertThrows(UserExistsException.class,()->{studService.addProfile(studentOne);});
        verify(repo,times(1)).save(any(Student.class));
    }

    @Test
    public void givenGetAllProfilesShouldReturnListOfAllProfiles(){
        repo.save(studentOne);
        repo.save(studentTwo);
        when(repo.findAll()).thenReturn(studentList);
        List<Student> students = studService.allProfiles();
        assertEquals(studentList,students);
        verify(repo,times(1)).save(studentOne);
        verify(repo,times(1)).findAll();
    }

    @Test
    public void givenStudentEmailToDeleteThenShouldReturnDeletedStudentName(){
        when(repo.existsByEmail(studentTwo.getEmail())).thenReturn(true);
        String s = studService.deleteProfile("swathi10@gmail.com");
        assertEquals(null,s);
        verify(repo,times(1)).existsByEmail(studentTwo.getEmail());
        verify(repo,times(1)).deleteByEmail(studentTwo.getEmail());
    }

    @Test
    public void givenEmployeeEmailToDeleteIfNotPresentThenShouldThrowException(){
        when(repo.existsByEmail(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,()->{studService.deleteProfile("suresh21@gmail.com");});
        verify(repo,times(1)).existsByEmail(anyString());
    }

    @Test
    public void givenProfileToUpdateThenShouldReturnUpdatedProfileName(){
        when(repo.findByEmail(studentOne.getEmail())).thenReturn(Optional.of(studentOne));
        when(repo.save(studentOne)).thenReturn(studentOne);
        studentOne.setName("Ramesh");
        Student student = studService.updateProfile(studentOne);
        assertEquals(student.getName(),"Ramesh");
        verify(repo,times(1)).save(studentOne);
        verify(repo,times(2)).findByEmail(studentOne.getEmail());
    }

    @Test
    public void givenProfileToUpdateIfNotPresentThenShouldThrowException(){
        when(repo.findByEmail(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,()->{studService.updateProfile(studentOne);});
        verify(repo,times(1)).findByEmail(studentOne.getEmail());
    }


}
