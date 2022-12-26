package ProjectManagement.controllers.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserToRegister {
    private String userName;
    private String password;
    private String email;
}
