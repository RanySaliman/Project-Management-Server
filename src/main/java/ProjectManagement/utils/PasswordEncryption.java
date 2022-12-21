package ProjectManagement.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryption {
    /**
     * Encrypts a given password String using the BCryptPasswordEncoder() Spring Security method.
     *
     * @param password String to be encrypted.
     * @return String containing the encrypted password.
     */
    public static String encryptPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if the password the user inputted matches the encrypted password saved in the database.
     *
     * @param encryptedPassword String, encrypted user password as saved in database.
     * @param rawPassword String, password inputted by user that needs to be checked.
     * @return boolean, true - if the password user inputted matches user's password in the database; false - if inputted password is incorrect.
     */
    public static boolean isPasswordCorrect(String encryptedPassword, String rawPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
