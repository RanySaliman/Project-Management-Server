package ProjectManagement.entities;

import ProjectManagement.entities.enums.UserSource;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserSource source;
    private LocalDateTime registrationDate;




    public User(String email, String username, String password, UserSource source) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.source= source;
        this.registrationDate = LocalDateTime.now();
    }


}
