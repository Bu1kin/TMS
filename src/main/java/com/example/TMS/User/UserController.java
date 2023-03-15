package com.example.TMS.User;

import com.example.TMS.Department.DepartmentRepo;
import com.example.TMS.Post.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/all")
    public String viewUser(Model model) {
        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "Admin/Users/index";
    }

    @PostMapping("/edit/{id}")
    public String userEdit(User user, BindingResult result) {
        if(result.hasErrors())
            return ("Admin/Users/details");
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
        model.addAttribute("user", user);
        return "Admin/Users/details";
    }

    @GetMapping("/filter-contains")
    public String userFilterContains(@RequestParam String searchName,
                                         Model model){
        List<User> users = userRepo.findBySurname(searchName);
        model.addAttribute("users", users);
        return "Admin/Users/filter";
    }
}
