package ems.example.ems.controllers;
import ems.example.ems.models.Employee;
import ems.example.ems.repositories.SignupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SigninController {

    @Autowired
    private SignupRepository signupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> signin(@RequestBody Employee user) {
        Map<String, Object> response = new HashMap<>();

        // Check if the user is an admin
        Query adminQuery = entityManager.createNativeQuery("SELECT * FROM admin WHERE email = ?1 AND password = ?2");
        adminQuery.setParameter(1, user.getEmail());
        adminQuery.setParameter(2, user.getPassword());

        List<Object[]> adminResults = adminQuery.getResultList();

        if (!adminResults.isEmpty()) {
            // Admin exists in admin table
            response.put("message", "Signin successful for admin");

            return ResponseEntity.ok(response);
        }


        Employee existingUser = signupRepository.findByEmail(user.getEmail());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {

            response.put("message", "Signin successful for staff");
            response.put("userId", existingUser.getId());
            return ResponseEntity.ok(response);
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}