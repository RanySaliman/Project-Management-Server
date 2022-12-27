package ProjectManagement;

import ProjectManagement.controllers.BoardController;
import ProjectManagement.services.TaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringApp extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(SpringApp.class, args);
    }
}
