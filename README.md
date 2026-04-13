# Sports Intelligence Engine

Backend service built with Java 21 and Spring Boot for simulating live football matches, ingesting domain events, and recalculating win/draw probabilities in real time.

## Why This Project Is Interesting

This repository is intentionally shaped like a production-oriented backend rather than a CRUD demo:

- Hexagonal-style separation between domain, application, infrastructure, and interfaces
- Event-driven flow with a simulation publisher and an asynchronous consumer
- In-memory repositories that make the app easy to run locally while keeping ports ready for real adapters
- Deterministic domain logic for match state transitions and odds recalculation
- Automated tests covering domain rules, application flows, infrastructure components, and HTTP endpoints
- Container-ready setup, OpenAPI docs, CI workflow, and contribution scaffolding

## Portfolio Highlights

- Designed a modular backend with clear application boundaries
- Modeled live match state and probability updates as domain behavior, not controller logic
- Added request validation, structured JSON errors, and automated HTTP tests
- Prepared the project for real-world collaboration with CI, Docker, PR templates, and contributor guidance

## Tech Stack

- Java 21
- Spring Boot 3
- Maven
- H2 for local profile
- PostgreSQL-ready production profile
- Springdoc OpenAPI / Swagger UI
- Spring Boot Actuator
- JUnit 5 and MockMvc
- Docker and Docker Compose

## Architecture

```text
interfaces/rest
  -> controllers and DTOs

application
  -> use cases and orchestration services

domain
  -> match model, events, odds logic, repository contracts

infrastructure
  -> in-memory repositories, in-memory event queue, scheduling config
```

## Core Features

- Create a match with home and away teams
- Retrieve current match state
- Apply events such as `GOAL`, `RED_CARD`, `YELLOW_CARD`, and `PERIOD_UPDATE`
- Retrieve the latest probability snapshot for home win, away win, or draw
- Simulate ongoing matches with scheduled event publishing and consumption
- Validate incoming requests and return consistent JSON errors
- Explore the API through Swagger UI and monitor health via Actuator

## Running Locally

### Prerequisites

- Java 21
- Optional: Maven 3.9+ if you do not want to use the included wrapper

### Start the application

```bash
./mvnw spring-boot:run
```

The application starts with the `dev` profile and uses an in-memory H2 database.

### Useful local URLs

- API docs: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Health endpoint: `http://localhost:8080/actuator/health`
- H2 console: `http://localhost:8080/h2-console`

## Run With Docker

```bash
docker compose up --build
```

This starts:

- the Spring Boot app on port `8080`
- PostgreSQL on port `5432`

## API Quickstart

### Create a match

```bash
curl -X POST http://localhost:8080/matches \
  -H "Content-Type: application/json" \
  -d '{
    "homeTeam": "Real Madrid",
    "awayTeam": "Barcelona"
  }'
```

### Apply an event

```bash
curl -X POST http://localhost:8080/matches/{matchId}/events \
  -H "Content-Type: application/json" \
  -d '{
    "type": "GOAL",
    "minute": 17,
    "team": "HOME"
  }'
```

### Get current odds

```bash
curl http://localhost:8080/matches/{matchId}/odds
```

## Make Commands

```bash
make run
make test
make package
make docker-build
make compose-up
```

## Tests

Run the full test suite with:

```bash
./mvnw test
```

## Configuration

The app includes two profiles:

- `dev`: uses H2 in-memory database
- `prod`: expects PostgreSQL connection variables `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`

Simulation and consumer timing can be adjusted through:

```text
simulation.interval-ms
simulation.initial-delay-ms
consumer.interval-ms
consumer.initial-delay-ms
```

## Current Tradeoffs

These are conscious simplifications, not hidden gaps:

- Persistence adapters are still in-memory, even though the app is already structured for real database adapters
- Odds calculation is heuristic and deterministic, not ML-based
- There is no authentication or authorization layer yet
- The production profile is PostgreSQL-ready, but a true JPA persistence implementation is still pending

## What I Would Build Next

- PostgreSQL-backed repository adapters
- Event broker integration with Kafka or RabbitMQ
- Metrics, tracing, and dashboards
- Authentication for administrative endpoints
- Load testing and performance baselines

## Repository Hygiene

The project already includes:

- CI workflow for pushes and pull requests
- Cross-platform CI matrix for Linux, macOS, and Windows
- Dockerfile and Compose setup
- `.editorconfig` and `.gitattributes`
- MIT license
- Pull request template
- Contributing guide
- OpenAPI metadata and health endpoint exposure

## Before Publishing To GitHub

- Replace `your-username` in `OpenApiConfiguration` with your real GitHub handle
- Add CI and build badges once the repository URL exists
- Consider splitting the current work into a few intentional commits before pushing

## Cross-Platform Notes

- The repository includes `mvnw` and `mvnw.cmd` so contributors do not need a preinstalled Maven version
- GitHub Actions validates the project on Linux, macOS, and Windows
- Docker build validation runs separately to catch packaging regressions

## License

MIT
