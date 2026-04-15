# CITRUS Delivery Record

- Change set: `vps-canonical-domain`
- Goal: make `https://lizaalertspb.ru` the single canonical public URL and redirect all other domain variants to it

## Environment

- Host: `185.21.8.116:2222`
- Domains:
  - `lizaalertspb.ru`
  - `www.lizaalertspb.ru`

## Actions performed

- updated nginx virtual host configuration to split the domain variants into dedicated server blocks
- configured redirects:
  - `http://lizaalertspb.ru` -> `https://lizaalertspb.ru`
  - `http://www.lizaalertspb.ru` -> `https://lizaalertspb.ru`
  - `https://www.lizaalertspb.ru` -> `https://lizaalertspb.ru`
- kept the application backend only behind canonical `https://lizaalertspb.ru`
- validated nginx config and reloaded the service

## Validation

- `curl -I http://lizaalertspb.ru`
- `curl -I https://lizaalertspb.ru`
- `curl -I http://www.lizaalertspb.ru`
- `curl -I https://www.lizaalertspb.ru`
- `curl https://lizaalertspb.ru/api/v1/lost-cases`

## Outcome

- apex domain `https://lizaalertspb.ru` is canonical and serves the site with `200`
- all non-canonical variants redirect with `301` to the canonical URL
- API remains available on the canonical HTTPS domain
