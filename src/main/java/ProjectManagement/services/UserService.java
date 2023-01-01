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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final EmailSender emailSender;
    private final GithubOAuthApi githubOAuthApi;


    @Autowired
    public UserService(UserRepository userRepo, GithubOAuthApi githubOAuthApi, EmailSender emailSender) {
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.githubOAuthApi = githubOAuthApi;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user the user to be registered
     * @return a {@link Response} object with the registered user if the registration was successful, or an error message if it failed
     */
    private Response<User> registerUser(User user) {
        Response<String> userNotExists = checkIfUserNotExists(user.getEmail(), user.getUsername());
        if (userNotExists.isSucceed()) {
            System.out.println(user);
            return Response.createSuccessfulResponse(userRepo.save(user));
        } else {
            return Response.createFailureResponse(userNotExists.getMessage());
        }
    }

    /**
     * Register a new user with the given email, username, and password.
     * The user will be registered with a local account.
     *
     * @param email    the email of the user to be registered
     * @param username the username of the user to be registered
     * @param password the password of the user to be registered
     * @return a {@link Response} object with the registered user as its data if the registration was successful,
     * or an error message if the registration failed.
     */
    public Response<User> localRegister(String email, String username, String password) {
        User localUser = new User(email, username, PasswordEncryption.encryptPassword(password), UserSource.LOCAL);
        Response<User> registerUser = registerUser(localUser);
        if (registerUser.isSucceed()) {
            emailSender.sendEmail(List.of(localUser.getEmail()), "Welcome to Project Management", "Please click on the following link to activate your account: ");
        }
        return registerUser;
    }

    /**
     * Registers a new user with their GitHub account.
     *
     * @param code the authorization code returned by the GitHub OAuth API
     * @return a response object with the registered user if the registration was successful, or an error message otherwise
     */
    public Response<User> registerWithGithub(String code) {
        // Get access token from GitHub
        Response<String> getToken = githubOAuthApi.getAccessTokenFromGithub(code);
        if (getToken.isSucceed()) {
            // Use the access token to get user information from GitHub
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "bearer " + getToken.getData());
            HttpEntity<Void> httpEntityWithToken = new HttpEntity<>(headers);
            Response<GithubUser> getUserInfo = githubOAuthApi.getUserInfoFromGithub(httpEntityWithToken);
            Response<String> userEmailFromGithub = githubOAuthApi.getUserEmailFromGithub(httpEntityWithToken);
            // If we were able to get the user information and email from GitHub, create a new user with the information
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

    /**
     * Method that checks if a user with the same email already exists, and it is a GitHub user.
     * If the user exists, it updates the user with the new information provided.
     *
     * @param userToSave the {@link User} object with the new information to update
     * @return a {@link Response} object with a message indicating whether the user exists or not.
     * If the user exists, it will return a {@link Response} object with the user object.
     * If the user does not exist, the message will be "is not login".
     */
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

    /**
     * Method that checks if a user with the same email or username already exists.
     *
     * @param email    the email of the user to check
     * @param username the username of the user to check
     * @return a {@link Response} object with a message indicating whether the user exists or not.
     * If the user does not exist, the message will be "User doesn't exist.".
     * If the user exists, the message will be "User with this email already exists." if the email is the same
     * or "User with this username already exists." if the username is the same.
     */

    Response<String> checkIfUserNotExists(String email, String username) {
        if (userRepo.findByEmail(email) != null) {
            return Response.createFailureResponse("User with this email already exists.");
        }
        if (userRepo.findByUsername(username) != null) {
            return Response.createFailureResponse("User with this username already exists.");
        }
        return Response.createSuccessfulResponse("User doesn't exist.");
    }

    /**
     * This method is used to retrieve a user with a given id from the repository.
     *
     * @param id the id of the user to retrieve
     * @return a Response object containing the user if it exists, or a failure message if the user does not exist
     */
    public Response<User> getUserById(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return Response.createSuccessfulResponse(user.get());
        } else {
            return Response.createFailureResponse("User not found.");
        }
    }

    /**
     * This method is used to retrieve a user with a given username from the repository.
     *
     * @param name the username of the user to retrieve
     * @return a Response object containing the user if it exists, or a failure message if the user does not exist
     */
    public Response<User> getUserByName(String name) {
        User user = userRepo.findByUsername(name);
        if (user != null) {
            return Response.createSuccessfulResponse(user);
        } else {
            return Response.createFailureResponse("User not found.");
        }
    }
}

