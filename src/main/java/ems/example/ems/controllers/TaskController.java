package ems.example.ems.controllers;

import ems.example.ems.models.Employee;
import ems.example.ems.models.Tasks;
import ems.example.ems.repositories.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @PostMapping("/tasksave")
    public String tasks(@RequestBody Tasks tasks){
        tasks.setTaskStatus("Pending");
        taskRepository.save(tasks);
        return "task assigned successfull";
    }

    @PostMapping("/taskemployee")
    public ResponseEntity<List<Map<String,String>>>viewTaskEmployee(@RequestBody Employee employee){
        System.out.println(employee.getId().toString());
        List<Map<String,String>> data = taskRepository.viewTaskEmployee(employee.getId().toString());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/submit/{task_id}")
    public Tasks submitTask(@PathVariable Long task_id) {
        Tasks task = taskRepository.findById(task_id).orElse(null);
        if (task != null) {
            task.setTaskStatus("Submitted");
            return taskRepository.save(task);
        }
        return null; // Handle error case
    }

    @PostMapping("/pending/{task_id}")
    public Tasks markTaskPending(@PathVariable Long task_id) {
        Tasks task = taskRepository.findById(task_id).orElse(null);
        if (task != null) {
            task.setTaskStatus("Pending");
            return taskRepository.save(task);
        }
        return null; // Handle error case
    }
    @GetMapping("/submitted")
    public ResponseEntity<List<Tasks>> getSubmittedTasks() {
        List<Tasks> submittedTasks = taskRepository.findBytaskStatus("Submitted");
        return ResponseEntity.ok(submittedTasks);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Tasks>> getPendingTasks() {
        List<Tasks> pendingTasks = taskRepository.findBytaskStatus("Pending");
        return ResponseEntity.ok(pendingTasks);
    }

}