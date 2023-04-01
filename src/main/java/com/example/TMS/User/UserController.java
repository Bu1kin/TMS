package com.example.TMS.User;

import com.example.TMS.Department.Department;
import com.example.TMS.Department.DepartmentRepo;
import com.example.TMS.Post.Post;
import com.example.TMS.Post.PostRepo;
import com.example.TMS.Role.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public String viewUser(Model model) {
        Iterable<User> users = userRepo.findAll();
        Iterable<Post> posts = postRepo.findAll();
        Iterable<Department> departments = departmentRepo.findAll();

        model.addAttribute("users", users);
        model.addAttribute("posts", posts);
        model.addAttribute("departments", departments);
        return "/Admin/Users/index";
    }

    @GetMapping("/registration")
    public String RegView(userDTO userDTO, Model model){
        Iterable<Post> posts = postRepo.findAll();
        Iterable<Department> departments = departmentRepo.findAll();

        model.addAttribute("posts", posts);
        model.addAttribute("departments", departments);
        return "/securing/registration";
    }

    @PostMapping("/registration")
    public String addUser(userDTO userDTO, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return("/securing/registration");
        }
        if(userRepo.findByLogin(userDTO.getLogin()) != null){
           model.addAttribute("error", "Такой пользователь уже существует!");
           return "/securing/registration";
        }

        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setSurname(userDTO.getSurname());
        user.setName(userDTO.getName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setDepartment(departmentRepo.findById(userDTO.getDepartment()).orElseThrow());
        user.setPost(postRepo.findById(userDTO.getPost()).orElseThrow());

        user.setRole(Collections.singleton(Roles.Пользователь));
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setActive(true);

        userRepo.save(user);
        return "redirect:/login";
    }

    @PostMapping("/edit/{id}")
    public String userEdit(@PathVariable Long id,
                           userDTO userDTO,
                           BindingResult result,
                           Model model) {
        User user = userRepo.findById(id).orElseThrow();

        user.setLogin(userDTO.getLogin());
        user.setSurname(userDTO.getSurname());
        user.setName(userDTO.getName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setDepartment(departmentRepo.findById(userDTO.getDepartment()).orElseThrow());
        user.setPost(postRepo.findById(userDTO.getPost()).orElseThrow());

        if(result.hasErrors())
            return ("/Admin/Users/details");
        userRepo.save(user);
        return("redirect:/user/all");
    }

    @GetMapping("/del/{id}")
    public String userDelete(@PathVariable Long id) {
        userRepo.deleteById(id);
        return("redirect:/user/all");
    }

    @GetMapping("/details/{id}")
    public String userDetails(Model model, @PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        Iterable<Post> posts = postRepo.findAll();
        Iterable<Department> departments = departmentRepo.findAll();

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", Roles.values());
        return "/Admin/Users/details";
    }

    @GetMapping("/filter-contains")
    public String userFilterContains(@RequestParam String searchName,
                                         Model model){
        List<User> users = userRepo.findBySurname(searchName);
        model.addAttribute("users", users);
        return "/Admin/Users/filter";
    }
}
