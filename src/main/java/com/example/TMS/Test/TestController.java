package com.example.TMS.Test;

import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestRepo testRepo;
    //ДОДЕЛАТЬ СВЯЗИ В CRUD
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProjectRepo projectRepo;
    @GetMapping("/all")
    public String viewTest(Model model) {
        Iterable<Test> tests = testRepo.findAll();
        model.addAttribute("tests", tests);
        return "/test/index";
    }

    @PostMapping("/add")
    public String testAdd(Test test, Model model){
        testRepo.save(test);
        return ("redirect:/test/all");
    }

    @PostMapping("/edit/{id}")
    public String testEdit(Test test, BindingResult result) {
        if(result.hasErrors())
            return ("/test/edit");
        testRepo.save(test);
        return("redirect:/test/all");
    }

    @GetMapping("/del/{id}")
    public String testDelete(@PathVariable Long id) {
        testRepo.deleteById(id);
        return("redirect:/test/all");
    }

    @GetMapping("/details/{id}")
    public String testDetails(Model model, @PathVariable Long id) {
        Test test = testRepo.findById(id).orElseThrow();
        model.addAttribute("test", test);
        return "/test/details";
    }

    @GetMapping("/filter-contains")
    public String testFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Test> tests = testRepo.findByNameTestContaining(searchName);
        model.addAttribute("tests", tests);
        return "/test/filter";
    }
}
