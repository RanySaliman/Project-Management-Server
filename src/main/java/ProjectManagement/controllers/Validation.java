package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.TaskInput;
import ProjectManagement.entities.Response;

import java.util.regex.Pattern;

public class Validation {
    /**
     * A regex pattern that matches a valid email address.
     * The email address must contain at least one alphanumeric character, followed by an '@' symbol,
     * followed by at least one alphanumeric character, followed by a '.' symbol,
     * followed by at least two characters (which can be either alphabetic or numeric).
     * The email address is case-insensitive.
     */
    public static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    /**
     * A regex pattern that matches a valid password.
     * The password must contain at least 8 characters, at most 20 characters,
     * at least one lowercase letter, at least one uppercase letter, and at least one number.
     */
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$");

    /**
     * Validates an input string based on a given regex pattern.
     *
     * @param input   the input string to validate
     * @param pattern the regex pattern to use for validation
     * @return a response object with a success status and a null data if the input is valid,
     * or a failure status and an error message if the input is invalid
     */
    private static Response<Void> isValidInput(String input, Pattern pattern) {
        if (input == null || input.isEmpty()) {
            return Response.createFailureResponse("input is null or empty.");
        }
        if (pattern == null || pattern.matcher(input).find()) {
            return Response.createSuccessfulResponse(null);
        } else {
            return Response.createFailureResponse("input doesn't match the pattern.");
        }
    }

    /**
     * Validates the given email and password.
     *
     * @param email    the email to be validated
     * @param password the password to be validated
     * @return a response object indicating the result of the validation. If the validation is successful, the response will be successful. Otherwise, it will contain an error message.
     */
    public static Response<Void> isValidUserProperties(String email, String password) {
        Response<Void> isPasswordValid = isValidInput(password, VALID_PASSWORD_REGEX);
        if (!isPasswordValid.isSucceed()) {
            return Response.createFailureResponse("Your password is not valid. It must contain at least one digit, one lower case letter, one upper case letter, and must be between 8 and 20 characters long.");
        }
        Response<Void> isEmailValid = isValidInput(email, VALID_EMAIL_REGEX);
        if (!isEmailValid.isSucceed()) {
            return Response.createFailureResponse("Your email is not valid.");
        }
        return Response.createSuccessfulResponse(null);
    }

    /**
     * Validates the given user properties.
     *
     * @param userName the username to validate
     * @param password the password to validate
     * @param email    the email to validate
     * @return a response object indicating the result of the validation. If the validation is successful, the response will be successful. Otherwise, it will contain an error message.
     */
    public static Response<Void> isValidUserProperties(String userName, String password, String email) {
        Response<Void> isUserNameValid = isValidInput(userName, null);
        if (!isUserNameValid.isSucceed()) {
            return isUserNameValid;
        }
        Response<Void> response = isValidUserProperties(email, password);
        if (!response.isSucceed()) {
            return response;
        }
        return Response.createSuccessfulResponse(null);
    }

    /**
     * The method isValidTaskInput is a helper method that checks whether the provided TaskInput object has valid values for its fields. It returns a Response object with a Void payload, indicating whether the validation was successful or not.
     * <p>
     * The method checks the following conditions:
     * <p>
     * The importance field must be between 1 and 5 (inclusive).
     * The title field must not be null or empty, and must have a length less than or equal to 50 characters.
     * The description field must not be null or empty, and must have a length less than or equal to 500 characters.
     * The status field must not be null or empty, and must have a length less than or equal to 25 characters.
     * The type field must not be null or empty, and must have a length less than or equal to 25 characters.
     * If all of these conditions are met, the method returns a successful Response object. Otherwise, it returns a failure Response object with a message indicating which condition was not met.
     *
     * @param taskInput the TaskInput object to validate
     * @return a Response object indicating whether the validation was successful or not
     */

    public static Response<Void> isValidTaskInput(TaskInput taskInput) {
        if (taskInput.getImportance() > 5 || taskInput.getImportance() < 1) {
            return Response.createFailureResponse("Importance must be between 1 to 5");
        }
        if (taskInput.getTitle() == null || taskInput.getTitle().isEmpty() || taskInput.getTitle().length() > 50) {
            return Response.createFailureResponse("Title is required and must be less than 50 characters.");
        }
        if (taskInput.getDescription() == null || taskInput.getDescription().isEmpty() || taskInput.getDescription().length() > 500) {
            return Response.createFailureResponse("Description is required and must be less than 500 characters.");
        }
        if (taskInput.getStatus() == null || taskInput.getStatus().isEmpty() || taskInput.getStatus().length() > 25) {
            return Response.createFailureResponse("Status is required and must be less than 25 characters.");
        }
        if (taskInput.getType() == null || taskInput.getType().isEmpty() || taskInput.getType().length() > 25) {
            return Response.createFailureResponse("Type is required and must be less than 25 characters.");
        }
        return Response.createSuccessfulResponse(null);
    }
}
