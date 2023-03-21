package com.example.TMS.Task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    public List<Task> findByNameTaskContaining(String name);
    public List<Task> findAllByTest_Id(Long id);
}
