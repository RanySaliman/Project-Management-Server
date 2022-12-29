package Java;

import static org.junit.jupiter.api.Assertions.*;

import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserSource;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGithubLogin_UserNotFound() {
        User user = new User();
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(false);

        Response<String> response = authService.githubLogin(user);
        assertFalse(response.isSucceed());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void testGithubLogin_UserFound() {
        User user = new User();
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);

        Response<String> response = authService.githubLogin(user);
        assertTrue(response.isSucceed());
        assertNotNull(response.getData());
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "test@example.com";
        String password = "password";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        Response<String> response = authService.login(email, password);
        assertFalse(response.isSucceed());
        assertEquals("Invalid email or password", response.getMessage());
    }

    @Test
    void testLogin_PasswordIncorrect() {
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setPassword("incorrect_password");
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        Response<String> response = authService.login(email, password);
        assertFalse(response.isSucceed());
        assertEquals("Invalid email or password", response.getMessage());
    }

    @Test
    void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setPassword(password);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        Response<String> response = authService.login(email, password);
        assertTrue(response.isSucceed());
        assertNotNull(response.getData());
    }

    @Test
    void testReLogin_TokenExpired() {
        String token = "expired_token";
        Response<Integer> isValidToken = Response.createFailureResponse("Token expired");
        Mockito.when(authService.validateToken(token)).thenReturn(isValidToken);
        Response<String> response = authService.reLogin(token);
        assertFalse(response.isSucceed());
        assertEquals("Invalid token", response.getMessage());
    }

    @Test
    void testReLogin_Success() {
        String token = "valid_token";
        int userId = 1;
        Response<Integer> isValidToken = Response.createSuccessfulResponse(userId);
        Mockito.when(authService.validateToken(Mockito.anyString())).thenReturn(isValidToken);
        Mockito.verify(authService).validateToken(token);
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Response<String> response = authService.reLogin(token);
        assertTrue(response.isSucceed());
        assertNotNull(response.getData());
    }

    @Test
    void testGenerateNewToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User("test@example.com", "username", "password", UserSource.LOCAL);
        Class<? extends AuthService> aClass = authService.getClass();
        Method generateNewToken = aClass.getDeclaredMethod("generateNewToken", User.class);
        generateNewToken.setAccessible(true);
        generateNewToken.invoke(authService, user);
        String token = (String) generateNewToken.invoke(authService, user);
        assertNotNull(token);
    }

    @Test
    void testValidateToken_InvalidTokenSignature() {
        String token = "invalid_token";
        Response<Integer> response = authService.validateToken(token);
        assertFalse(response.isSucceed());
        assertEquals("Invalid token signature", response.getMessage());
    }

    @Test
    void testValidateToken_TokenExpired() {
        String token = "expired_token";
        Response<Integer> response = authService.validateToken(token);
        assertFalse(response.isSucceed());
    }

    @Test
    void testValidateToken_Success() {
        String token = "valid_token";
        int userId = 1;
        Response<Integer> response = authService.validateToken(token);
        assertTrue(response.isSucceed());
        assertEquals(userId, response.getData());
    }
}
