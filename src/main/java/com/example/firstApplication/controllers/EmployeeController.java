package com.example.firstApplication.controllers;

import java.io.Console;
import java.lang.System.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.firstApplication.exception.ResourceNotFoundException;
import com.example.firstApplication.model.Employee;
import com.example.firstApplication.repos.EmployeeRepository;
import com.example.firstApplication.services.EmployeeService;


@RestController
@RequestMapping("api/v1/")
public class EmployeeController {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeRepository employeerepository;
	
	@Autowired
	private EmployeeService employeeservice;
	
	
	//get all employees
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees(){
		return ResponseEntity.ok(employeeservice.getAllEmployees());
	}
	
	//Create employee rest api
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/employees")
	public ResponseEntity<Employee> createEmployees(@Validated @RequestBody Employee employee){
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeservice.createEmployees(employee));
		}
	
	//get employee by id rest api
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee=employeeservice.getEmployeeById(id);
		return ResponseEntity.ok(employee);
	}
	
	//update employee rest api
	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(Long id,Employee employeeDetails){
		Employee updatedEmployee=employeeservice.updateEmployee(id, employeeDetails);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	//delete Employee rest api
	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String,Boolean>> deleteEmployee(@PathVariable Long id) {
		return ResponseEntity.ok(employeeservice.deleteEmployee(id));
	}

}
