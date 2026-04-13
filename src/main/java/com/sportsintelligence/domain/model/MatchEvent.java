package com.sportsintelligence.domain.model;

import java.util.Objects;
import java.util.UUID;

public record MatchEvent(
        UUID id,
        UUID matchId,
        EventType type,
        int minute,
        TeamSide team
) {

    public MatchEvent {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(matchId, "matchId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(team, "team must not be null");

        if (minute < 0) {
            throw new IllegalArgumentException("minute must not be negative");
        }
    }
}
