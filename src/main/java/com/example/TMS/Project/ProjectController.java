package com.example.TMS.Project;

import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectRepo projectRepo;
//    ДОДЕЛАТЬ СВЯЗИ В CRUD
    @Autowired
    UserRepo userRepo;

    @GetMapping("/all")
    public String viewProject(Model model) {
        Iterable<Project> projects = projectRepo.findAll();
        model.addAttribute("projects", projects);
        return "/Project/index";
    }

    @PostMapping("/add")
    public String projectAdd(Project project, Model model){
        projectRepo.save(project);
        return ("redirect:/project/all");
    }

    @PostMapping("/edit/{id}")
    public String projectEdit(Project project, BindingResult result) {
        if(result.hasErrors())
            return ("/Project/edit");
        projectRepo.save(project);
        return("redirect:/project/all");
    }

    @GetMapping("/del/{id}")
    public String projectDelete(@PathVariable Long id) {
        projectRepo.deleteById(id);
        return("redirect:/project/all");
    }

    @GetMapping("/details/{id}")
    public String projectDetails(Model model, @PathVariable Long id) {
        Project project = projectRepo.findById(id).orElseThrow();
        model.addAttribute("project", project);
        return "/Project/details";
    }

    @GetMapping("/filter-contains")
    public String projectFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Project> projectList = projectRepo.findByNameProjectContaining(searchName);
        model.addAttribute("projectList", projectList);
        return "/Project/filter";
    }
}
