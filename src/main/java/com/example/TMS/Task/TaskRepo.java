package com.example.TMS.Task;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    public List<Task> findByNameTaskContaining(String name);
    public List<Task> findAllByTest_Id(Long id);
    public List<Task> findAll(Sort sort);
    public List<Task> findAllByTest_IdOrderByPriorityAsc(Long id);
    public List<Task> findAllByTest_IdOrderByPriorityDesc(Long id);
}
