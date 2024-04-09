package ems.example.ems.controllers;

import ems.example.ems.repositories.SignupRepository;
import ems.example.ems.models.Employee; // Changed from Signup to Employee
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Employee employee) { // Changed from Signup to Employee
        // Check if the email already exists in the database
        Employee existingUser = signupRepository.findByEmail(employee.getEmail()); // Changed from Signup to Employee

        if (existingUser != null) {
            // If the email already exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email address already in use");
        } else {
            // Email does not exist, proceed with signup
            signupRepository.save(employee); // Changed from Signup to Employee
            return ResponseEntity.ok("Signup successful");
        }
    }
}