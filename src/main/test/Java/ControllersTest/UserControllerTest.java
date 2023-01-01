package Java.ControllersTest;

import ProjectManagement.controllers.UserController;
import ProjectManagement.controllers.Validation;
import ProjectManagement.controllers.entities.UserToRegister;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserSource;
import ProjectManagement.services.AuthService;
import ProjectManagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // initialize mocks here, if necessary
    }

    @Test
    void testGithubRegister_Success() {
        String code = "test_code";
        User user = new User();
        Response<User> userResponse = Response.createSuccessfulResponse(user);
        String token = "test_token";
        Response<String> githubLogin = Response.createSuccessfulResponse(token);
        when(userService.registerWithGithub(code)).thenReturn(userResponse);
        when(authService.githubLogin(user)).thenReturn(githubLogin);

        ResponseEntity<String> response = userController.githubRegister(code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void testGithubRegister_UserServiceError() {
        String code = "test_code";
        String errorMessage = "user service error";
        Response<User> userResponse = Response.createFailureResponse(errorMessage);
        when(userService.registerWithGithub(code)).thenReturn(userResponse);

        ResponseEntity<String> response = userController.githubRegister(code);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("failed to register.\n" + errorMessage, response.getBody());
    }

    @Test
    void testGithubRegister_AuthServiceError() {
        String code = "test_code";
        User user = new User();
        Response<User> userResponse = Response.createSuccessfulResponse(user);
        String errorMessage = "auth service error";
        Response<String> githubLogin = Response.createFailureResponse(errorMessage);
        when(userService.registerWithGithub(code)).thenReturn(userResponse);
        when(authService.githubLogin(user)).thenReturn(githubLogin);
        ResponseEntity<String> response = userController.githubRegister(code);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("failed to login.\n" + errorMessage, response.getBody());
    }

    @Test
    void testLocalRegister_Success() {
        String UserName = "test_username";
        String Password = "StrongPassword1#";
        String Email = "email@Gmail.com";
        UserToRegister userToRegister = new UserToRegister();
        userToRegister.setUsername(UserName);
        userToRegister.setPassword(Password);
        userToRegister.setEmail(Email);
        User user = new User(Email,UserName ,Password , UserSource.LOCAL);
        Response<Void> isValidUser = Response.createSuccessfulResponse(null);
        when(userService.localRegister(anyString(), anyString(), anyString())).thenReturn(Response.createSuccessfulResponse(user));

        ResponseEntity<String> response = userController.localRegister(userToRegister);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user created successfully.", response.getBody());
    }

    @Test
    void testLocalRegister_InvalidUserProperties() {
        UserToRegister userToRegister = new UserToRegister();
        userToRegister.setUsername("test_username");
        userToRegister.setPassword("test_password");
        userToRegister.setEmail("test_email");
        String errorMessage = "invalid user properties";
        Response<Void> isValidUser = Response.createFailureResponse(errorMessage);


        ResponseEntity<String> response = userController.localRegister(userToRegister);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("invalid user properties\n", response.getBody().substring(0, 24));
    }

    @Test
    void testLocalRegister_UserServiceError() {
        UserToRegister userToRegister = new UserToRegister();
        userToRegister.setUsername("username");
        userToRegister.setPassword("StrongPassword1#");
        userToRegister.setEmail("email@gmail.com");
        String errorMessage = "user service error";
        when(userService.localRegister(anyString(), anyString(), anyString())).thenReturn(Response.createFailureResponse(errorMessage));

        ResponseEntity<String> response = userController.localRegister(userToRegister);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}