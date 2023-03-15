package com.example.TMS.Department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepo extends JpaRepository<Department, Long> {
    public List<Department> findByNameDepartmentContaining(String name);
}
