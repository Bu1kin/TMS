package com.example.TMS.Post;

import com.example.TMS.User.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postName;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Collection<User> users;

    public Post(String postName){
        this.postName = postName;
    }

    public Post() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
