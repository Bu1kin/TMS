package com.example.TMS.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    public List<Post> findByPostNameContaining(String name);
}
