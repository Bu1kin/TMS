package com.example.TMS.Test;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.Role.Roles;
import com.example.TMS.Task.Task;
import com.example.TMS.Task.TaskRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import com.example.TMS.Utility.CSVService;
import com.example.TMS.Utility.ImportHelper;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    CSVService fileService;

    @GetMapping("/all")
    public String viewTest(Principal principal, Model model) {
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        Iterable<Test> tests = testRepo.findAll();
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        Iterable<Project> departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("tests", tests);
        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("departmentProjects", departmentProjects);
        model.addAttribute("status", Status.values());
        return "/Test/index";
    }

    @PostMapping("/add")
    public String testAdd(@RequestParam String nameTest,
                          @RequestParam Set<Status> status,
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

        int successCounter = 0;
        int failCounter = 0;
        int cancelCounter = 0;
        int totalCounter = 0;
        double successResultPercentage = 0;
        double failResultPercentage = 0;
        double cancelResultPercentage = 0;

        for(Task task1 : test.getTasks()) {
            if (task1.getStatus().toArray()[0] == Status.Passed) {
                successCounter++;
                totalCounter++;
            } else if (task1.getStatus().toArray()[0] == Status.NotPassed) {
                failCounter++;
                totalCounter++;
            } else {
                cancelCounter++;
                totalCounter++;
            }
        }

        successResultPercentage = (double) successCounter / (double) totalCounter * 100;
        successResultPercentage = Precision.round(successResultPercentage, 1);

        failResultPercentage = (double) failCounter / (double) totalCounter * 100;
        failResultPercentage = Precision.round(failResultPercentage, 1);

        cancelResultPercentage = (double) cancelCounter / (double) totalCounter * 100;
        cancelResultPercentage = Precision.round(cancelResultPercentage, 1);

        model.addAttribute("successResultPercentage", successResultPercentage);
        model.addAttribute("failResultPercentage", failResultPercentage);
        model.addAttribute("cancelResultPercentage", cancelResultPercentage);

        model.addAttribute("successCounter", successCounter);
        model.addAttribute("failCounter", failCounter);
        model.addAttribute("cancelCounter", cancelCounter);
        model.addAttribute("totalCounter", totalCounter);

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

    @GetMapping("/taskList/{id}/del")
    public String allTaskDeleteFromTest(@PathVariable Long id) {
        List<Task> tasks = taskRepo.findAllByTest_Id(id);

        for (Task task : tasks) {
            taskRepo.deleteById(task.getId());
        }
        return("redirect:/test/taskList/{id}");
    }

    @PostMapping("/taskList/{id}/import")
    public String taskImport(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        Test test = testRepo.findById(id).orElseThrow();

        if (ImportHelper.hasCSVFormat(file)) {
            try {
                fileService.saveTasks(file, test);

                return ("redirect:/test/taskList/{id}");
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        return ("redirect:/test/taskList/{id}");
    }

    @GetMapping("/taskList/{id}/export")
    public void testTasksExportToCSV(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tasks_from_test_#" + id + " " + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<Task> tasks = taskRepo.findAllByTest_Id(id);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"NameTask", "Priority", "Status", "Duration", "Description"};
        String[] nameMapping = {"nameTask", "priority", "status", "duration", "description"};

        csvWriter.writeHeader(csvHeader);

        for (Task taskTemp : tasks) {
            csvWriter.write(taskTemp, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/sortTaskListByPriorityAsc/{id}")
    public String taskListSortAsc(@PathVariable Long id, Model model){
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_IdOrderByPriorityAsc(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Test/taskList";
    }

    @GetMapping("/sortTaskListByPriorityDesc/{id}")
    public String taskListSortDesc(@PathVariable Long id, Model model){
        Test test = testRepo.findById(id).orElseThrow();
        Iterable<Task> tasks = taskRepo.findAllByTest_IdOrderByPriorityDesc(id);

        model.addAttribute("test", test);
        model.addAttribute("tasks", tasks);
        model.addAttribute("priority", Priority.values());
        model.addAttribute("status", Status.values());
        return "/Test/taskList";
    }

    @GetMapping("/filter-contains")
    public String testFilterContains(@RequestParam String searchName,
                                         Principal principal,
                                         Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> searchedDepartmentTests = testRepo.findByNameTestContainingAndProjectUserDepartmentId(searchName, user.getDepartment().getId());
        List<Test> tests = testRepo.findByNameTestContaining(searchName);
        Iterable<Project> departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("searchedDepartmentTests", searchedDepartmentTests);
        model.addAttribute("tests", tests);
        model.addAttribute("departmentProjects", departmentProjects);
        model.addAttribute("status", Status.values());
        return "/Test/filter";
    }

    @GetMapping("/sortStatusAsc")
    public String testSortAscContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(Sort.by("status").ascending(), user.getDepartment().getId());
        List<Test> tests = testRepo.findAll(Sort.by("status").ascending());
        Iterable<Project> departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("tests", tests);
        model.addAttribute("departmentProjects", departmentProjects);
        model.addAttribute("status", Status.values());
        return "/Test/index";
    }

    @GetMapping("/sortStatusDesc")
    public String testSortDescContains(Principal principal, Model model){
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Test> departmentTests = testRepo.findAllByProjectUserDepartmentId(Sort.by("status").descending(), user.getDepartment().getId());
        List<Test> tests = testRepo.findAll(Sort.by("status").descending());
        Iterable<Project> departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());

        model.addAttribute("departmentTests", departmentTests);
        model.addAttribute("tests", tests);
        model.addAttribute("departmentProjects", departmentProjects);
        model.addAttribute("status", Status.values());
        return "/Test/index";
    }

    @GetMapping("/export")
    public void exportToCSV(HttpServletResponse response, Principal principal) throws IOException {
        response.setContentType("text/csv");
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tests_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<Test> departmentTests;

        if(user.getRole().toArray()[0] == Roles.Пользователь){
            departmentTests = testRepo.findAllByProjectUserDepartmentId(user.getDepartment().getId());
        }
        else {
            departmentTests = testRepo.findAll();
        }

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"NameTest", "Status", "Version", "Description"};
        String[] nameMapping = {"nameTest", "status", "version", "description"};

        csvWriter.writeHeader(csvHeader);

        for (Test testTemp : departmentTests) {
            csvWriter.write(testTemp, nameMapping);
        }

        csvWriter.close();
    }
}
