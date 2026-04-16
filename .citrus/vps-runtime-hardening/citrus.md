# CITRUS Delivery Record

- Change set: `vps-runtime-hardening`
- Goal: make the first VPS deployment persistent and externally reachable

## Environment

- Host: `185.21.8.116:2222`
- OS: `Ubuntu 24.04.3 LTS`

## Actions performed

- persisted VM tuning in `/etc/sysctl.d/99-liza-alert-backend.conf`
- created `systemd` unit `/etc/systemd/system/liza-alert-backend.service`
- enabled `liza-alert-backend.service` on boot
- moved application startup from `nohup` to managed `systemd`
- installed and configured `nginx` as reverse proxy from port `80` to `127.0.0.1:8080`
- enabled `nginx` on boot

## Validation

- `systemctl is-enabled liza-alert-backend.service nginx`
- `systemctl is-active liza-alert-backend.service nginx`
- `curl http://localhost/api/v1/lost-cases` on VPS
- `curl http://185.21.8.116/api/v1/lost-cases` from local environment
- reviewed `liza-alert-backend.service` status and `nginx -t`

## Outcome

- application now runs under `systemd`
- reverse proxy works on port `80`
- API is reachable externally with HTTP `200`
- swap tuning is persisted through `sysctl.d`

## Notes

- current setup is still HTTP-only; TLS is not configured yet
- database remains in Docker Compose and is started from the app service via `ExecStartPre`
