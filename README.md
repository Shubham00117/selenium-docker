# Selenium Docker TestNG Framework

Selenium Java automation framework using Maven, TestNG, Page Object Model, Selenium Grid, Docker Compose, and ExtentReports.

## Prerequisites

- Java 21
- Maven 3.8+
- Docker Desktop or Docker Engine with Compose
- Git

## Run Tests Locally

Start Selenium Grid:

```bash
docker compose up -d
```

Run the TestNG master suite:

```bash
mvn test
```

Stop Selenium Grid:

```bash
docker compose down
```

The default configuration runs tests against Selenium Grid at `http://localhost:4444` with Chrome. You can override values from the command line:

```bash
mvn test -Dexecution=grid -Dbrowser=firefox -Dgrid.url=http://localhost:4444
```

## Test Suites

The master suite is configured in `src/test/resources/test-suites/master.xml` and includes:

- `flight-reservation.xml`
- `vendor-portal.xml`

## Reports

Generated TestNG and ExtentReports output is written under:

```text
test-output/
```

## GitHub Actions

GitHub Actions runs on pushes and pull requests to `main`, and can also be started manually from the Actions tab. The workflow:

- Sets up Java 21
- Starts the Selenium Grid with Docker Compose
- Waits for Grid readiness on `http://localhost:4444/status`
- Runs `mvn test`
- Uploads `test-output/` as a workflow artifact
