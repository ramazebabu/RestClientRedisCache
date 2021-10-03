package com.learn.liquibase.demo.repository.student;

import com.learn.liquibase.demo.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface StudentRepo extends JpaRepository<Student,Integer> {
    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);
    @Transactional
    @Modifying
    String deleteByEmail(String email);
}
