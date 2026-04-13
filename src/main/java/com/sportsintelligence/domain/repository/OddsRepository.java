package com.sportsintelligence.domain.repository;

import com.sportsintelligence.domain.model.OddsSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface OddsRepository {

    Optional<OddsSnapshot> findLatestByMatchId(UUID matchId);

    OddsSnapshot save(OddsSnapshot oddsSnapshot);
}
