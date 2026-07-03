package com.Employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Employee.entity.Employee;

public interface EmployeeRepo
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}