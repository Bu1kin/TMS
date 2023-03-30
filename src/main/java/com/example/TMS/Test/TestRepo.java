package com.example.TMS.Test;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepo extends JpaRepository<Test, Long> {
    public List<Test> findByNameTestContaining(String name);
    public List<Test> findAllByProject_Id(Long id);
    public List<Test> findAll(Sort sort);
    public List<Test> findAllByProject_IdOrderByStatusAsc(Long id);
    public List<Test> findAllByProject_IdOrderByStatusDesc(Long id);
}
