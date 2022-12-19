package ProjectManagement.services;

import ProjectManagement.entities.GithubUser;
import ProjectManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepo;

    private final RestTemplate restTemplate;
    @Value("${github.clientId}")
    private String clientId;
    @Value("${github.clientSecret}")
    private String clientSecret;

    @Autowired
    public UserService(UserRepository userRepo, RestTemplateBuilder builder) {
        this.userRepo = userRepo;
        this.restTemplate = builder.build();
    }

    public void registerWithGithub(String code) {
        String url = "https://github.com/login/oauth/access_token?" +
                "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code+
                "&scope=user:email";
        System.out.println(url);
        String response = restTemplate.postForObject(url, null, String.class);
        System.out.println(response);
        Map<String, String> params = fromURLParamsToMap(response);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "bearer " + params.get("access_token"));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubUser> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entity, GithubUser.class);
        GithubUser githubUser = exchange.getBody();
        githubUser.setAccessToken(params.get("access_token"));
        //restTemplate.exchange("https://api.github.com/user/emails", HttpMethod.GET, entity, );
       // githubUser.setEmail();
    }

    private Map<String, String> fromURLParamsToMap(String url) {
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            try {
                String[] keyValue = param.split("=");
                String name = keyValue[0];
                String value = keyValue[1];
                map.put(name, value);
            } catch (Exception e) {
            }
        }
        return map;
    }
}

