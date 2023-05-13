package com.example.TMS.Utility;

import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    ProjectRepo projectRepo;

    public void save(MultipartFile file) {
        try {
            List<Project> tutorials = ImportHelper.csvToProjects(file.getInputStream());
            projectRepo.saveAll(tutorials);
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
