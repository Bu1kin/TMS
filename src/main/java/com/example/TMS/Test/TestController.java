package com.example.TMS.Test;

import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.Task.Task;
import com.example.TMS.Task.TaskRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestRepo testRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    TaskRepo taskRepo;

    @GetMapping("/all")
    public String viewTest(Model model) {
        Iterable<Test> tests = testRepo.findAll();
        Iterable<User> users = userRepo.findAll();
        Iterable<Project> projects = projectRepo.findAll();

        model.addAttribute("tests", tests);
        model.addAttribute("users", users);
        model.addAttribute("projects", projects);
        return "/Test/index";
    }

    @PostMapping("/add")
    public String testAdd(@RequestParam String nameTest,
                          @RequestParam String status,
                          @RequestParam String results,
                          @RequestParam String description,
                          @RequestParam String project,
                          Model model){
        Project project1 = projectRepo.findById(Long.valueOf(project.split(" ")[0])).orElseThrow();

        Test test = new Test(nameTest, status, 1.0, results, description, project1);
        testRepo.save(test);
        return ("redirect:/test/all");
    }

    @PostMapping("/edit/{id}")
    public String testEdit(Test test, BindingResult result) {
        if(result.hasErrors())
            return ("/Test/details");
        Test testTemp = testRepo.findById(test.getId()).orElseThrow();
        test.setVersion(1.0);
        test.setProject(testTemp.getProject());
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
        Iterable<User> users = userRepo.findAll();
        Iterable<Project> projects = projectRepo.findAll();

        model.addAttribute("test", test);
        model.addAttribute("users", users);
        model.addAttribute("projects", projects);
        return "/Test/details";
    }

    @GetMapping("/taskList/{id}")
    public String testTaskList(Model model, @PathVariable Long id) {
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_Id(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        return "/Test/taskList";
    }

    @PostMapping("/taskList/{id}")
    public String taskAdd(@PathVariable Long id,
                          @RequestParam String nameTask,
                          @RequestParam String priority,
                          @RequestParam String status,
                          @RequestParam String duration,
                          @RequestParam String description,
                          Model model){
        Test test = testRepo.findById(id).orElseThrow();

        Task task = new Task(nameTask, priority, status, duration, description, test);
        taskRepo.save(task);
        return ("redirect:/test/taskList/{id}");
    }

    @GetMapping("/sortTaskListByPriorityAsc/{id}")
    public String taskListSortAsc(@PathVariable Long id, Model model){
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_IdOrderByPriorityAsc(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        return "/Test/taskList";
    }

    @GetMapping("/sortTaskListByPriorityDesc/{id}")
    public String taskListSortDesc(@PathVariable Long id, Model model){
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_IdOrderByPriorityDesc(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        return "/Test/taskList";
    }

    @GetMapping("/filter-contains")
    public String testFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Test> tests = testRepo.findByNameTestContaining(searchName);
        model.addAttribute("tests", tests);
        return "/Test/filter";
    }

    @GetMapping("/sortStatusAsc")
    public String testSortAscContains(Model model){
        List<Test> tests = testRepo.findAll(Sort.by("status").ascending());
        model.addAttribute("tests", tests);
        return "/Test/index";
    }

    @GetMapping("/sortStatusDesc")
    public String testSortDescContains(Model model){
        List<Test> tests = testRepo.findAll(Sort.by("status").descending());
        model.addAttribute("tests", tests);
        return "/Test/index";
    }
}
