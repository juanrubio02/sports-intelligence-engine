package com.sportsintelligence.application.port.in;

import com.sportsintelligence.domain.model.Match;

public interface CreateMatchUseCase {

    Match execute(CreateMatchCommand command);

    record CreateMatchCommand(String homeTeam, String awayTeam) {
    }
}
