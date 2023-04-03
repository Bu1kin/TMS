package com.example.TMS.Task;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Test.Test;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameTask;
    @ElementCollection(targetClass = Priority.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "priority", joinColumns = @JoinColumn(name = "id_task"))
    @Enumerated(EnumType.STRING)
    private Set<Priority> priority;
    @ElementCollection(targetClass = Status.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "statusTask", joinColumns = @JoinColumn(name = "id_task"))
    @Enumerated(EnumType.STRING)
    private Set<Status> status;
    private String duration;
    private String description;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Test test;

    public Task(String nameTask, Set<Priority> priority, Set<Status> status, String duration, String description, Test test) {
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

    public Set<Priority> getPriority() {
        return priority;
    }

    public void setPriority(Set<Priority> priority) {
        this.priority = priority;
    }

    public Set<Status> getStatus() {
        return status;
    }

    public void setStatus(Set<Status> status) {
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
