package com.example.TMS.Task;

import com.example.TMS.Test.Test;

import javax.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameTask;
    private String priority;
    private String status;
    private String duration;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private Test test;

    public Task(String nameTask, String priority, String status, String duration, String description, Test test) {
        this.nameTask = nameTask;
        this.priority = priority;
        this.status = status;
        this.duration = duration;
        this.description = description;
        this.test = test;
    }

    public Task() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
