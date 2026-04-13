package com.sportsintelligence.infrastructure.persistence;

import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.repository.MatchRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMatchRepository implements MatchRepository {

    private final ConcurrentMap<UUID, Match> matches = new ConcurrentHashMap<>();

    @Override
    public Optional<Match> findById(UUID id) {
        return Optional.ofNullable(matches.get(id))
                .map(Match::copy);
    }

    @Override
    public Collection<Match> findAll() {
        return matches.values().stream()
                .map(Match::copy)
                .toList();
    }

    @Override
    public Match save(Match match) {
        Match storedMatch = match.copy();
        matches.put(storedMatch.getId(), storedMatch);
        return storedMatch.copy();
    }
}
