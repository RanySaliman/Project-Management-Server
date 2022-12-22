package ProjectManagement.controllers;
/*
import ProjectManagement.controllers.entities.LoginCredentials;

import ProjectManagement.entities.Response;
import ProjectManagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCredentials credentials) {
        if (Validation.isValidUserProperties(credentials.getEmail(), credentials.getPassword()).isSucceed()) {
            Response<String> login = authService.login(credentials.getEmail(), credentials.getPassword());
            if (login.isSucceed()) {
                return ResponseEntity.ok(login.getData());
            } else {
                return ResponseEntity.badRequest().body(login.getMessage());
            }
        }
        else return ResponseEntity.badRequest().body("bad input format.");
    }
}
*/