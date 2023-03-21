package com.example.TMS.User;

import com.example.TMS.Department.Department;
import com.example.TMS.Department.DepartmentRepo;
import com.example.TMS.Post.Post;
import com.example.TMS.Post.PostRepo;
import com.example.TMS.Role.Roles;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @Autowired
//    private PasswordEncoder passwordEncoder;

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
    public String RegView(User user, Model model){
        Iterable<Post> posts = postRepo.findAll();
        Iterable<Department> departments = departmentRepo.findAll();

        model.addAttribute("posts", posts);
        model.addAttribute("departments", departments);
        return "/securing/registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return("/securing/registration");
        }
        if(userRepo.findByLogin(user.getLogin()) != null){

           model.addAttribute("error", "Такой пользователь уже существует!");
           return "/securing/registration";
        }

        //мб тут понадобится Post post1 = postRepo.findById(Long.valueOf(post.split(" ")[0])).orElseThrow();

        user.setRole(Collections.singleton(Roles.Пользователь));
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);

        userRepo.save(user);
        return "redirect:/login";
    }

    @PostMapping("/edit/{id}")
    public String userEdit(@PathVariable Long id,
                           @RequestParam String login,
                           @RequestParam String surname,
                           @RequestParam String name,
                           @RequestParam String middleName,
                           @RequestParam String post,
                           @RequestParam String department,
                           @RequestParam String[] roles,
                           BindingResult result) {
        User user = userRepo.findById(id).orElseThrow();
        Post post1 = postRepo.findById(Long.valueOf(post.split(" ")[0])).orElseThrow();
        Department department1 = departmentRepo.findById(Long.valueOf(department.split(" ")[0])).orElseThrow();

        user.setLogin(login);
        user.setSurname(surname);
        user.setName(name);
        user.setMiddleName(middleName);
        user.setPost(post1);
        user.setDepartment(department1);
        user.getRole().clear();

        for(String role: roles) {
            user.getRole().add(Roles.valueOf(role));
        }

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
