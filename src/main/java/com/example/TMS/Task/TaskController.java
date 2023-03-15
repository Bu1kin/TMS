package com.example.TMS.Task;

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
    //ДОДЕЛАТЬ СВЯЗИ В CRUD
    @Autowired
    TestRepo testRepo;
    @GetMapping("/all")
    public String viewTask(Model model) {
        Iterable<Task> tasks = taskRepo.findAll();
        model.addAttribute("tasks", tasks);
        return "/task/index";
    }

    @PostMapping("/add")
    public String taskAdd(Task task, Model model){
        taskRepo.save(task);
        return ("redirect:/task/all");
    }

    @PostMapping("/edit/{id}")
    public String taskEdit(Task task, BindingResult result) {
        if(result.hasErrors())
            return ("/task/edit");
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
        model.addAttribute("task", task);
        return "/task/details";
    }

    @GetMapping("/filter-contains")
    public String taskFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Task> tasks = taskRepo.findByNameTaskContaining(searchName);
        model.addAttribute("tasks", tasks);
        return "/task/filter";
    }
}
