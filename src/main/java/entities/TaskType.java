package entities;

import java.util.Objects;

public class TaskType {
    String name;

    public TaskType() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskType{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskType taskType = (TaskType) o;
        return Objects.equals(name, taskType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
