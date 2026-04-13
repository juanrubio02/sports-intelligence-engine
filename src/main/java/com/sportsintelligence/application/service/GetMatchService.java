package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.GetMatchUseCase;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetMatchService implements GetMatchUseCase {

    private final MatchRepository matchRepository;

    public GetMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Optional<Match> execute(UUID matchId) {
        Objects.requireNonNull(matchId, "matchId must not be null");
        return matchRepository.findById(matchId);
    }
}
