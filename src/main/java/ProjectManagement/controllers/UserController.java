package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.LoginCredentials;
import ProjectManagement.controllers.entities.UserToRegister;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.services.AuthService;
import ProjectManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/registerWithGithub")
    public ResponseEntity<String> githubRegister(@RequestParam String code) {
        Response<User> userResponse = userService.registerWithGithub(code);
        if (userResponse.isSucceed()) {
            Response<String> githubLogin = authService.githubLogin(userResponse.getData());
            if (githubLogin.isSucceed()) {
                return ResponseEntity.ok(githubLogin.getData());
            } else {
                return ResponseEntity.badRequest().body("failed to login.\n"+githubLogin.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("failed to register.\n"+userResponse.getMessage());
    }

    @PostMapping("/register")
    public ResponseEntity<String> localRegister(@RequestBody UserToRegister userToRegister) {
        Response<Void> isValidUser = Validation.isValidUserProperties(userToRegister.getUserName(), userToRegister.getPassword(), userToRegister.getEmail());
        if (isValidUser.isSucceed()) {
            Response<User> isUserCreated = userService.localRegister(userToRegister.getEmail(), userToRegister.getUserName(), userToRegister.getPassword());
            if (isUserCreated.isSucceed()) {
                return ResponseEntity.ok("user created successfully.");
            } else {
                return ResponseEntity.badRequest().body(isUserCreated.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("invalid user properties\n" + isValidUser.getMessage());
        }
    }
}
