package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.out.EventQueue;
import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.MatchEventMessage;
import com.sportsintelligence.domain.model.TeamSide;
import com.sportsintelligence.domain.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MatchSimulationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchSimulationService.class);
    private static final int REGULATION_MINUTES = 90;

    private final MatchRepository matchRepository;
    private final EventQueue eventQueue;

    public MatchSimulationService(MatchRepository matchRepository, EventQueue eventQueue) {
        this.matchRepository = matchRepository;
        this.eventQueue = eventQueue;
    }

    @Scheduled(
            fixedDelayString = "${simulation.interval-ms:5000}",
            initialDelayString = "${simulation.initial-delay-ms:5000}"
    )
    public void simulate() {
        for (Match match : matchRepository.findAll()) {
            if (match.getCurrentMinute() >= REGULATION_MINUTES) {
                continue;
            }

            publishEvent(match.getId(), EventType.PERIOD_UPDATE, TeamSide.HOME);

            if (ThreadLocalRandom.current().nextDouble() < 0.35) {
                EventType randomEventType = randomMatchEventType();
                TeamSide randomTeamSide = randomTeamSide();

                publishEvent(match.getId(), randomEventType, randomTeamSide);
            }
        }
    }

    private void publishEvent(UUID matchId, EventType eventType, TeamSide teamSide) {
        MatchEventMessage eventMessage = new MatchEventMessage(
                UUID.randomUUID(),
                matchId,
                eventType,
                teamSide,
                Instant.now()
        );

        eventQueue.publish(eventMessage);
        LOGGER.info(
                "event_published eventId={} matchId={} type={} team={}",
                eventMessage.eventId(),
                eventMessage.matchId(),
                eventMessage.type(),
                eventMessage.team()
        );
    }

    private EventType randomMatchEventType() {
        int value = ThreadLocalRandom.current().nextInt(100);

        if (value < 20) {
            return EventType.GOAL;
        }
        if (value < 32) {
            return EventType.RED_CARD;
        }
        return EventType.YELLOW_CARD;
    }

    private TeamSide randomTeamSide() {
        return ThreadLocalRandom.current().nextBoolean() ? TeamSide.HOME : TeamSide.AWAY;
    }
}
