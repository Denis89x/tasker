package by.lebenkov.task_tracker.storage.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    String role;

    @OneToMany(mappedBy = "taskOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<Task> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Token> tokenList = new ArrayList<>();
}