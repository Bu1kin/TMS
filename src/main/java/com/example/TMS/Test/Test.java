package com.example.TMS.Test;

import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Task.Task;
import com.example.TMS.User.User;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameTest;
    @ElementCollection(targetClass = Status.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "statusTest", joinColumns = @JoinColumn(name = "id_test"))
    @Enumerated(EnumType.STRING)
    private Set<Status> status;
    private Double version;
    private String description;
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Project project;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Collection<Task> tasks;

    public Test(String nameTest, Set<Status> status, Double version, String description, Project project) {
        this.nameTest = nameTest;
        this.status = status;
        this.version = version;
        this.description = description;
        this.project = project;
    }

    public Test() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public Set<Status> getStatus() {
        return status;
    }

    public void setStatus(Set<Status> status) {
        this.status = status;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Double getResults() {
        int successCounter = 0;
        int totalCounter = 0;
        double resultPercentage = 0;

        for(Task task1 : tasks) {
            if (task1.getStatus().toArray()[0] == Status.Passed) {
                successCounter++;
                totalCounter++;
            }
            else {
                totalCounter++;
            }
        }

        resultPercentage = (double) successCounter / (double) totalCounter * 100;
        resultPercentage = Precision.round(resultPercentage, 1);

        return resultPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
