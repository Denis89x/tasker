package by.lebenkov.task_tracker.storage.model;

import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long taskId;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    TaskStatus taskStatus;

    @Column(name = "task_priority")
    Integer taskPriority;

    @Column(name = "due_date")
    LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User taskOwner;
}
