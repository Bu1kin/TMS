package com.example.TMS.Utility;

import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.Task.Task;
import com.example.TMS.Task.TaskRepo;
import com.example.TMS.Test.Test;
import com.example.TMS.Test.TestRepo;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class CSVService {
    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    TestRepo testRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    public void saveProjects(MultipartFile file, User user) {
        try {
            List<Project> projects = ImportHelper.csvToProjects(file.getInputStream());
            for(var project : projects) {
                project.setUser(user);
            }
            projectRepo.saveAll(projects);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public void saveTests(MultipartFile file, Project project) {
        try {
            List<Test> tests = ImportHelper.csvToTests(file.getInputStream());
            for(var test : tests) {
                test.setProject(project);
            }
            testRepo.saveAll(tests);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public void saveTasks(MultipartFile file, Test test) {
        try {
            List<Task> tasks = ImportHelper.csvToTasks(file.getInputStream());
            for(var task : tasks) {
                task.setTest(test);
            }
            taskRepo.saveAll(tasks);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<Project> projects = projectRepo.findAll();

        ByteArrayInputStream in = ImportHelper.projectsToCSV(projects);
        return in;
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public List<Project> getAllDepartmentProjects(User user) {
        return projectRepo.findAllByUserDepartmentId(user.getDepartment().getId());
    }
}
