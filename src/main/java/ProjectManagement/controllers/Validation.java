package ProjectManagement.controllers;

import ProjectManagement.entities.Response;

import java.util.regex.Pattern;

public class Validation {
    public static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$");
    private static Response<Void> isValidInput(String input, Pattern pattern) {
        if(input == null || input.isEmpty()) {
            return Response.createFailureResponse("input is null or empty.");
        }
        if(pattern==null||pattern.matcher(input).find()) {
            return Response.createSuccessfulResponse(null);
        } else {
            return Response.createFailureResponse("input doesn't match the pattern.");
        }
    }
    public static Response<Void> isValidUserProperties(String userName, String password, String email) {
        Response<Void> isUserNameValid = isValidInput(userName, null);
        if(!isUserNameValid.isSucceed()) {
            return isUserNameValid;
        }
        Response<Void> isPasswordValid = isValidInput(password, VALID_PASSWORD_REGEX);
        if(!isPasswordValid.isSucceed()) {
            return Response.createFailureResponse("Your password is not valid. It must contain at least one digit, one lower case letter, one upper case letter, and must be between 8 and 20 characters long.");
        }
        Response<Void> isEmailValid = isValidInput(email, VALID_EMAIL_REGEX);
        if(!isEmailValid.isSucceed()) {
            return Response.createFailureResponse("Your email is not valid.");
        }
        return Response.createSuccessfulResponse(null);
    }
}
