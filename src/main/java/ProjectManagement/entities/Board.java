package ProjectManagement.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(targetEntity=UserInBoard.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "board_id")
    Set<UserInBoard> users;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(targetEntity=Task.class,mappedBy = "board" , cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JsonManagedReference
    Set<Task> tasks;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "BoardTaskTypes",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
    @Column(name = "taskType")
     Set<String> taskTypes;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "board_statuses",   joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
    @Column(name = "Status")
    Set<String> statuses;




    public Board(String name) {
        this.name = name;
        this.users=new HashSet<>();
        this.tasks=new HashSet<>();
        this.taskTypes=new HashSet<>(Set.of("Task","Bug","Subtask"));
        this.statuses=new HashSet<>(Set.of("Open","In Progress","Done"));
    }

    public Optional<UserInBoard> getUser(int id){
        return this.getUsers().stream().filter(u->u.getUser().getId()==id).findFirst();
    }

}
