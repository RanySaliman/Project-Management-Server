package ProjectManagement.services;

import ProjectManagement.entities.GithubEmail;
import ProjectManagement.entities.GithubUser;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserSource;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.EmailSender;
import ProjectManagement.utils.PasswordEncryption;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final EmailSender emailSender;
    private final RestTemplate restTemplate;
    @Value("${github.clientId}")
    private String clientId;
    @Value("${github.clientSecret}")
    private String clientSecret;

    @Autowired
    public UserService(UserRepository userRepo, RestTemplateBuilder builder, EmailSender emailSender) {
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.restTemplate = builder.build();
    }

    private Response<User> registerUser(User user) {
        Response<String> userNotExists = checkIfUserNotExists(user.getEmail(), user.getUsername());
        if (userNotExists.isSucceed()) {
            System.out.println(user);
            return Response.createSuccessfulResponse(userRepo.save(user));
        } else {
            return Response.createFailureResponse(userNotExists.getMessage());
        }
    }

    public Response<User> localRegister(String email, String username, String password) {
        User localUser = new User(email, username, PasswordEncryption.encryptPassword(password), UserSource.LOCAL);
        Response<User> registerUser = registerUser(localUser);
        if (registerUser.isSucceed()) {
            emailSender.sendEmail(List.of(localUser.getEmail()), "Welcome to Project Management", "Please click on the following link to activate your account: ");
        }
        return registerUser;
    }

    public Response<User> registerWithGithub(String code) {
        String url = "https://github.com/login/oauth/access_token?" +
                "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
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
        ResponseEntity<GithubEmail[]> githubEmails = restTemplate.exchange("https://api.github.com/user/emails", HttpMethod.GET, entity, GithubEmail[].class);
        for (GithubEmail email : githubEmails.getBody()) {
            if (email.isPrimary()) {
                githubUser.setEmail(email.getEmail());
                break;
            }
        }
        User userToSave = new User(githubUser.getEmail(), githubUser.getSiteUsername(), githubUser.getAccessToken(), UserSource.GITHUB);
        return registerUser(userToSave);
    }

    Response<String> checkIfUserNotExists(String email, String username) {
        if (userRepo.findByEmail(email) != null) {
            return Response.createFailureResponse("User with this email already exists.");
        }
        if (userRepo.findByUsername(username) != null) {
            return Response.createFailureResponse("User with this username already exists.");
        }
        return Response.createSuccessfulResponse("User doesn't exist.");
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

