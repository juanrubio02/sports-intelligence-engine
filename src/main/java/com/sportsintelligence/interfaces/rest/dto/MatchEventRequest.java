package com.sportsintelligence.interfaces.rest.dto;

import com.sportsintelligence.domain.model.EventType;
import com.sportsintelligence.domain.model.TeamSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MatchEventRequest(
        @NotNull(message = "type is required") EventType type,
        @NotNull(message = "minute is required") @Min(value = 0, message = "minute must be greater than or equal to 0") Integer minute,
        @NotNull(message = "team is required") TeamSide team
) {
}
