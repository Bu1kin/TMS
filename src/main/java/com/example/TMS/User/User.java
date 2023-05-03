package com.example.TMS.User;

import com.example.TMS.Department.Department;
import com.example.TMS.Post.Post;
import com.example.TMS.Project.Project;
import com.example.TMS.Role.Roles;
import com.example.TMS.Test.Test;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "User_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private Boolean active;
    private String surname;
    private String name;
    private String middleName;
    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;
    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "id_user"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Project> projects;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Test> tests;

    public User(String login, String password, Boolean active, String surname, String name, String middleName, Post post, Department department, Set<Roles> role){
        this.login = login;
        this.password = password;
        this.active = active;
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        this.post = post;
        this.department = department;
        this.role = role;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    public Collection<Test> getTests() {
        return tests;
    }

    public void setTests(Collection<Test> tests) {
        this.tests = tests;
    }

    public Set<Roles> getRole() {
        return role;
    }

    public void setRole(Set<Roles> role) {
        this.role = role;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
