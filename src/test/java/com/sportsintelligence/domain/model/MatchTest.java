package com.sportsintelligence.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchTest {

    @Test
    void applyGoalEventShouldUpdateScoreAndMinute() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 12, 0, 0);
        MatchEvent event = new MatchEvent(
                UUID.randomUUID(),
                match.getId(),
                EventType.GOAL,
                15,
                TeamSide.HOME
        );

        match.applyEvent(event);

        assertEquals(15, match.getCurrentMinute());
        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void applyAwayGoalEventShouldUpdateOnlyAwayScoreAndMinute() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 12, 1, 0);
        MatchEvent event = new MatchEvent(
                UUID.randomUUID(),
                match.getId(),
                EventType.GOAL,
                18,
                TeamSide.AWAY
        );

        match.applyEvent(event);

        assertEquals(18, match.getCurrentMinute());
        assertEquals(1, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    void applyNonGoalEventShouldOnlyAdvanceMinute() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 25, 1, 0);
        MatchEvent event = new MatchEvent(
                UUID.randomUUID(),
                match.getId(),
                EventType.YELLOW_CARD,
                28,
                TeamSide.AWAY
        );

        match.applyEvent(event);

        assertEquals(28, match.getCurrentMinute());
        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void updateMinuteShouldNotMoveBackwards() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 40, 2, 1);

        match.updateMinute(35);

        assertEquals(40, match.getCurrentMinute());
    }

    @Test
    void applyEventShouldRejectDifferentMatchId() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 10, 0, 0);
        MatchEvent event = new MatchEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                EventType.GOAL,
                12,
                TeamSide.HOME
        );

        assertThrows(IllegalArgumentException.class, () -> match.applyEvent(event));
    }

    @Test
    void applyHomeGoalShouldNotChangeAwayScoreWhenAwayAlreadyHasGoals() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 55, 2, 2);
        MatchEvent event = new MatchEvent(
                UUID.randomUUID(),
                match.getId(),
                EventType.GOAL,
                56,
                TeamSide.HOME
        );

        match.applyEvent(event);

        assertEquals(3, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
        assertEquals(56, match.getCurrentMinute());
    }

    @Test
    void constructorShouldRejectSameTeams() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Match(UUID.randomUUID(), "Madrid", "Madrid", 0, 0, 0)
        );
    }

    @Test
    void constructorShouldRejectBlankTeamNames() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Match(UUID.randomUUID(), " ", "Barcelona", 0, 0, 0)
        );
    }

    @Test
    void updateMinuteShouldRejectNegativeValues() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> match.updateMinute(-1));
    }

    @Test
    void updateScoreShouldRejectNegativeValues() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, 0));
    }
}
