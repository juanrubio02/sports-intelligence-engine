package com.sportsintelligence.infrastructure.messaging;

import com.sportsintelligence.application.port.out.EventQueue;
import com.sportsintelligence.domain.model.MatchEventMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class InMemoryEventQueue implements EventQueue {

    private final BlockingQueue<MatchEventMessage> queue = new LinkedBlockingQueue<>();

    @Override
    public void publish(MatchEventMessage event) {
        queue.offer(event);
    }

    @Override
    public Optional<MatchEventMessage> consume() {
        return Optional.ofNullable(queue.poll());
    }
}
