package ProjectManagement.entities;

import ProjectManagement.entities.enums.UserSource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String username;
    private String password;
    private UserSource source;



    public User(String email, String username, String password, UserSource source) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.source= source;
    }






}
