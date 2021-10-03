package com.learn.liquibase.demo.service;

import com.learn.liquibase.demo.exception.EmptyEmailException;
import com.learn.liquibase.demo.exception.UserExistsException;
import com.learn.liquibase.demo.exception.UserNotFoundException;
import com.learn.liquibase.demo.model.employee.Employee;
import com.learn.liquibase.demo.repository.employee.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepo repo;

//    private RedisTemplate template;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo repo) {
        this.repo = repo;
    }

    @Override
    public Employee addProfile(Employee employee) throws UserExistsException {
        if(employee.getEmail()==null){
                throw new EmptyEmailException("Please Provide EmailID");
        }
        else {
            if(repo.existsByEmail(employee.getEmail())){
                throw new UserExistsException("Profile Already exists With Email");
            }
            Employee savedProfile = repo.save(employee);
            return savedProfile;
        }
    }

    @Override
    public Employee updateProfile(Employee employee) throws UserNotFoundException {
        Optional<Employee> byEmail = repo.findByEmail(employee.getEmail());
        if(byEmail.isPresent()){
            Employee employee1 = repo.findByEmail(employee.getEmail()).get();
            employee1.setName(employee.getName());
            employee1.setEmail(employee.getEmail());
            employee1.setDob(employee.getDob());
            employee1.setGender(employee.getGender());
            employee1.setRole(employee.getRole());
            employee1.setAddress(employee.getAddress());
            Employee save = repo.save(employee1);
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


    @Override
    public List<Employee> allProfiles() {

        return repo.findAll();
    }

    @Override
    public Employee getProfileByEmail(String email) {
        if (repo.existsByEmail(email)){
            return repo.findByEmail(email).get();
        }
        else {
          throw new UserNotFoundException("Profile Not Found");
        }
    }
}
