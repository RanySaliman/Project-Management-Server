package ProjectManagement.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser {
    private String name;
    private String email;
    @JsonProperty("login")
    private String siteUsername;
    private String source="github";
    private String accessToken;
}

