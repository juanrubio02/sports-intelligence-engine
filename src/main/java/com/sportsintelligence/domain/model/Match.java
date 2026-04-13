package com.sportsintelligence.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Match {

    private final UUID id;
    private final String homeTeam;
    private final String awayTeam;
    private int currentMinute;
    private int homeScore;
    private int awayScore;

    public Match(
            UUID id,
            String homeTeam,
            String awayTeam,
            int currentMinute,
            int homeScore,
            int awayScore
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.homeTeam = requireTeamName(homeTeam, "homeTeam");
        this.awayTeam = requireTeamName(awayTeam, "awayTeam");

        if (this.homeTeam.equalsIgnoreCase(this.awayTeam)) {
            throw new IllegalArgumentException("homeTeam and awayTeam must be different");
        }

        updateMinute(currentMinute);
        updateScore(homeScore, awayScore);
    }

    public Match copy() {
        return new Match(id, homeTeam, awayTeam, currentMinute, homeScore, awayScore);
    }

    public UUID getId() {
        return id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("scores must not be negative");
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public void updateMinute(int minute) {
        if (minute < 0) {
            throw new IllegalArgumentException("minute must not be negative");
        }

        this.currentMinute = Math.max(this.currentMinute, minute);
    }

    public void applyEvent(MatchEvent event) {
        Objects.requireNonNull(event, "event must not be null");

        if (!id.equals(event.matchId())) {
            throw new IllegalArgumentException("event does not belong to this match");
        }

        updateMinute(event.minute());

        if (event.type() == EventType.GOAL) {
            if (event.team() == TeamSide.HOME) {
                updateScore(homeScore + 1, awayScore);
            } else {
                updateScore(homeScore, awayScore + 1);
            }
        }
    }

    private static String requireTeamName(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        String normalizedValue = value.trim();
        if (normalizedValue.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return normalizedValue;
    }
}
