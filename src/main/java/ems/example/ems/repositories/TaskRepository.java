package ems.example.ems.repositories;

import ems.example.ems.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TaskRepository extends JpaRepository <Tasks,Long> {
    @Query(value = "select tb.task_id,tb.task_count,tb.task_name,e.employee_id from ems.tasks_table tb join employee_table e on tb.employee_id = e.employee_id where e.employee_id=?1",nativeQuery = true)
    List<Map<String,String>>viewTaskEmployee(String id);

    List<Tasks> findBytaskStatus(String taskstatus);
}