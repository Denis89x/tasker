package by.lebenkov.task_tracker.storage.model;

import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE task SET deleted = true WHERE task_id = ?")
@SQLRestriction("deleted = false")
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    LocalDateTime updatedAt;

    @Builder.Default
    boolean deleted = false;
}
