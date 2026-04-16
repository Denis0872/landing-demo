# CITRUS Delivery Record

- Change set: `vps-bootstrap-tools`
- Goal: prepare the target VPS for `liza_alert_backend` deployment with Java 21 and Docker tooling

## Environment

- Host: `185.21.8.116:2222`
- OS: `Ubuntu 24.04.3 LTS`
- User verified: `denis`

## Actions performed

- verified SSH access for `denis`
- detected that `denis` had no usable passwordless `sudo` for package installation
- used direct `root` SSH access to complete bootstrap safely
- installed `openjdk-21-jdk`
- installed `docker-compose-v2`
- ensured Docker service is enabled and active
- added user `denis` to group `docker`

## Validation

- `ssh -p 2222 denis@185.21.8.116 "id && java -version && javac -version && docker --version && docker compose version && docker ps --format '{{.Names}} {{.Status}}'"`
- `ssh -p 2222 root@185.21.8.116 "systemctl is-enabled docker && systemctl is-active docker"`

## Outcome

- Java 21 is installed and available for `denis`
- Docker CLI works for `denis` without `sudo`
- Docker Compose v2 is installed
- Docker service is enabled and running

## Notes

- VPS currently has low memory (`~961 MiB`) and no swap configured; this may matter during image builds or heavier Spring startup scenarios
