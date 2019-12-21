package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
