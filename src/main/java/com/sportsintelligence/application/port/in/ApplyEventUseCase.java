package com.sportsintelligence.application.port.in;

import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.TeamSide;

import java.util.Optional;
import java.util.UUID;

public interface ApplyEventUseCase {

    Optional<Match> execute(ApplyEventCommand command);

    record ApplyEventCommand(
            UUID matchId,
            EventType type,
            int minute,
            TeamSide team
    ) {
    }
}
