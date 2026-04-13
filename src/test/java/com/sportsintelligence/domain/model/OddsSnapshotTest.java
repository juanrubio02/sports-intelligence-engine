package com.sportsintelligence.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OddsSnapshotTest {

    @Test
    void calculateShouldFavorHomeTeamWhenLeadingLate() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 82, 2, 1);

        OddsSnapshot snapshot = OddsSnapshot.calculate(match, Instant.parse("2026-04-10T09:00:00Z"));

        assertTrue(snapshot.probabilityHomeWin() > snapshot.probabilityAwayWin());
        assertTrue(snapshot.probabilityHomeWin() > snapshot.probabilityDraw());
        assertProbabilitiesCloseToOne(snapshot);
    }

    @Test
    void calculateShouldIncreaseDrawProbabilityInLateTie() {
        Match match = new Match(UUID.randomUUID(), "Madrid", "Barcelona", 85, 1, 1);

        OddsSnapshot snapshot = OddsSnapshot.calculate(match, Instant.parse("2026-04-10T09:00:00Z"));

        assertTrue(snapshot.probabilityDraw() > snapshot.probabilityHomeWin());
        assertTrue(snapshot.probabilityDraw() > snapshot.probabilityAwayWin());
        assertProbabilitiesCloseToOne(snapshot);
    }

    @Test
    void constructorShouldRejectProbabilityOutsideRange() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OddsSnapshot(UUID.randomUUID(), 1.2, -0.1, 0.0, Instant.parse("2026-04-10T09:00:00Z"))
        );
    }

    private static void assertProbabilitiesCloseToOne(OddsSnapshot snapshot) {
        double total = snapshot.probabilityHomeWin()
                + snapshot.probabilityAwayWin()
                + snapshot.probabilityDraw();

        assertEquals(1.0, total, 0.0000001);
    }
}
