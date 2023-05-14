package com.example.TMS.Task;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Role.Roles;
import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    TestRepo testRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/all")
    public String viewTask(Principal principal, Model model) {
        Iterable<Task> tasks = taskRepo.findAll();

        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        List<Task> departmentTasks = taskRepo.findAllByTestProjectUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("tasks", tasks);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("departmentTasks", departmentTasks);
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
                                         Principal principal,
                                         Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        List<Task> searchedDepartmentTasks = taskRepo.findByNameTaskContainingAndTestProjectUserDepartmentId(searchName, user.getDepartment().getId());
        List<Task> tasks = taskRepo.findByNameTaskContaining(searchName);

        model.addAttribute("searchedDepartmentTasks", searchedDepartmentTasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/filter";
    }

    @GetMapping("/sortPriorityAsc")
    public String taskSortAscContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        List<Task> departmentTasks = taskRepo.findAllByTestProjectUserDepartmentId(Sort.by("priority").ascending(), user.getDepartment().getId());
        List<Task> tasks = taskRepo.findAll(Sort.by("priority").ascending());

        model.addAttribute("departmentTasks", departmentTasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/index";
    }

    @GetMapping("/sortPriorityDesc")
    public String taskSortDescContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        List<Task> departmentTasks = taskRepo.findAllByTestProjectUserDepartmentId(Sort.by("priority").descending(), user.getDepartment().getId());
        List<Task> tasks = taskRepo.findAll(Sort.by("priority").descending());

        model.addAttribute("departmentTasks", departmentTasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Task/index";
    }

    @GetMapping("/export")
    public void exportToCSV(HttpServletResponse response, Principal principal) throws IOException {
        response.setContentType("text/csv");
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tasks_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<Task> departmentTasks;

        if(user.getRole().toArray()[0] == Roles.Пользователь){
            departmentTasks = taskRepo.findAllByTestProjectUserDepartmentId(user.getDepartment().getId());
        }
        else {
            departmentTasks = taskRepo.findAll();
        }

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "NameTask", "Priority", "Status", "Duration", "Description"};
        String[] nameMapping = {"id", "nameTask", "priority", "status", "duration", "description"};

        csvWriter.writeHeader(csvHeader);

        for (Task taskTemp : departmentTasks) {
            csvWriter.write(taskTemp, nameMapping);
        }

        csvWriter.close();
    }
}
