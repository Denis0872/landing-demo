# CITRUS Delivery Record

- Change set: `deploy-script`
- Goal: add a repeatable deployment/update script for the VPS runtime

## Scope

- created `scripts/deploy.sh`
- script flow:
  - ensure clean working tree on `main`
  - `git pull --ff-only`
  - Maven package build with constrained memory options
  - `systemd` restart for `liza-alert-backend.service`
  - wait for healthy service state
  - retrying HTTPS health-check against `https://lizaalertspb.ru/api/v1/lost-cases`

## Validation

- `bash -n scripts/deploy.sh`
- `./mvnw.cmd test`

## Outcome

- repository now contains a single command deployment helper for VPS updates
- script is consistent with the current production runtime model (`git + maven + systemd + nginx`)
