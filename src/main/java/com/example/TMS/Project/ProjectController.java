package com.example.TMS.Project;

import com.example.TMS.Enums.Status;
import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    TestRepo testRepo;

    @GetMapping("/all")
    public String viewProject(Model model) {
        Iterable<Project> projects = projectRepo.findAll();

        model.addAttribute("projects", projects);
        model.addAttribute("statuses", Status.values());
        return "/Project/index";
    }

    @PostMapping("/add")
    public String projectAdd(@RequestParam String nameProject,
                             @RequestParam String description,
                             Model model){
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);

        Project project = new Project(nameProject, description, date);
        projectRepo.save(project);
        return ("redirect:/project/all");
    }

    @PostMapping("/edit/{id}")
    public String projectEdit(Project project, BindingResult result) {
        if(result.hasErrors())
            return ("/Project/details");
        Project projectTemp = projectRepo.findById(project.getId()).orElseThrow();
        project.setDateCreation(projectTemp.getDateCreation());
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

    @GetMapping("/testList/{id}")
    public String projectTestList(Model model, @PathVariable Long id) {
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_Id(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        model.addAttribute("status", Status.values());
        return "/Project/testList";
    }

    @PostMapping("/testList/{id}")
    public String testAdd(@PathVariable Long id,
                          @RequestParam String nameTest,
                          @RequestParam Set<Status> status,
                          @RequestParam String results,
                          @RequestParam String description,
                          Model model){
        Project project = projectRepo.findById(id).orElseThrow();

        Test test = new Test(nameTest, status, 1.0, results, description, project);

        testRepo.save(test);
        return ("redirect:/project/testList/{id}");
    }

    @GetMapping("/sortTestListByStatusAsc/{id}")
    public String testListSortAsc(@PathVariable Long id, Model model){
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_IdOrderByStatusAsc(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        return "/Project/testList";
    }

    @GetMapping("/sortTestListByStatusDesc/{id}")
    public String testListSortDesc(@PathVariable Long id, Model model){
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_IdOrderByStatusDesc(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        return "/Project/testList";
    }
}
