package ProjectManagement.entities;



import ProjectManagement.entities.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
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
    @ElementCollection
    @CollectionTable(name = "BoardMembersAndRoles",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "userId")
    @Column(name = "userRole")
    Map<User, UserRole> users;


    @ElementCollection
    @CollectionTable(name = "BoardTasksType",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "taskId")
    @Column(name = "taskStatus")
    Map<Task,TaskType> taskTypeMap;


    @ElementCollection
    @CollectionTable(name = "BoardTasksStatuses",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "taskId")
    @Column(name = "taskStatus")
    Map<Task,Status> taskStatusMap;

    @ElementCollection
    @CollectionTable(name = "board_statuses", joinColumns = @JoinColumn(name = "board_id"))
    Set<Status> statuses;




    public Board(String name) {
        this.name = name;
        this.users=new HashMap<>();
        this.taskStatusMap=new HashMap<>();
        this.taskTypeMap=new HashMap<>();
    }
    public void addUser(User user, UserRole userRole) {
        users.put(user, userRole);
    }
}
