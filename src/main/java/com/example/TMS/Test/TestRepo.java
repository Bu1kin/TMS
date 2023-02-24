package com.example.TMS.Test;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepo extends JpaRepository<TestModel, Long> {
}
