# CITRUS Delivery Record

- Change set: `vps-nginx-rate-limit`
- Goal: reduce noise from opportunistic scanners by adding basic rate limiting to nginx without breaking normal site access

## Environment

- Host: `185.21.8.116:2222`
- Domain: `lizaalertspb.ru`

## Actions performed

- added global nginx zones in `/etc/nginx/conf.d/liza_alert_backend_rate_limit.conf`
  - `limit_req_zone $binary_remote_addr zone=liza_alert_backend_per_ip:10m rate=5r/s`
  - `limit_conn_zone $binary_remote_addr zone=liza_alert_backend_conn_per_ip:10m`
- applied per-server limits in `/etc/nginx/sites-available/liza_alert_backend`
  - `limit_req zone=liza_alert_backend_per_ip burst=20 nodelay`
  - `limit_conn liza_alert_backend_conn_per_ip 20`
- validated nginx configuration and reloaded the service

## Validation

- `nginx -t`
- `curl -I https://lizaalertspb.ru`
- repeated API smoke requests to confirm normal traffic still passes

## Outcome

- site remains available with HTTP `200`
- repeated normal requests still succeed
- nginx now has basic per-IP request and connection limiting to reduce scanner noise

## Notes

- this is lightweight protection and not a full WAF
- more aggressive controls can be added later if scanner traffic becomes materially disruptive
