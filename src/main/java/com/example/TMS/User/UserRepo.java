package com.example.TMS.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    public List<User> findBySurname(String name);
    public User findByLogin(String name);
}
