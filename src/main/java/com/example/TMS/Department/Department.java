package com.example.TMS.Department;

import com.example.TMS.User.User;
import javax.persistence.*;
import java.util.Collection;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameDepartment;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Collection<User> users;

    public Department(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }

    public Department() {

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNameDepartment() {
        return nameDepartment;
    }
    public void setNameDepartment(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }
    public Collection<User> getUsers() {
        return users;
    }
    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
