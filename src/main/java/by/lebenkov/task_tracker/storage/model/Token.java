package by.lebenkov.task_tracker.storage.model;

import by.lebenkov.task_tracker.storage.enums.TokenStatus;
import by.lebenkov.task_tracker.storage.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    TokenType tokenType = TokenType.BEARER;

    @Enumerated(EnumType.STRING)
    TokenStatus tokenStatus;

    boolean revoked;

    boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}