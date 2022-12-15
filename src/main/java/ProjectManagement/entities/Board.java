package ProjectManagement.entities;



import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column(nullable = false)
    String name;
    public Board(String name) {
        this.name = name;
    }
}
