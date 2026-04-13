package com.sportsintelligence.interfaces.rest.dto;

import com.sportsintelligence.domain.model.Match;

import java.util.UUID;

public record MatchResponse(
        UUID id,
        String homeTeam,
        String awayTeam,
        int currentMinute,
        int homeScore,
        int awayScore
) {

    public static MatchResponse from(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getCurrentMinute(),
                match.getHomeScore(),
                match.getAwayScore()
        );
    }
}
