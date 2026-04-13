package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.CreateMatchUseCase;
import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.MatchEventMessage;
import com.sportsintelligence.domain.model.TeamSide;
import com.sportsintelligence.infrastructure.messaging.InMemoryEventQueue;
import com.sportsintelligence.infrastructure.persistence.InMemoryMatchRepository;
import com.sportsintelligence.infrastructure.persistence.InMemoryOddsRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventConsumerServiceTest {

    private final InMemoryMatchRepository matchRepository = new InMemoryMatchRepository();
    private final InMemoryOddsRepository oddsRepository = new InMemoryOddsRepository();

    private final CreateMatchService createMatchService = new CreateMatchService(matchRepository, oddsRepository);
    private final GetMatchService getMatchService = new GetMatchService(matchRepository);
    private final ApplyEventService applyEventService = new ApplyEventService(matchRepository, oddsRepository);
    private final InMemoryEventQueue eventQueue = new InMemoryEventQueue();
    private final EventConsumerService eventConsumerService = new EventConsumerService(
            eventQueue,
            getMatchService,
            applyEventService
    );

    @Test
    void shouldConsumeQueuedEventsAndUpdateMatch() {
        Match match = createMatchService.execute(
                new CreateMatchUseCase.CreateMatchCommand("Madrid", "Barcelona")
        );

        eventQueue.publish(new MatchEventMessage(
                UUID.randomUUID(),
                match.getId(),
                EventType.PERIOD_UPDATE,
                TeamSide.HOME,
                Instant.parse("2026-04-10T09:00:00Z")
        ));
        eventQueue.publish(new MatchEventMessage(
                UUID.randomUUID(),
                match.getId(),
                EventType.GOAL,
                TeamSide.HOME,
                Instant.parse("2026-04-10T09:00:01Z")
        ));

        eventConsumerService.consumeEvents();

        Match updatedMatch = getMatchService.execute(match.getId()).orElseThrow();
        assertEquals(1, updatedMatch.getCurrentMinute());
        assertEquals(1, updatedMatch.getHomeScore());
        assertEquals(0, updatedMatch.getAwayScore());
    }
}
