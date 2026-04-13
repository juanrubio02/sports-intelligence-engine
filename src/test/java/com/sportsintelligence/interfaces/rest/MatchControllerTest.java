package com.sportsintelligence.interfaces.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "simulation.interval-ms=60000",
        "simulation.initial-delay-ms=60000",
        "consumer.interval-ms=60000",
        "consumer.initial-delay-ms=60000"
})
@AutoConfigureMockMvc
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateMatchFromHttpRequest() throws Exception {
        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam": "Real Madrid",
                                  "awayTeam": "Barcelona"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.homeTeam").value("Real Madrid"))
                .andExpect(jsonPath("$.awayTeam").value("Barcelona"))
                .andExpect(jsonPath("$.currentMinute").value(0));
    }

    @Test
    void shouldRejectInvalidMatchCreationRequest() throws Exception {
        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam": " ",
                                  "awayTeam": "Barcelona"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("homeTeam is required"));
    }

    @Test
    void shouldRejectInvalidEventRequest() throws Exception {
        String createResponse = mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam": "Atletico",
                                  "awayTeam": "Sevilla"
                                }
                                """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(createResponse);
        String matchId = jsonNode.get("id").asText();

        mockMvc.perform(post("/matches/{id}/events", matchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "GOAL",
                                  "minute": -1,
                                  "team": "HOME"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JsonNode errorResponse = objectMapper.readTree(result.getResponse().getContentAsString());
                    assertThat(errorResponse.get("message").asText()).contains("minute must be greater than or equal to 0");
                });
    }

    @Test
    void shouldRejectMalformedMatchPayload() throws Exception {
        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam":
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed request payload"));
    }

    @Test
    void shouldRejectMissingEventFields() throws Exception {
        String createResponse = mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam": "Valencia",
                                  "awayTeam": "Betis"
                                }
                                """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String matchId = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(post("/matches/{id}/events", matchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "minute": 10
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JsonNode errorResponse = objectMapper.readTree(result.getResponse().getContentAsString());
                    assertThat(errorResponse.get("message").asText()).contains("type is required");
                    assertThat(errorResponse.get("message").asText()).contains("team is required");
                });
    }

    @Test
    void shouldRejectSameTeamMatchCreation() throws Exception {
        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "homeTeam": "Athletic Club",
                                  "awayTeam": "Athletic Club"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("homeTeam and awayTeam must be different"));
    }

    @Test
    void shouldReturnJsonNotFoundForMissingMatch() throws Exception {
        mockMvc.perform(get("/matches/{id}", "11111111-1111-1111-1111-111111111111"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Match not found"));
    }

    @Test
    void shouldReturnJsonBadRequestForInvalidMatchIdFormat() throws Exception {
        mockMvc.perform(get("/matches/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for id"));
    }
}
