package com.learn.liquibase.demo.service;

import com.learn.liquibase.demo.exception.EmptyEmailException;
import com.learn.liquibase.demo.exception.UserExistsException;
import com.learn.liquibase.demo.exception.UserNotFoundException;
import com.learn.liquibase.demo.model.student.Student;
import com.learn.liquibase.demo.repository.student.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private StudentRepo repo;

    @Autowired
    public StudentServiceImpl(StudentRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Student> allProfiles() {
        return repo.findAll();
    }

    @Override
    public Student addProfile(Student student) throws UserExistsException {
        if (student.getEmail()==null){
            throw new EmptyEmailException("Please Provide EmailID");
        }
        else {
            if(repo.existsByEmail(student.getEmail())){
                throw new UserExistsException("Profile Already exists With Email");
            }
            Student savedProfile = repo.save(student);
            return savedProfile;
        }
    }

    @Override
    public Student updateProfile(Student student) throws UserNotFoundException {
        Optional<Student> byEmail = repo.findByEmail(student.getEmail());
        if(byEmail.isPresent()){
            Student stud = repo.findByEmail(student.getEmail()).get();
            stud.setName(student.getName());
            stud.setEmail(student.getEmail());
            stud.setDob(student.getDob());
            stud.setGender(student.getGender());
            stud.setGroup(student.getGroup());
            Student save = repo.save(stud);
            return save;
        }
        else{
            throw new UserNotFoundException("Profile Not Found");
        }
    }

    @Override
    public String deleteProfile(String email) throws UserNotFoundException {
        if(repo.existsByEmail(email)){
            String name = repo.deleteByEmail(email);
            return name;
        }
        throw new UserNotFoundException("Profile Not Found");
    }
}
