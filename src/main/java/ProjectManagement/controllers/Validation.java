package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.TaskInput;
import ProjectManagement.entities.Response;

import java.util.regex.Pattern;

public class Validation {
    public static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$");

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
    public static Response<Void> isValidTaskInput(TaskInput taskInput )
    {
        if(taskInput.getImportance()>5 || taskInput.getImportance() < 1){
            return Response.createFailureResponse("Importance must be between 1 to 5");
        }
        if(taskInput.getTitle()==null || taskInput.getTitle().isEmpty()|| taskInput.getTitle().length()>50){
            return Response.createFailureResponse("Title is required and must be less than 50 characters.");
        }
        if(taskInput.getDescription()==null || taskInput.getDescription().isEmpty()|| taskInput.getDescription().length()>500){
            return Response.createFailureResponse("Description is required and must be less than 500 characters.");
        }
        if(taskInput.getStatus()==null || taskInput.getStatus().isEmpty()|| taskInput.getStatus().length()>25){
            return Response.createFailureResponse("Status is required and must be less than 25 characters.");
        }
        if(taskInput.getType()==null || taskInput.getType().isEmpty()|| taskInput.getType().length()>25){
            return Response.createFailureResponse("Type is required and must be less than 25 characters.");
        }
        return Response.createSuccessfulResponse(null);
    }
}
