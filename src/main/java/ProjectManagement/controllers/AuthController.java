package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.LoginCredentials;

import ProjectManagement.entities.Response;
import ProjectManagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;


@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    private final Pattern bearerPattern = Pattern.compile("^[Bb]earer\\s+(.*)$");

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCredentials credentials) {
        if (Validation.isValidUserProperties(credentials.getEmail(), credentials.getPassword()).isSucceed()) {
            Response<String> login = authService.login(credentials.getEmail(), credentials.getPassword());
            if (login.isSucceed()) {
                return ResponseEntity.ok(login.getData());
            } else {
                return ResponseEntity.badRequest().body(login.getMessage());
            }
        } else return ResponseEntity.badRequest().body("bad input format.");
    }

    @GetMapping("/loginWithToken")
    public ResponseEntity<String> reLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        var matcher = bearerPattern.matcher(bearerToken);
        if (matcher.matches()) {
            String token = matcher.group(1);
            Response<String> tryReLogin = authService.reLogin(token);
            if (tryReLogin.isSucceed()) {
                return ResponseEntity.ok(tryReLogin.getData());
            }
        }
        return ResponseEntity.badRequest().body("Failed to get new token with token.\nPlease try to login with email & Password.");
    }

}
