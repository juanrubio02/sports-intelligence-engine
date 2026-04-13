package com.sportsintelligence.interfaces.rest;

import com.sportsintelligence.application.port.in.ApplyEventUseCase;
import com.sportsintelligence.application.port.in.CreateMatchUseCase;
import com.sportsintelligence.application.port.in.GetMatchUseCase;
import com.sportsintelligence.application.port.in.GetOddsUseCase;
import com.sportsintelligence.domain.model.Match;
import com.sportsintelligence.domain.model.OddsSnapshot;
import com.sportsintelligence.interfaces.rest.dto.CreateMatchRequest;
import com.sportsintelligence.interfaces.rest.dto.MatchEventRequest;
import com.sportsintelligence.interfaces.rest.dto.MatchResponse;
import com.sportsintelligence.interfaces.rest.dto.OddsResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final CreateMatchUseCase createMatchUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final ApplyEventUseCase applyEventUseCase;
    private final GetOddsUseCase getOddsUseCase;

    public MatchController(
            CreateMatchUseCase createMatchUseCase,
            GetMatchUseCase getMatchUseCase,
            ApplyEventUseCase applyEventUseCase,
            GetOddsUseCase getOddsUseCase
    ) {
        this.createMatchUseCase = createMatchUseCase;
        this.getMatchUseCase = getMatchUseCase;
        this.applyEventUseCase = applyEventUseCase;
        this.getOddsUseCase = getOddsUseCase;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody CreateMatchRequest request) {
        Match match = createMatchUseCase.execute(
                new CreateMatchUseCase.CreateMatchCommand(request.homeTeam(), request.awayTeam())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(MatchResponse.from(match));
    }

    @GetMapping("/{id}")
    public MatchResponse getMatch(@PathVariable UUID id) {
        return getMatchUseCase.execute(id)
                .map(MatchResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
    }

    @PostMapping("/{id}/events")
    public MatchResponse applyEvent(@PathVariable UUID id, @Valid @RequestBody MatchEventRequest request) {
        return applyEventUseCase.execute(new ApplyEventUseCase.ApplyEventCommand(
                        id,
                        request.type(),
                        request.minute(),
                        request.team()
                ))
                .map(MatchResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
    }

    @GetMapping("/{id}/odds")
    public OddsResponse getOdds(@PathVariable UUID id) {
        return getOddsUseCase.execute(id)
                .map(OddsResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
    }
}
