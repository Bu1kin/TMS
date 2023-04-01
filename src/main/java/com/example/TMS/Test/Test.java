package com.example.TMS.Test;

import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Task.Task;
import com.example.TMS.User.User;

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
    private String results;
    private String description;
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Project project;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    private Collection<Task> tasks;

    public Test(String nameTest, Set<Status> status, Double version, String results, String description, Project project) {
        this.nameTest = nameTest;
        this.status = status;
        this.version = version;
        this.results = results;
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

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
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
