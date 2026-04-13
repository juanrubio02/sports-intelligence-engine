package com.sportsintelligence.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMatchRequest(
        @NotBlank(message = "homeTeam is required") String homeTeam,
        @NotBlank(message = "awayTeam is required") String awayTeam
) {
}
