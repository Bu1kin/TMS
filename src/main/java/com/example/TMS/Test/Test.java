package com.example.TMS.Test;

import com.example.TMS.Project.Project;
import com.example.TMS.Task.Task;
import com.example.TMS.User.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameTest;
    private String status;
    private Double version;
    private Date dateStart;
    private Date dateEnd;
    private String results;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Project project;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    private Collection<Task> tasks;

    public Test(String nameTest, String status, Double version, Date dateStart, Date dateEnd, String results, String description, User user, Project project) {
        this.nameTest = nameTest;
        this.status = status;
        this.version = version;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.results = results;
        this.description = description;
        this.user = user;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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
