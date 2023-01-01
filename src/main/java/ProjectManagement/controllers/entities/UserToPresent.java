package ProjectManagement.controllers.entities;

import ProjectManagement.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserToPresent {
    private String userName;
    private String email;

    public static UserToPresent fromUser(User user) {
        UserToPresent userToPresent = new UserToPresent();
        userToPresent.userName = user.getUsername();
        userToPresent.email = user.getEmail();
        return userToPresent;
    }
}
