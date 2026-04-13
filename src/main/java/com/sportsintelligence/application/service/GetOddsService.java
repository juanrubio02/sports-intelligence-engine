package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.GetOddsUseCase;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.domain.repository.MatchRepository;
import com.sportsintelligence.domain.repository.OddsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetOddsService implements GetOddsUseCase {

    private final MatchRepository matchRepository;
    private final OddsRepository oddsRepository;

    public GetOddsService(MatchRepository matchRepository, OddsRepository oddsRepository) {
        this.matchRepository = matchRepository;
        this.oddsRepository = oddsRepository;
    }

    @Override
    public Optional<OddsSnapshot> execute(UUID matchId) {
        Objects.requireNonNull(matchId, "matchId must not be null");

        Optional<Match> match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            return Optional.empty();
        }

        Optional<OddsSnapshot> existingSnapshot = oddsRepository.findLatestByMatchId(matchId);
        if (existingSnapshot.isPresent()) {
            return existingSnapshot;
        }

        OddsSnapshot newSnapshot = OddsSnapshot.calculate(match.get(), Instant.now());
        oddsRepository.save(newSnapshot);
        return Optional.of(newSnapshot);
    }
}
