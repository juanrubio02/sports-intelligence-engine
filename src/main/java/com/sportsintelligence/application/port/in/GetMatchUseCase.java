package com.sportsintelligence.application.port.in;

import com.sportsintelligence.domain.model.Match;

import java.util.Optional;
import java.util.UUID;

public interface GetMatchUseCase {

    Optional<Match> execute(UUID matchId);
}
