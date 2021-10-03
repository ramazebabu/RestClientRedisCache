package com.learn.liquibase.demo.repository.employee;

import com.learn.liquibase.demo.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
    @Transactional
    @Modifying
    String deleteByEmail(String email);
}
