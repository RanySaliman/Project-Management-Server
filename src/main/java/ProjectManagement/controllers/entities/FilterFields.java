package ProjectManagement.controllers.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class FilterFields {
    private int id;
    private int parentTitle;
    private int creator;
    private int assignedUserId;
    LocalDateTime DueDate;
    int importance;
    String description;
    String title;

    public static class FilterFieldsBuilder {

        //Required Parameters
        //Optional Parameters
        @Id
        private int taskParentId;
        private int creator;
        private int assignedUserId ;
        LocalDateTime DueDate;
        int importance;
        private String title;

        public FilterFieldsBuilder() {
        }

        public FilterFieldsBuilder(int creator){
            this.creator = creator;
        }

        public FilterFieldsBuilder(String title){
            this.title = title;
        }

        public FilterFieldsBuilder DueDate( LocalDateTime DueDate) {
            this.DueDate = DueDate;
            return this;
        }

        public FilterFields build() {
            return new FilterFields(this);
        }
    }

    private FilterFields(FilterFieldsBuilder builder) {
        this.creator = builder.creator;
        this.title = builder.title;
        this.DueDate = builder.DueDate;
    }
    }
