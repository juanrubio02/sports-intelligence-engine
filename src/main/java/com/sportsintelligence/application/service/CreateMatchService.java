package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.CreateMatchUseCase;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.domain.repository.MatchRepository;
import com.sportsintelligence.domain.repository.OddsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class CreateMatchService implements CreateMatchUseCase {

    private final MatchRepository matchRepository;
    private final OddsRepository oddsRepository;

    public CreateMatchService(MatchRepository matchRepository, OddsRepository oddsRepository) {
        this.matchRepository = matchRepository;
        this.oddsRepository = oddsRepository;
    }

    @Override
    public Match execute(CreateMatchCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        Match match = new Match(
                UUID.randomUUID(),
                command.homeTeam(),
                command.awayTeam(),
                0,
                0,
                0
        );

        Match savedMatch = matchRepository.save(match);
        oddsRepository.save(OddsSnapshot.calculate(savedMatch, Instant.now()));

        return savedMatch;
    }
}
