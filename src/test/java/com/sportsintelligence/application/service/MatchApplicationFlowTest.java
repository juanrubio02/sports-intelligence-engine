package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.ApplyEventUseCase;
import com.sportsintelligence.application.port.in.CreateMatchUseCase;
import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.domain.model.TeamSide;
import com.sportsintelligence.infrastructure.persistence.InMemoryMatchRepository;
import com.sportsintelligence.infrastructure.persistence.InMemoryOddsRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchApplicationFlowTest {

    private final InMemoryMatchRepository matchRepository = new InMemoryMatchRepository();
    private final InMemoryOddsRepository oddsRepository = new InMemoryOddsRepository();

    private final CreateMatchService createMatchService = new CreateMatchService(matchRepository, oddsRepository);
    private final GetMatchService getMatchService = new GetMatchService(matchRepository);
    private final ApplyEventService applyEventService = new ApplyEventService(matchRepository, oddsRepository);
    private final GetOddsService getOddsService = new GetOddsService(matchRepository, oddsRepository);

    @Test
    void shouldCreateMatchApplyEventAndReturnUpdatedOdds() {
        Match createdMatch = createMatchService.execute(
                new CreateMatchUseCase.CreateMatchCommand("Madrid", "Barcelona")
        );

        applyEventService.execute(new ApplyEventUseCase.ApplyEventCommand(
                createdMatch.getId(),
                EventType.GOAL,
                17,
                TeamSide.HOME
        ));

        Optional<Match> updatedMatch = getMatchService.execute(createdMatch.getId());
        Optional<OddsSnapshot> oddsSnapshot = getOddsService.execute(createdMatch.getId());

        assertTrue(updatedMatch.isPresent());
        assertTrue(oddsSnapshot.isPresent());
        assertEquals(1, updatedMatch.get().getHomeScore());
        assertEquals(0, updatedMatch.get().getAwayScore());
        assertEquals(17, updatedMatch.get().getCurrentMinute());
        assertTrue(oddsSnapshot.get().probabilityHomeWin() > oddsSnapshot.get().probabilityAwayWin());
    }
}
