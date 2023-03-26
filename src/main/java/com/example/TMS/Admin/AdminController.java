package com.example.TMS.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public String adminPanel(Model model){
        return ("/Admin/AdminPanel");
    }

    @GetMapping("/backup")
    @ResponseBody
    public String backup(String dbName) {
        try {
            String folderPath = System.getProperty("user.dir") + "\\backup\\";
            File temp = new File(folderPath);
            temp.mkdir();

            String savePath = folderPath + "backup.sql";

            //String executeCmd = "mysqldump --port=3306 --column-statistics=0 -uroot " + dbName + " -r " + savePath;
//            String executeCmd = "mysqldump --column-statistics=0 -uroot " + dbName + " -r " + savePath;

            String executeCmd = "mysqldump --column-statistics=0 -uroot TMS -r " + savePath;

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {

                return ("Резервное копирование базы данных прошло успешно");
            } else {

                return ("Не удалось сохранить резервную копию базы данных");
            }

        } catch (IOException | InterruptedException ex) {

            return ("Не удалось сохранить резервную копию базы данных: " + ex.getMessage());
        }
    }

    @GetMapping("/restore")
    @ResponseBody
    public String restore(String dbName){
        try {
            //String executeCmd = "cmd.exe /c mysql --port=3306 -uroot " + dbName + " < " + System.getProperty("user.dir") + "\\backup\\backup.sql";
            //String executeCmd = "cmd.exe /c mysql -uroot " + dbName + " < " + System.getProperty("user.dir") + "\\backup\\backup.sql";
            String executeCmd = "cmd.exe /c mysql -uroot TMS < " + System.getProperty("user.dir") + "\\backup\\backup.sql";
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                return ("БД успешно восстановлена из файла: " + System.getProperty("user.dir") + "\\backup\\backup.sql");
            } else {
                return ("Ошибка при восстановлении БД из файла: " + System.getProperty("user.dir") + "\\backup\\backup.sql");
            }
        } catch(IOException | InterruptedException | HeadlessException e){

            return ("Ошибка при восстановлении БД из файла: " + System.getProperty("user.dir") + "\\backup\\backup.sql" + " | " + e.getMessage());
        }
    }
}
