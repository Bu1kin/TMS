package com.example.TMS.Test;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.Task.Task;
import com.example.TMS.Task.TaskRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

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
    public String viewTest(Principal principal, Model model) {
        Iterable<Test> tests = testRepo.findAll();
        Iterable<Project> projects = projectRepo.findAll();

        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("tests", tests);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("projects", projects);
        model.addAttribute("status", Status.values());
        return "/Test/index";
    }

    @PostMapping("/add")
    public String testAdd(@RequestParam String nameTest,
                          @RequestParam Set<Status> status,
                          @RequestParam Double results,
                          @RequestParam String description,
                          @RequestParam String project,
                          Model model){
        Project project1 = projectRepo.findById(Long.valueOf(project.split(" ")[0])).orElseThrow();

        Test test = new Test(nameTest, status, 1.0, description, project1);
        testRepo.save(test);
        return ("redirect:/test/all");
    }

    @PostMapping("/edit/{id}")
    public String testEdit(Test test, @RequestParam String[] status, BindingResult result) {
        if(result.hasErrors())
            return ("/Test/details");
        Test testTemp = testRepo.findById(test.getId()).orElseThrow();
        test.setVersion(1.0);
        test.setProject(testTemp.getProject());
        test.getStatus().clear();

        for(String statusTemp: status) {
            test.getStatus().add(Status.valueOf(statusTemp));
        }

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
        model.addAttribute("status", Status.values());
        return "/Test/details";
    }

    @GetMapping("/taskList/{id}")
    public String testTaskList(Model model, @PathVariable Long id) {
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_Id(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Test/taskList";
    }

    @PostMapping("/taskList/{id}")
    public String taskAdd(@PathVariable Long id,
                          @RequestParam String nameTask,
                          @RequestParam Set<Priority> priority,
                          @RequestParam Set<Status> status,
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
                                         Principal principal,
                                         Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> searchedDepartmentTests = testRepo.findByNameTestContainingAndProjectUserDepartmentId(searchName, user.getDepartment().getId());
        Iterable<Project> projects = projectRepo.findAll();

        model.addAttribute("searchedDepartmentTests", searchedDepartmentTests);
        model.addAttribute("projects", projects);
        model.addAttribute("status", Status.values());
        return "/Test/filter";
    }

    @GetMapping("/sortStatusAsc")
    public String testSortAscContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(Sort.by("status").ascending(), user.getDepartment().getId());

        model.addAttribute("departmentTests", departmentTests);
        return "/Test/index";
    }

    @GetMapping("/sortStatusDesc")
    public String testSortDescContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(Sort.by("status").descending(), user.getDepartment().getId());

        model.addAttribute("departmentTests", departmentTests);
        return "/Test/index";
    }
}
