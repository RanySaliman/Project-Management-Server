package ProjectManagement.utils;

import ProjectManagement.entities.GithubEmail;
import ProjectManagement.entities.GithubUser;
import ProjectManagement.entities.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Component
public class GithubOAuthApi {
    @Value("${github.clientId}")
    private String clientId;
    @Value("${github.clientSecret}")
    private String clientSecret;

    private RestTemplate restTemplate;

    GithubOAuthApi(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Response<GithubUser> getUserInfoFromGithub(HttpEntity<Void> httpEntityWithToken) {
        GithubUser githubUser;
        try {
            ResponseEntity<GithubUser> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntityWithToken, GithubUser.class);
            githubUser = exchange.getBody();
        } catch (Exception e) {
            return Response.createFailureResponse("Error while getting user info from github");
        }
        if (githubUser == null) return Response.createFailureResponse("error in getting user info from github");
        return Response.createSuccessfulResponse(githubUser);
    }

    public Response<String> getUserEmailFromGithub(HttpEntity<Void> entityWithAccessToken) {
        try {
            ResponseEntity<GithubEmail[]> githubEmails = restTemplate.exchange("https://api.github.com/user/emails", HttpMethod.GET, entityWithAccessToken, GithubEmail[].class);
            if (githubEmails.getBody() == null)
                return Response.createFailureResponse("error in getting user email from github");
            for (GithubEmail email : githubEmails.getBody()) {
                if (email.isPrimary()) {
                    return Response.createSuccessfulResponse(email.getEmail());
                }
            }
            return Response.createFailureResponse("Error while getting user email from github");
        } catch (Exception e) {
            return Response.createFailureResponse("Error while getting user email from github");
        }
    }

    public Response<String> getAccessTokenFromGithub(String code) {
        String url = "https://github.com/login/oauth/access_token?" +
                "client_id=" + clientId + "&client_secret=" + clientSecret +
                "&code=" + code + "&scope=user:email";
        String access_token;
        try {
            String response = restTemplate.postForObject(url, null, String.class);
            if (response == null) return Response.createFailureResponse("error in getting access token from github");
            System.out.println(response);
            Map<String, String> params = fromURLParamsToMap(response);
            access_token = params.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.createFailureResponse("error in getting access token from github");
        }
        if (access_token != null) {
            return Response.createSuccessfulResponse(access_token);
        } else {
            return Response.createFailureResponse("Error while getting access token from github");
        }
    }

    private Map<String, String> fromURLParamsToMap(String url) {
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            String name = keyValue[0];
            String value = keyValue[1];
            map.put(name, value);
        }
        return map;
    }
}
