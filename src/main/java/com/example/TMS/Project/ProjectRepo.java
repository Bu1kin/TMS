package com.example.TMS.Project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    public List<Project> findByNameProjectContaining(String name);
}
