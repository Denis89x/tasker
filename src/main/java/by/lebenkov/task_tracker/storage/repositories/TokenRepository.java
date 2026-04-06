package by.lebenkov.task_tracker.storage.repositories;

import by.lebenkov.task_tracker.storage.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
      select t from Token t inner join User u on t.user.userId = u.userId
      where u.userId = :id and (t.expired = false or t.revoked = false)
    """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
