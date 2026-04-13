package com.sportsintelligence.domain.repository;

import com.sportsintelligence.domain.model.Match;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {

    Optional<Match> findById(UUID id);

    Collection<Match> findAll();

    Match save(Match match);
}
