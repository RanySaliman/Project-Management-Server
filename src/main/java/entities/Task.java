package entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Task {
    /*
    ● Items have the following data:
○ Board id
○ Status (open/in progress/etc..)(per board)
○ Type (Task/Bug/Subtask/etc..)(per board)
○ Parent item
○ Creator (user)
○ Assigned to (user)
○ Due date
○ Importance (1-5)
○ Title
○ Description
○ Comments
     */
    int boardId;
    int id;
    Status status;
    TaskType type;
    Task parent;
    User creator;
    User assignedUser;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;
    List<String> comments;

    public Task() {}

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        DueDate = dueDate;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Task{" +
                "boardId=" + boardId +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", parent=" + parent +
                ", creator=" + creator +
                ", assignedUser=" + assignedUser +
                ", DueDate=" + DueDate +
                ", importance=" + importance +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return boardId == task.boardId && id == task.id && importance == task.importance && Objects.equals(status, task.status) && Objects.equals(type, task.type) && Objects.equals(parent, task.parent) && Objects.equals(creator, task.creator) && Objects.equals(assignedUser, task.assignedUser) && Objects.equals(DueDate, task.DueDate) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(comments, task.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, id, status, type, parent, creator, assignedUser, DueDate, importance, title, description, comments);
    }
}
