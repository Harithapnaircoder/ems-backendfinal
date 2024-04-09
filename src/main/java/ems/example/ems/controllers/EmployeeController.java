package ems.example.ems.controllers;

import ems.example.ems.models.Employee;
import ems.example.ems.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees/{employeeId}")
    public Optional<Employee> getEmployeeById(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId);
    }
}
