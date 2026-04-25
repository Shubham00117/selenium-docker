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

## Jenkins

This repository includes a declarative `Jenkinsfile` for running the same Selenium Grid TestNG suite from Jenkins.

### Start Jenkins Locally

If you are running Jenkins from a WAR file, download `jenkins.war`, place it in this project folder, and run:

```bash
chmod +x jenkins-local-setup.sh
./jenkins-local-setup.sh
```

The setup script starts Jenkins on the default Jenkins port `8080`:

```bash
java -jar jenkins.war --httpPort=8080
```

Then open:

```text
http://localhost:8080
```

The Jenkins port is not configured inside `Jenkinsfile`. Jenkins must already be running before it can read and execute the pipeline.

### Jenkins Agent Requirements

Install these tools on the Jenkins agent that will run the job:

- Java 21
- Maven 3.8+
- Docker with Docker Compose v2
- Git
- curl

The Jenkins user must be allowed to run Docker commands.

### Create the Jenkins Pipeline Job

1. Create a new Jenkins item.
2. Select `Pipeline`.
3. In **Pipeline**, choose **Pipeline script**.
4. Paste the contents of `Jenkinsfile`.
5. Save the job.
6. Click `Build with Parameters` and choose the browser and suite XML if needed.

### Pipeline Behavior

The Jenkins pipeline:

- Checks out the repository from GitHub
- Starts Selenium Grid with `docker compose up -d --remove-orphans`
- Waits for Selenium Grid readiness on `http://localhost:5544/status`
- Runs `mvn -B -ntp test`
- Archives `test-output/`
- Stops Selenium Grid in the post-build cleanup step

Pipeline parameters:

- `BROWSER`: `chrome` or `firefox`
- `SUITE_XML`: TestNG suite file, default `src/test/resources/test-suites/master.xml`
