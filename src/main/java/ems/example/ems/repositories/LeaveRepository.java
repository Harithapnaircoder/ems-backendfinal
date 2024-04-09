package ems.example.ems.repositories;

import ems.example.ems.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query("SELECT l.id,e.fullName AS Employee, l.leaveType, l.reason, l.startDate, l.endDate " +
            "FROM Leave l " +
            "INNER JOIN Employee e ON e.id = l.empId " +
            "WHERE l.approvedStatus IS NULL")
    List<Object[]> findPendingLeaveWithEmployeeName();

    @Query("SELECT l.id,e.fullName as Employee, l.leaveType, l.reason, l.startDate, l.endDate, " +
            "CASE WHEN l.approvedStatus = true THEN 'Approved' ELSE 'Rejected' END AS approvalStatus " +
            "FROM Leave l " +
            "INNER JOIN Employee e ON e.id = l.empId")
    List<Object[]> getAllLeaveRecords();

    @Query("SELECT l.id, l.leaveType, l.reason, l.startDate, l.endDate, " +
            "CASE WHEN l.approvedStatus IS NULL THEN 'Not Reviewed' " +
            "WHEN l.approvedStatus = true THEN 'Approved' ELSE 'Rejected' END AS approvalStatus " +
            "FROM Leave l " +
            "WHERE l.empId = :empId")
    List<Object[]> getLeaveRecordsByEmployeeId(@Param("empId") Long empId);
    List<Leave> findByEmpId(Long empId);
}
