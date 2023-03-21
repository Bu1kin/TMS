package com.example.TMS.Project;

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

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    TestRepo testRepo;

    @GetMapping("/all")
    public String viewProject(Model model) {
        Iterable<Project> projects = projectRepo.findAll();
        Iterable<User> users = userRepo.findAll();

        model.addAttribute("projects", projects);
        model.addAttribute("users", users);
        return "/Project/index";
    }

    @PostMapping("/add")
    public String projectAdd(@RequestParam String nameProject,
                             @RequestParam String description,
//                             @RequestParam String user,
                             Model model){
        //User user1 = userRepo.findById(Long.valueOf(user.split(" ")[0])).orElseThrow();
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
        //а тут надо будет менять?
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
        Iterable<User> users = userRepo.findAll();

        model.addAttribute("project", project);
        model.addAttribute("users", users);

        return "/Project/details";
    }

    @GetMapping("/testList/{id}")
    public String projectTestList(Model model, @PathVariable Long id) {
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_Id(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        return "/Project/testList";
    }

    @PostMapping("/testList/{id}")
    public String testAdd(@PathVariable Long id,
                          @RequestParam String nameTest,
                          @RequestParam String status,
                          @RequestParam String results,
                          @RequestParam String description,
                          Model model){
        Project project = projectRepo.findById(id).orElseThrow();

        Test test = new Test(nameTest, status, 1.0, results, description, project);
        //Проект и Версию убрать из конструктора
//        test.setProject(project);
//        test.setVersion(1.0);
        testRepo.save(test);
        return ("redirect:/project/testList/{id}");
    }

    @GetMapping("/filter-contains")
    public String projectFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Project> projectList = projectRepo.findByNameProjectContaining(searchName);
        model.addAttribute("projectList", projectList);
        return "/Project/filter";
    }
}
