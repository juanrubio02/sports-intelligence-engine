package com.sportsintelligence.infrastructure.persistence;

import com.sportsintelligence.domain.model.Match;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryMatchRepositoryTest {

    @Test
    void findByIdShouldReturnDetachedCopy() {
        InMemoryMatchRepository repository = new InMemoryMatchRepository();
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 10, 1, 0);

        repository.save(match);

        Match loadedMatch = repository.findById(match.getId()).orElseThrow();
        loadedMatch.updateScore(5, 5);

        Optional<Match> reloadedMatch = repository.findById(match.getId());

        assertTrue(reloadedMatch.isPresent());
        assertEquals(1, reloadedMatch.get().getHomeScore());
        assertEquals(0, reloadedMatch.get().getAwayScore());
    }
}
