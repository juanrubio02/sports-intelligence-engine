package com.sportsintelligence.interfaces.rest.dto;

import com.sportsintelligence.domain.model.OddsSnapshot;

import java.time.Instant;
import java.util.UUID;

public record OddsResponse(
        UUID matchId,
        double probabilityHomeWin,
        double probabilityAwayWin,
        double probabilityDraw,
        Instant timestamp
) {

    public static OddsResponse from(OddsSnapshot oddsSnapshot) {
        return new OddsResponse(
                oddsSnapshot.matchId(),
                oddsSnapshot.probabilityHomeWin(),
                oddsSnapshot.probabilityAwayWin(),
                oddsSnapshot.probabilityDraw(),
                oddsSnapshot.timestamp()
        );
    }
}
