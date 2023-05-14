package com.example.TMS.Utility;

import com.example.TMS.Enums.Priority;
import com.example.TMS.Enums.Status;
import com.example.TMS.Project.Project;
import com.example.TMS.Project.ProjectRepo;
import com.example.TMS.Task.Task;
import com.example.TMS.Test.Test;
import com.example.TMS.User.User;
import com.example.TMS.User.UserRepo;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ImportHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "NameProject", "Description", "DateCreation" };



    public static boolean hasCSVFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }

    public static List<Project> csvToProjects(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Project> projects = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Project project = new Project(
                        csvRecord.get("NameProject"),
                        csvRecord.get("Description"),
                        csvRecord.get("DateCreation")
                );

                projects.add(project);
            }

            return projects;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static List<Test> csvToTests(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Test> tests = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Test test = new Test(
                        csvRecord.get("NameTest"),
                        Set.of(Status.valueOf(csvRecord.get("Status"))),
                        Double.parseDouble(csvRecord.get("Version")),
                        csvRecord.get("Description")
                );

                tests.add(test);
            }

            return tests;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static List<Task> csvToTasks(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Task> tasks = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Task task = new Task(
                        csvRecord.get("NameTask"),
                        Set.of(Priority.valueOf(csvRecord.get("Priority"))),
                        Set.of(Status.valueOf(csvRecord.get("Status"))),
                        csvRecord.get("Duration"),
                        csvRecord.get("Description")
                );

                tasks.add(task);
            }

            return tasks;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream projectsToCSV(List<Project> projects) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Project project : projects) {
                List<String> data = Arrays.asList(
                        String.valueOf(project.getId()),
                        project.getNameProject(),
                        project.getDescription(),
                        project.getDateCreation()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
