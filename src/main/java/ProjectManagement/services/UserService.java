package ProjectManagement.services;

import ProjectManagement.entities.GithubUser;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserSource;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.EmailSender;
import ProjectManagement.utils.GithubOAuthApi;
import ProjectManagement.utils.PasswordEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final EmailSender emailSender;
    private final GithubOAuthApi githubOAuthApi;


    @Autowired
    public UserService(UserRepository userRepo, GithubOAuthApi githubOAuthApi, EmailSender emailSender, EntityManager entityManager) {
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.githubOAuthApi = githubOAuthApi;
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
        Response<String> getToken = githubOAuthApi.getAccessTokenFromGithub(code);
        if (getToken.isSucceed()) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "bearer " + getToken.getData());
            HttpEntity<Void> httpEntityWithToken = new HttpEntity<>(headers);
            Response<GithubUser> getUserInfo = githubOAuthApi.getUserInfoFromGithub(httpEntityWithToken);
            Response<String> userEmailFromGithub = githubOAuthApi.getUserEmailFromGithub(httpEntityWithToken);
            if (getUserInfo.isSucceed() && userEmailFromGithub.isSucceed()) {
                GithubUser githubUser = getUserInfo.getData();
                githubUser.setEmail(userEmailFromGithub.getData());
                User userToSave = new User(githubUser.getEmail(), githubUser.getSiteUsername(), githubUser.getAccessToken(), UserSource.GITHUB);
                Response<User> userToLogin = isGithubLogin(userToSave);
                if (userToLogin.isSucceed()) return userToLogin;
                return registerUser(userToSave);
            } else {
                return Response.createFailureResponse(getUserInfo.getMessage());
            }
        } else return Response.createFailureResponse(getToken.getMessage());
    }

    private Response<User> isGithubLogin(User userToSave) {
        User user = userRepo.findByEmail(userToSave.getEmail());
        if (user != null && user.getSource() == UserSource.GITHUB) {
            user.setEmail(userToSave.getEmail());
            user.setUsername(userToSave.getUsername());
            user.setPassword(userToSave.getPassword());
            userRepo.save(user);
            return Response.createSuccessfulResponse(user);
        }
        return Response.createFailureResponse("is not login");
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

    public Response<User> getUserById(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return Response.createSuccessfulResponse(user.get());
        } else {
            return Response.createFailureResponse("User not found.");
        }
    }

    public Response<User> getUserByName(String name) {
        User user = userRepo.findByUsername(name);
        if (user != null) {
            return Response.createSuccessfulResponse(user);
        } else {
            return Response.createFailureResponse("User not found.");
        }
    }
}

