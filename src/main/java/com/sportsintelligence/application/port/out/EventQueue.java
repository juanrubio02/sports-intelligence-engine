package com.sportsintelligence.application.port.out;

import com.sportsintelligence.domain.model.MatchEventMessage;

import java.util.Optional;

public interface EventQueue {

    void publish(MatchEventMessage event);

    Optional<MatchEventMessage> consume();
}
