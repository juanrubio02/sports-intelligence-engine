package com.sportsintelligence.application.service;

import com.sportsintelligence.application.port.in.ApplyEventUseCase;
import com.sportsintelligence.application.port.in.GetMatchUseCase;
import com.sportsintelligence.application.port.out.EventQueue;
import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.MatchEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumerService.class);

    private final EventQueue eventQueue;
    private final GetMatchUseCase getMatchUseCase;
    private final ApplyEventUseCase applyEventUseCase;

    public EventConsumerService(
            EventQueue eventQueue,
            GetMatchUseCase getMatchUseCase,
            ApplyEventUseCase applyEventUseCase
    ) {
        this.eventQueue = eventQueue;
        this.getMatchUseCase = getMatchUseCase;
        this.applyEventUseCase = applyEventUseCase;
    }

    @Scheduled(
            fixedDelayString = "${consumer.interval-ms:1000}",
            initialDelayString = "${consumer.initial-delay-ms:1000}"
    )
    public void consumeEvents() {
        Optional<MatchEventMessage> nextEvent = eventQueue.consume();

        while (nextEvent.isPresent()) {
            MatchEventMessage eventMessage = nextEvent.get();
            LOGGER.info(
                    "event_consumed eventId={} matchId={} type={} team={}",
                    eventMessage.eventId(),
                    eventMessage.matchId(),
                    eventMessage.type(),
                    eventMessage.team()
            );

            getMatchUseCase.execute(eventMessage.matchId())
                    .ifPresent(match -> applyEventUseCase.execute(new ApplyEventUseCase.ApplyEventCommand(
                            eventMessage.matchId(),
                            eventMessage.type(),
                            resolveMinute(match, eventMessage),
                            eventMessage.team()
                    )));

            nextEvent = eventQueue.consume();
        }
    }

    private int resolveMinute(Match match, MatchEventMessage eventMessage) {
        if (eventMessage.type() == EventType.PERIOD_UPDATE) {
            return match.getCurrentMinute() + 1;
        }

        return match.getCurrentMinute();
    }
}
