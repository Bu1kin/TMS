package com.example.TMS.Task;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    TestRepo testRepo;

    @GetMapping("/all")
    public String viewTask(Model model) {
        Iterable<Task> tasks = taskRepo.findAll();
        Iterable<Test> tests = testRepo.findAll();

        model.addAttribute("tasks", tasks);
        model.addAttribute("tests", tests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/index";
    }

    @PostMapping("/add")
    public String taskAdd(@RequestParam String nameTask,
                          @RequestParam Set<Priority> priority,
                          @RequestParam Set<Status> status,
                          @RequestParam String duration,
                          @RequestParam String description,
                          @RequestParam String test,
                          Model model){
        Test test1 = testRepo.findById(Long.valueOf(test.split(" ")[0])).orElseThrow();

        Task task = new Task(nameTask, priority, status, duration, description, test1);
        taskRepo.save(task);
        return ("redirect:/task/all");
    }

    @PostMapping("/edit/{id}")
    public String taskEdit(Task task, BindingResult result) {
        if(result.hasErrors())
            return ("/Task/details");
        Task tempTask = taskRepo.findById(task.getId()).orElseThrow();
        task.setTest(tempTask.getTest());

        taskRepo.save(task);
        return("redirect:/task/all");
    }

    @GetMapping("/del/{id}")
    public String taskDelete(@PathVariable Long id) {
        taskRepo.deleteById(id);
        return("redirect:/task/all");
    }

    @GetMapping("/details/{id}")
    public String taskDetails(Model model, @PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAll();

        model.addAttribute("task", task);
        model.addAttribute("tests", tests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/details";
    }

    @GetMapping("/filter-contains")
    public String taskFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Task> tasks = taskRepo.findByNameTaskContaining(searchName);
        Iterable<Test> tests = testRepo.findAll();

        model.addAttribute("tasks", tasks);
        model.addAttribute("tests", tests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/filter";
    }

    @GetMapping("/sortPriorityAsc")
    public String taskSortAscContains(Model model){
        List<Task> tasks = taskRepo.findAll(Sort.by("priority").ascending());
        model.addAttribute("tasks", tasks);
        return "/Task/index";
    }

    @GetMapping("/sortPriorityDesc")
    public String taskSortDescContains(Model model){
        List<Task> tasks = taskRepo.findAll(Sort.by("priority").descending());
        model.addAttribute("tasks", tasks);
        return "/Task/index";
    }
}
