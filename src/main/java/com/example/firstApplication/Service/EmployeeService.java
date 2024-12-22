package com.example.firstApplication.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.firstApplication.Exception.ResourceNotFoundException;
import com.example.firstApplication.Repository.EmployeeRepository;
import com.example.firstApplication.model.Employee;



@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeerepository;
	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	public EmployeeService(EmployeeRepository employeerepository) {
		this.employeerepository=employeerepository;
	}
	
	public List<Employee> getAllEmployees(){
		Future<List<Employee>> future = executor.submit(() -> employeerepository.findAll());
        try {
            List<Employee> employees = future.get(); // Get the result from the future
            System.out.println("Employees: " + (employees.isEmpty() ? "No employees found" : employees.get(0).toString()));
            return employees;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching all employees");
        }
	}
	
	//Create employee rest api
	@CachePut(value="users",key="#employee.id")
	public Employee createEmployees(Employee employee){
		Future<Employee> future = executor.submit(() -> employeerepository.save(employee));
        try {
            return future.get(); // Get the result from the future
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating employee");
        }
	}
	
	//get employee by id rest api
	@Cacheable(value="users",key="#id")
	public Employee getEmployeeById(Long id) {
		Employee employee=employeerepository.findById(id).
				orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with id:"+id));
		return employee;
	}
	
	//update employee
	public Employee updateEmployee(Long id,Employee employeeDetails){
		Future<Employee> future = executor.submit(() -> {
		Employee employee=employeerepository.findById(id).
				orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with id:"+id));
		employee.setFirst_name(employeeDetails.getFirst_name());
		employee.setLast_name(employeeDetails.getLast_name());
		employee.setEmailid(employeeDetails.getEmailid());
		Employee updatedEmployee=employeerepository.save(employee);
		return updatedEmployee;});
		try {
            return future.get(); // Get the result from the future
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating employee");
        }
	}
	
	//delete Employee rest api
	@CacheEvict(value="users",key="#id")
	public Map<String,Boolean> deleteEmployee(Long id) {
		
		Future<Map<String, Boolean>> future = executor.submit(() -> {
		Employee employee=employeerepository.findById(id).
				orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with id:"+id));
		employeerepository.delete(employee);
		Map<String,Boolean> response = new HashMap<>();
		response.put("deleted",Boolean.TRUE);
		return response;});
				try {
		            return future.get(); // Get the result from the future
		        } catch (Exception e) {
		            e.printStackTrace();
		            throw new RuntimeException("Error deleting employee");
		        }		
	}
	
	public void shutdown() {
        executor.shutdown();
	}
}
