package com.example.firstApplication.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.firstApplication.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee,Long>{

}
