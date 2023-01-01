package Java.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ProjectManagement.controllers.UserController;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.services.AuthService;
import ProjectManagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService, authService);
    }

    @Test
    public void githubRegister_ShouldReturnLoginToken() {
        String code = "abcdefghijklmnopqrstuvwxyz";
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("test_password");
        user.setEmail("test@example.com");

        Response<User> userResponse = Response.createSuccessfulResponse(user);
        Response<String> loginResponse = Response.createSuccessfulResponse("login_token");

        when(userService.registerWithGithub(code)).thenReturn(userResponse);
        when(authService.githubLogin(user)).thenReturn(loginResponse);

        ResponseEntity<String> result = userController.githubRegister(code);

        assertEquals(200, result.getStatusCodeValue());

    }

    @Test
    public void testGithubRegisterGithubLoginFailed() {
        when(userService.registerWithGithub(anyString())).thenReturn(Response.createSuccessfulResponse(new User()));
        when(authService.githubLogin(any(User.class))).thenReturn(Response.createFailureResponse("github login failed"));
        ResponseEntity<String> result = userController.githubRegister("mock_code");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("failed to login.\ngithub login failed", result.getBody());
    }

}