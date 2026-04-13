package com.sportsintelligence.application.port.in;

import com.sportsintelligence.domain.model.OddsSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface GetOddsUseCase {

    Optional<OddsSnapshot> execute(UUID matchId);
}
