package ProjectManagement.controllers;

import ProjectManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register")
    public ResponseEntity<String> register(){
        userService.registerWithGithub("be085e370d2680657296");
        return ResponseEntity.ok("register");
    }



}
