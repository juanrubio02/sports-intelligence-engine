package com.sportsintelligence.infrastructure.messaging;

import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.MatchEventMessage;
import com.sportsintelligence.domain.model.TeamSide;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryEventQueueTest {

    @Test
    void shouldPublishAndConsumeEvent() {
        InMemoryEventQueue queue = new InMemoryEventQueue();
        MatchEventMessage eventMessage = new MatchEventMessage(
                UUID.randomUUID(),
                UUID.randomUUID(),
                EventType.GOAL,
                TeamSide.HOME,
                Instant.parse("2026-04-10T09:00:00Z")
        );

        queue.publish(eventMessage);

        Optional<MatchEventMessage> consumedEvent = queue.consume();

        assertTrue(consumedEvent.isPresent());
        assertEquals(eventMessage, consumedEvent.get());
    }
}
