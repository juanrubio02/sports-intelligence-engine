package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.ApplyEventUseCase;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.MatchEvent;
import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.domain.repository.MatchRepository;
import com.sportsintelligence.domain.repository.OddsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ApplyEventService implements ApplyEventUseCase {

    private final MatchRepository matchRepository;
    private final OddsRepository oddsRepository;
    private final ConcurrentMap<UUID, Object> matchLocks = new ConcurrentHashMap<>();

    public ApplyEventService(MatchRepository matchRepository, OddsRepository oddsRepository) {
        this.matchRepository = matchRepository;
        this.oddsRepository = oddsRepository;
    }

    @Override
    public Optional<Match> execute(ApplyEventCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        Object matchLock = matchLocks.computeIfAbsent(command.matchId(), ignored -> new Object());

        synchronized (matchLock) {
            return matchRepository.findById(command.matchId())
                    .map(match -> {
                        MatchEvent event = new MatchEvent(
                                UUID.randomUUID(),
                                command.matchId(),
                                command.type(),
                                command.minute(),
                                command.team()
                        );

                        match.applyEvent(event);
                        Match savedMatch = matchRepository.save(match);
                        oddsRepository.save(OddsSnapshot.calculate(savedMatch, Instant.now()));
                        return savedMatch;
                    });
        }
    }
}
