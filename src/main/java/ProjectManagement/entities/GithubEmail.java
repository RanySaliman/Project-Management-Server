package ProjectManagement.entities;

import lombok.Getter;

@Getter
public class GithubEmail {
    private String email;
    private boolean primary;
    private boolean verified;
    private String visibility;
}
