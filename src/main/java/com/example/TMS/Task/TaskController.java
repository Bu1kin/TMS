package com.example.TMS.Task;

import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "/Task/index";
    }

    @PostMapping("/add")
    public String taskAdd(@RequestParam String nameTask,
                          @RequestParam String priority,
                          @RequestParam String status,
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
        return "/Task/details";
    }

    @GetMapping("/filter-contains")
    public String taskFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Task> tasks = taskRepo.findByNameTaskContaining(searchName);
        model.addAttribute("tasks", tasks);
        return "/Task/filter";
    }
}
