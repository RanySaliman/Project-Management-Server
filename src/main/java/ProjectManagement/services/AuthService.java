package ProjectManagement.services;

/*
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.PasswordEncryption;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private String generateRandomString(int size) {
        Base64StringKeyGenerator generator = new Base64StringKeyGenerator(size);
        return generator.generateKey();
    }

    public Response<String> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && PasswordEncryption.isPasswordCorrect(user.getPassword(), password)) {
            return Response.createSuccessfulResponse(generateNewToken(user));
        } else {
            return Response.createFailureResponse("Invalid email or password");
        }
    }

    private String generateNewToken(User user) {
        long HourQuarterAsMillis = 900000;
        return Jwts.builder()
                .setId(generateRandomString(32))
                .setIssuer("IRD")
                .setIssuedAt(new java.util.Date())
                .setSubject("Authentication")
                .setExpiration(new java.util.Date(System.currentTimeMillis() +  HourQuarterAsMillis))
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("source", user.getSource().name())
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public Response<Void> validateToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return Response.createSuccessfulResponse(null);
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
}*/
