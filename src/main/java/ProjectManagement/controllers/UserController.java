package ProjectManagement.controllers;
import ProjectManagement.controllers.entities.UserToRegister;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/registerWithGithub")
    public ResponseEntity<String> githubRegister(@RequestParam String code) {
        userService.registerWithGithub(code);
        return ResponseEntity.ok("register");
    }
    @PostMapping("/register")
    public ResponseEntity<String> localRegister(@RequestBody UserToRegister userToRegister) {
        Response<Void> isValidUser = Validation.isValidUserProperties(userToRegister.getUserName(), userToRegister.getPassword(), userToRegister.getEmail());
        if(isValidUser.isSucceed()) {
            Response<User> isUserCreated = userService.localRegister(userToRegister.getEmail(), userToRegister.getUserName(), userToRegister.getPassword());
            if (isUserCreated.isSucceed()) {
                return ResponseEntity.ok("user created successfully.");
            } else {
                return ResponseEntity.badRequest().body(isUserCreated.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("invalid user properties\n"+isValidUser.getMessage());
        }
    }
}
