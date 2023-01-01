package Java.UtilitiesTest;

import ProjectManagement.entities.GithubUser;
import ProjectManagement.entities.Response;
import ProjectManagement.utils.GithubOAuthApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class GithubOAuthApiTest {
    @InjectMocks
    private GithubOAuthApi githubOAuthApi;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplate;
    @BeforeEach
    public void setUp() {
        try {
            Field restTemplateField = GithubOAuthApi.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(githubOAuthApi,restTemplate);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getAccessTokenFromGithub_whenRequestIsSuccessful_shouldReturnAccessToken() {

        // Given
        String code = "123456";
        String expectedToken = "abcdefghijklmnopqrstuvwxyz";
        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn("access_token=" + expectedToken);

        // When
        Response<String> response = githubOAuthApi.getAccessTokenFromGithub(code);

        // Then
        assertThat(response.isSucceed()).isTrue();
        assertThat(response.getData()).isEqualTo(expectedToken);
    }
    @Test
    void getUserInfoFromGithub_whenRequestIsSuccessful_shouldReturnUserInfo() {
        // Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer ACCESS_TOKEN");

        HttpEntity<Void> entityWithAccessToken = new HttpEntity<>(requestHeaders);
        GithubUser expectedUser = new GithubUser();
        expectedUser.setEmail("user@example.com");
        expectedUser.setName("user");
        expectedUser.setSource("github");
        expectedUser.setAccessToken("abcdefghijklmnopqrstuvwxyz");
        expectedUser.setSiteUsername("user-123");
        when(restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entityWithAccessToken, GithubUser.class)).thenReturn(ResponseEntity.ok(expectedUser));

        // When
        Response<GithubUser> response = githubOAuthApi.getUserInfoFromGithub(entityWithAccessToken);

        // Then
        assertThat(response.isSucceed()).isTrue();
        assertThat(response.getData()).matches(user -> user.getEmail().equals(expectedUser.getEmail()) &&
                user.getName().equals(expectedUser.getName()) &&
                user.getSource().equals(expectedUser.getSource()) &&
                user.getAccessToken().equals(expectedUser.getAccessToken()) &&
                user.getSiteUsername().equals(expectedUser.getSiteUsername()));
    }

}
