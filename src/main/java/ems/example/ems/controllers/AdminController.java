package ems.example.ems.controllers;

import ems.example.ems.models.Employee;
import ems.example.ems.repositories.AdminEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminEmployeeRepository adminEmployeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return adminEmployeeRepository.findAll();
    }
}
