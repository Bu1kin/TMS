package com.example.TMS.Project;

import com.example.TMS.Enums.Status;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;
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
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ImportHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);

                return("redirect:/project/all");
            } catch (Exception e) {
                return("redirect:/project/all");
            }
        }

        return("redirect:/project/all");
    }
}
