APP_NAME=sports-intelligence-engine

.PHONY: run test package docker-build compose-up compose-down

run:
	./mvnw spring-boot:run

test:
	./mvnw test

package:
	./mvnw clean package

docker-build:
	docker build -t $(APP_NAME):local .

compose-up:
	docker compose up --build

compose-down:
	docker compose down
