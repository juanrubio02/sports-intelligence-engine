package com.sportsintelligence.infrastructure.persistence;

import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.domain.repository.OddsRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryOddsRepository implements OddsRepository {

    private final ConcurrentMap<UUID, OddsSnapshot> latestSnapshots = new ConcurrentHashMap<>();

    @Override
    public Optional<OddsSnapshot> findLatestByMatchId(UUID matchId) {
        return Optional.ofNullable(latestSnapshots.get(matchId));
    }

    @Override
    public OddsSnapshot save(OddsSnapshot oddsSnapshot) {
        latestSnapshots.put(oddsSnapshot.matchId(), oddsSnapshot);
        return oddsSnapshot;
    }
}
