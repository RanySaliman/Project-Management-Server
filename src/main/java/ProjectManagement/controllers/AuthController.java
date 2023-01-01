package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.LoginCredentials;

import ProjectManagement.entities.Response;
import ProjectManagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * Controller for handling authentication related requests.
 */
@RestController
public class AuthController {
    /**
     * Service for handling authentication related tasks.
     */
    @Autowired
    private AuthService authService;
    /**
     * Regular expression for extracting the token from a Bearer token.
     */
    private final Pattern bearerPattern = Pattern.compile("^[Bb]earer\\s+(.*)$");

    /**
     * Logs in a user using their email and password.
     *
     * @param credentials the login credentials of the user
     * @return an HTTP response with the login token if the login is successful,
     * or an HTTP error response with an error message if the login fails
     */
    @PostMapping("/login")
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

    /**
     * Obtains a new login token using an existing one.
     *
     * @param bearerToken the existing login token in the form of a Bearer token
     * @return an HTTP response with the new login token if the re-login is successful,
     * or an HTTP error response with an error message if the re-login fails
     */
    @GetMapping("/loginWithToken")
    public ResponseEntity<String> reLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        if(bearerToken== null|| bearerToken.isEmpty()) return ResponseEntity.badRequest().body("no token provided.");
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
