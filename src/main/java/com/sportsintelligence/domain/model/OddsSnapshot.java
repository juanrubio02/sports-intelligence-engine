package com.sportsintelligence.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record OddsSnapshot(
        UUID matchId,
        double probabilityHomeWin,
        double probabilityAwayWin,
        double probabilityDraw,
        Instant timestamp
) {

    public OddsSnapshot {
        Objects.requireNonNull(matchId, "matchId must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        validateProbability(probabilityHomeWin, "probabilityHomeWin");
        validateProbability(probabilityAwayWin, "probabilityAwayWin");
        validateProbability(probabilityDraw, "probabilityDraw");
    }

    public static OddsSnapshot calculate(Match match, Instant timestamp) {
        Objects.requireNonNull(match, "match must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");

        int scoreDifference = match.getHomeScore() - match.getAwayScore();
        double progress = clamp(match.getCurrentMinute() / 90.0, 0.0, 1.0);
        double scoreImpact = scoreDifference * (0.35 + (0.45 * progress));

        double homeWeight = 1.0 + 0.10 + scoreImpact;
        double awayWeight = 1.0 - 0.10 - scoreImpact;
        double drawWeight = 0.75;

        if (scoreDifference == 0) {
            drawWeight += 0.25 + (0.55 * progress);
        } else {
            drawWeight -= Math.min(0.45, Math.abs(scoreDifference) * (0.18 + (0.22 * progress)));
        }

        homeWeight = Math.max(0.05, homeWeight);
        awayWeight = Math.max(0.05, awayWeight);
        drawWeight = Math.max(0.05, drawWeight);

        double totalWeight = homeWeight + awayWeight + drawWeight;

        return new OddsSnapshot(
                match.getId(),
                homeWeight / totalWeight,
                awayWeight / totalWeight,
                drawWeight / totalWeight,
                timestamp
        );
    }

    private static void validateProbability(double probability, String fieldName) {
        if (Double.isNaN(probability) || probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(fieldName + " must be between 0.0 and 1.0");
        }
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
