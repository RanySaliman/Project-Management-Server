package entities;


import java.util.List;
import java.util.Objects;

public class Board {
    int id;
    String name;
    List<User> userList;

    public Board() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id == board.id && Objects.equals(name, board.name) && Objects.equals(userList, board.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userList);
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userList=" + userList +
                '}';
    }
}
