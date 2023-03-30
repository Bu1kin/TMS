package com.example.TMS.User;

import com.example.TMS.Department.Department;
import com.example.TMS.Post.Post;
import com.example.TMS.Project.Project;
import com.example.TMS.Role.Roles;
import com.example.TMS.Test.Test;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

public class userDTO {
    private String login;
    private String password;
    private Boolean active;
    private String surname;
    private String name;
    private String middleName;
    private Long post;
    private Long department;

    public userDTO() {

    }

    public userDTO(String login, String password, Boolean active, String surname, String name, String middleName, Long post, Long department) {
        this.login = login;
        this.password = password;
        this.active = active;
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        this.post = post;
        this.department = department;
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

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }
}
