package ems.example.ems.repositories;
import ems.example.ems.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignupRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);
}