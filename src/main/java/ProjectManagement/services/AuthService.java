package ProjectManagement.services;


import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.PasswordEncryption;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.stereotype.Service;

import java.security.Key;


@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a random string of a given size using the Base64 encoding.
     *
     * @param size The size of the string to be generated.
     * @return The generated string.
     */
    private String generateRandomString(int size) {
        Base64StringKeyGenerator generator = new Base64StringKeyGenerator(size);
        return generator.generateKey();
    }

    /**
     * Receives a User object and checks if the user exists in the repository.
     * If it exists, generates and returns a new JSON Web Token (JWT) for the user.
     * If the user does not exist, returns a failure response with a message "User not found".
     *
     * @param user The User object to be checked and authenticated.
     * @return A Response object containing the generated JWT or an error message.
     */
    public Response<String> githubLogin(User user) {
        if (user != null && userRepository.existsById(user.getId())) {
            return Response.createSuccessfulResponse(generateNewToken(user));
        }
        return Response.createFailureResponse("User not found");
    }

    /**
     * Receives an email and a password and searches for a user in the repository with the given email.
     * If a user is found and the password provided is correct, generates and returns a new JWT for the user.
     * If the user is not found or the password is incorrect, returns a failure response with a message "Invalid email or password".
     *
     * @param email    The email of the user to be authenticated.
     * @param password The password of the user to be authenticated.
     * @return A Response object containing the generated JWT or an error message.
     */
    public Response<String> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && PasswordEncryption.isPasswordCorrect(user.getPassword(), password)) {
            return Response.createSuccessfulResponse(generateNewToken(user));
        } else {
            return Response.createFailureResponse("Invalid email or password");
        }
    }

    /**
     * Receives a JWT and validates it. If the token is valid, generates and returns a new JWT for the user associated with the token.
     * If the token is not valid, returns a failure response with a message "Invalid token".
     *
     * @param token The JWT to be validated and refreshed.
     * @return A Response object containing the generated JWT or an error message.
     */
    public Response<String> reLogin(String token) {
        Response<Integer> isValidToken = validateToken(token);
        if (isValidToken.isSucceed()) {
            User user = userRepository.findById(isValidToken.getData()).get();
            return Response.createSuccessfulResponse(generateNewToken(user));
        } else {
            return Response.createFailureResponse("Invalid token");
        }
    }

    /**
     * Receives a User object and generates a new JWT for the user.
     * Sets various claims (such as the user's ID, email, and source) in the token and returns it as a compact string.
     *
     * @param user The User object for which to generate the JWT.
     * @return The generated JWT as a compact string.
     */
    private String generateNewToken(User user) {
        long HourQuarterAsMillis = 900000;
        return Jwts.builder()
                .setId(generateRandomString(32))
                .setIssuer("IRD")
                .setIssuedAt(new java.util.Date())
                .setSubject("Authentication")
                .setExpiration(new java.util.Date(System.currentTimeMillis() + HourQuarterAsMillis))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("source", user.getSource().name())
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Receives a JWT and tries to parse it. If the token is valid and can be parsed, returns a success response with the user ID associated with the token.
     * If the token is invalid or cannot be parsed, returns a failure response with a relevant error message.
     *
     * @param jwt The JWT to be validated.
     * @return A Response object containing the user ID associated with the token or an error message.
     */
    public Response<Integer> validateToken(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            Integer userid = claimsJws.getBody().get("userId", Integer.class);
            return Response.createSuccessfulResponse(userid);
        } catch (SignatureException e) {
            return Response.createFailureResponse("Invalid token signature");
        } catch (ExpiredJwtException e) {
            return Response.createFailureResponse("Token expired");
        } catch (MalformedJwtException e) {
            return Response.createFailureResponse("Invalid token format");
        } catch (Exception e) {
            return Response.createFailureResponse("Invalid token");
        }
    }
}
