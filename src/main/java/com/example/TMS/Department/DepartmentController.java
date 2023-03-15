package com.example.TMS.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentRepo departmentRepo;

    @GetMapping("/all")
    public String viewDepartments(Model model) {
        Iterable<Department> departments = departmentRepo.findAll();
        model.addAttribute("departments", departments);
        return "/Admin/Departments/index";
    }

    @PostMapping("/add")
    public String departmentAdd(Department department, Model model){
        departmentRepo.save(department);
        return ("redirect:/department/all");
    }

    @PostMapping("/edit/{id}")
    public String departmentEdit(Department department, BindingResult result) {
        if(result.hasErrors())
            return ("/Admin/Departments/details");
        departmentRepo.save(department);
        return("redirect:/department/all");
    }

    @GetMapping("/del/{id}")
    public String departmentDelete(@PathVariable Long id) {
        departmentRepo.deleteById(id);
        return("redirect:/department/all");
    }

    @GetMapping("/details/{id}")
    public String departmentDetails(Model model, @PathVariable Long id) {
        Department department = departmentRepo.findById(id).orElseThrow();
        model.addAttribute("department", department);
        return "/Admin/Departments/details";
    }

    @GetMapping("/filter-contains")
    public String departmentFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Department> departmentList = departmentRepo.findByNameDepartmentContaining(searchName);
        model.addAttribute("departmentList", departmentList);
        return "/Admin/Departments/filter";
    }
}
