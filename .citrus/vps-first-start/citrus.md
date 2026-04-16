# CITRUS Delivery Record

- Change set: `vps-first-start`
- Goal: clone `liza_alert_backend` to the target VPS, start PostgreSQL via Docker Compose, build the application, and complete the first application startup

## Environment

- Host: `185.21.8.116:2222`
- Deploy user: `denis`
- App directory: `/home/denis/apps/liza_alert_backend`

## Actions performed

- created deployment directory `/home/denis/apps`
- cloned repository `https://github.com/Denis0872/liza_alert_backend.git`
- started PostgreSQL container with `docker compose up -d`
- built the project on the server with Maven using Java 21
- started the application from `target/liza_alert_backend-0.0.1-SNAPSHOT.jar` under `nohup`

## Validation

- `docker compose ps`
- `java -version`
- `mvn -version`
- `curl http://localhost:8080/api/v1/lost-cases`
- reviewed `app.log` for successful Tomcat and Liquibase startup

## Outcome

- PostgreSQL container is healthy on port `5434`
- application responds on `http://localhost:8080/api/v1/lost-cases` with HTTP `200`
- first startup completed successfully with Liquibase migrations applied

## Notes

- current application process is started with `nohup` and tracked by `app.pid`
- this is suitable for first verification, but production operation should move to `systemd` or a containerized app service
