package com.example.TMS.Post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<PostModel, Long> {
}
