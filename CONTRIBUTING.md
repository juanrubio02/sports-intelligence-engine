# Contributing

## Local Setup

1. Install Java 21 and Maven 3.9+.
2. Run `./mvnw test`.
3. Start the app with `./mvnw spring-boot:run`.

## Guidelines

- Keep the layered architecture intact: `domain`, `application`, `infrastructure`, and `interfaces`
- Add or update tests for any behavior change
- Prefer small, focused pull requests with descriptive commit messages
- Document new endpoints or commands in `README.md`

## Pull Requests

- Explain the problem being solved
- Summarize the technical approach
- Mention validation steps such as `./mvnw test` or `docker compose up --build`
