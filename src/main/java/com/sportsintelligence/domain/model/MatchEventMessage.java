package com.sportsintelligence.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record MatchEventMessage(
        UUID eventId,
        UUID matchId,
        EventType type,
        TeamSide team,
        Instant timestamp
) {

    public MatchEventMessage {
        Objects.requireNonNull(eventId, "eventId must not be null");
        Objects.requireNonNull(matchId, "matchId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(team, "team must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
    }
}
