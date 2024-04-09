package ems.example.ems.repositories;

import ems.example.ems.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminEmployeeRepository extends JpaRepository<Employee, Long> {
    // You can add custom methods here if needed
}
