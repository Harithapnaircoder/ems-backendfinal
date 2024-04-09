package ems.example.ems.repositories;

import ems.example.ems.models.Feedback;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>
{

    @Query("SELECT f.id, e.fullName AS Employee, f.feedback " +
            "FROM Feedback f " +
            "INNER JOIN Employee e ON f.empId = e.id " +
            "WHERE f.reply IS NULL")
    List<Object[]> findFeedbackWithEmployeeName();

//    @Query("SELECT f.id, f.feedback, f.reply FROM Feedback f WHERE f.employee.id = :empId AND f.reply IS NOT NULL")
//    List<Object[]> findFeedbackWithReply(@Param("empId") Long empId);
    List<Feedback> findByEmpId(Long empId);

    @Transactional
    @Modifying
    @Query("UPDATE Feedback f SET f.reply = :reply WHERE f.id = :feedbackId")
    void updateReply(@Param("feedbackId") Long feedbackId, @Param("reply") String reply);

}
