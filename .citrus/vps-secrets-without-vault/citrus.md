# CITRUS Delivery Record

- Change set: `vps-secrets-without-vault`
- Goal: move runtime secrets out of tracked config files without introducing Vault complexity

## Scope

- switched `compose.yaml` to consume values from `.env`
- added `.env.example` as a safe template
- ignored local runtime secret/log files in `.gitignore`
- added `scripts/vps-secrets.sh` for controlled secret operations on the VPS
- updated `scripts/deploy.sh` to bootstrap `.env` from `/etc/liza-alert-backend/liza-alert-backend.env` when needed

## Validation

- `bash -n scripts/deploy.sh`
- `bash -n scripts/vps-secrets.sh`
- `./mvnw.cmd test`

## Outcome

- secrets can stay root-only on the VPS in `/etc/liza-alert-backend/liza-alert-backend.env`
- repository keeps only a safe `.env.example`
- deploy flow can recreate `.env` from the protected env file when required
