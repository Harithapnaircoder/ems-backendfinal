package ems.example.ems.repositories;

import ems.example.ems.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // No need to define findById method here
}
