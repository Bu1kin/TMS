package com.example.TMS.Project;

import com.example.TMS.Enums.Status;
import com.example.TMS.Role.Roles;
import com.example.TMS.Task.Task;
import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import com.example.TMS.Utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    TestRepo testRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CSVService fileService;

    @GetMapping("/all")
    public String viewProject(Principal principal, Model model) {
        Iterable<Project> projects = projectRepo.findAll();
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        List<Project> departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());
        String userData = user.getName() + " " + user.getMiddleName();

        model.addAttribute("userData", userData);
        model.addAttribute("projects", projects);
        model.addAttribute("departmentProjects", departmentProjects);
        model.addAttribute("statuses", Status.values());
        return "/Project/index";
    }

    @PostMapping("/add")
    public String projectAdd(@RequestParam String nameProject,
                             @RequestParam String description,
                             Principal principal,
                             Model model){

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        User user = userRepo.findByLoginAndActive(principal.getName(), true);

        Project project = new Project(nameProject, description, date);
        project.setUser(user);
        projectRepo.save(project);
        return ("redirect:/project/all");
    }

    @PostMapping("/edit/{id}")
    public String projectEdit(Project project, BindingResult result) {
        if(result.hasErrors())
            return ("/Project/details");

        Project projectTemp = projectRepo.findById(project.getId()).orElseThrow();

        project.setDateCreation(projectTemp.getDateCreation());
        project.setUser(projectTemp.getUser());

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
                          @RequestParam String description,
                          Model model){
        Project project = projectRepo.findById(id).orElseThrow();

        Test test = new Test(nameTest, status, 1.0, description, project);

        testRepo.save(test);
        return ("redirect:/project/testList/{id}");
    }

    @GetMapping("/testList/{id}/del")
    public String allTaskDeleteFromProject(@PathVariable Long id) {
        List<Test> tests = testRepo.findAllByProject_Id(id);

        for (Test test : tests) {
            testRepo.deleteById(test.getId());
        }
        return("redirect:/project/testList/{id}");
    }

    @PostMapping("/testList/{id}/import")
    public String testImport(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        Project project = projectRepo.findById(id).orElseThrow();

        if (ImportHelper.hasCSVFormat(file)) {
            try {
                fileService.saveTests(file, project);

                return ("redirect:/project/testList/{id}");
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        return ("redirect:/project/testList/{id}");
    }

    @GetMapping("/testList/{id}/export")
    public void projectTestsExportToCSV(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tests_from_project_#" + id + " " + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<Test> tests = testRepo.findAllByProject_Id(id);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"NameTest", "Status", "Version", "Description"};
        String[] nameMapping = {"nameTest", "status", "version", "description"};

        csvWriter.writeHeader(csvHeader);

        for (Test testTemp : tests) {
            csvWriter.write(testTemp, nameMapping);
        }

        csvWriter.close();
    }

//    @GetMapping("/testList/{idProject}/testDetails/{idTest}")
//    public String testDetailsFromTestList(Model model, @PathVariable Long idProject, @PathVariable Long idTest) {
//        Project currProject = projectRepo.findById(idProject).orElseThrow();
//        Test test = testRepo.findById(idTest).orElseThrow();
//
//        model.addAttribute("test", test);
//        model.addAttribute("status", Status.values());
//        return "/Test/details";
//    }
    //два контроллера: изменение и удаление данных теста внутри проекта

    @GetMapping("/sortTestListByStatusAsc/{id}")
    public String testListSortAsc(@PathVariable Long id, Model model){
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_IdOrderByStatusAsc(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        model.addAttribute("status", Status.values());
        return "/Project/testList";
    }

    @GetMapping("/sortTestListByStatusDesc/{id}")
    public String testListSortDesc(@PathVariable Long id, Model model){
        Project project = projectRepo.findById(id).orElseThrow();
        Iterable<Test> tests = testRepo.findAllByProject_IdOrderByStatusDesc(id);

        model.addAttribute("project", project);
        model.addAttribute("tests", tests);
        model.addAttribute("status", Status.values());
        return "/Project/testList";
    }

    @PostMapping("/import")
    public String uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {

        User user = userRepo.findByLoginAndActive(principal.getName(), true);

        if (ImportHelper.hasCSVFormat(file)) {
            try {
                fileService.saveProjects(file, user);

                return("redirect:/project/all");
            } catch (Exception e) {
                return("redirect:/project/all");
            }
        }

        return("redirect:/project/all");
    }

    @GetMapping("/export")
    public void exportToCSV(HttpServletResponse response, Principal principal) throws IOException {
        response.setContentType("text/csv");
        User user = userRepo.findByLoginAndActive(principal.getName(), true);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=projects_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<Project> departmentProjects;

        if(user.getRole().toArray()[0] == Roles.Пользователь){
            departmentProjects = projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());
        }
        else {
            departmentProjects = projectRepo.findAll();
        }

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"NameProject", "Description", "DateCreation"};
        String[] nameMapping = {"nameProject", "description", "dateCreation"};

        csvWriter.writeHeader(csvHeader);

        for (Project projectTemp : departmentProjects) {
            csvWriter.write(projectTemp, nameMapping);
        }

        csvWriter.close();
    }
}
