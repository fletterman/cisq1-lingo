package nl.hu.cisq1.lingo.game.data;

import nl.hu.cisq1.lingo.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringGameRepository extends JpaRepository<Game, Long> {
}
