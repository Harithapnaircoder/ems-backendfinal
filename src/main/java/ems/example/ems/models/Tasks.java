package ems.example.ems.models;

import jakarta.persistence.*;


@Entity
@Table(name = "tasks_table")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long task_id;

    @Column(name = "task_name")
    private String task_name;

    @Column(name = "task_count")
    private long task_count;

    @Column(name = "Employee_id")
    private long employeeid;

    @Column(name = "task_status")
    private String taskStatus;

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public long getTask_count() {
        return task_count;
    }

    public void setTask_count(long task_count) {
        this.task_count = task_count;
    }

    public long getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(long employeeid) {
        this.employeeid = employeeid;
    }
}