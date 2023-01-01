package Java.ControllersTest;

import ProjectManagement.controllers.AuthController;
import ProjectManagement.controllers.entities.LoginCredentials;
import ProjectManagement.entities.Response;
import ProjectManagement.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;


    @Test
    void login_whenCredentialsAreValid_shouldReturnOkResponse() {
        // Given
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("StrongPassword1#");
        when(authService.login(anyString(), anyString())).thenReturn(Response.createSuccessfulResponse("token"));

        // When
        ResponseEntity<String> response = authController.login(credentials);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("token");
    }

    @Test
    void login_whenCredentialsAreInvalid_shouldReturnBadRequestResponse() {
        // Given
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("StrongPassword1#");
        when(authService.login(anyString(), anyString())).thenReturn(Response.createFailureResponse("error message"));

        // When
        ResponseEntity<String> response = authController.login(credentials);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("error message");
    }

    @Test
    void reLogin_whenTokenIsValid_shouldReturnOkResponse() {
        // Given
        when(authService.reLogin(anyString())).thenReturn(Response.createSuccessfulResponse("new token"));

        // When
        ResponseEntity<String> response = authController.reLogin("Bearer token");

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("new token");
    }

    @Test
    void reLogin_whenTokenIsInvalid_shouldReturnBadRequestResponse() {
        // Given
        when(authService.reLogin(anyString())).thenReturn(Response.createFailureResponse("error message"));

        // When
        ResponseEntity<String> response = authController.reLogin("Bearer token");

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Failed to get new token with token.\nPlease try to login with email & Password.");
    }
    @Test
    void reLogin_whenTokenIsMalformed_shouldReturnBadRequestResponse() {
        // When
        ResponseEntity<String> response = authController.reLogin("Invalid token");

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Failed to get new token with token.\nPlease try to login with email & Password.");
    }

    @Test
    void reLogin_whenAuthorizationHeaderIsMissing_shouldReturnBadRequestResponse() {
        // When
        ResponseEntity<String> response = authController.reLogin(null);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("no token provided.");
    }
}

