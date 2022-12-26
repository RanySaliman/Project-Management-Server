package ProjectManagement.controllers.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LoginCredentials {
   private String email;
    private String password;
}
