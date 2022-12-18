package ProjectManagement.services;

import ProjectManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public void registerWithGithub(String code)
    {
        String url = "https://github.com/login/oauth/access_token?"+
                "client_id=" +clientId+
                "&client_secret="+clientSecret +
                "&code=" + code;
        System.out.println(url);
        String response = restTemplate.postForObject(url, null, String.class);
        System.out.println(response);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","");
        restTemplate.getForEntity("https://api.github.com/user", String.class);
    }

}

