package com.example.TMS.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    public List<User> findBySurname(String name);
    public List<User> findAllByDepartment_Id(Long id);
    public List<User> findAllByPost_Id(Long id);
    public User findByLogin(String name);
    public User findByLoginAndActive(String login, Boolean active);
}
