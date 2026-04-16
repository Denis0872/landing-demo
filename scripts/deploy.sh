#!/usr/bin/env bash

set -euo pipefail

APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVICE_NAME="liza-alert-backend.service"
APP_URL="https://lizaalertspb.ru/api/v1/lost-cases"
MAVEN_OPTS_VALUE="-Xmx384m -XX:+UseSerialGC"

info() {
  printf '[INFO] %s\n' "$1"
}

fail() {
  printf '[ERROR] %s\n' "$1" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || fail "Команда '$1' не найдена"
}

require_command git
require_command mvn
require_command sudo
require_command curl

cd "$APP_DIR"

info "Каталог проекта: $APP_DIR"

if [[ ! -f "$APP_DIR/.env" ]]; then
  if [[ -f /etc/liza-alert-backend/liza-alert-backend.env ]]; then
    info "Синхронизирую .env из /etc/liza-alert-backend/liza-alert-backend.env"
    sudo install -m 600 -o "$(id -un)" -g "$(id -gn)" /etc/liza-alert-backend/liza-alert-backend.env "$APP_DIR/.env"
  else
    fail "Не найден .env и отсутствует /etc/liza-alert-backend/liza-alert-backend.env"
  fi
fi

CURRENT_BRANCH="$(git rev-parse --abbrev-ref HEAD)"
if [[ "$CURRENT_BRANCH" != "main" ]]; then
  fail "Скрипт рассчитан на ветку main, сейчас активна '$CURRENT_BRANCH'"
fi

if ! git diff --quiet || ! git diff --cached --quiet; then
  fail "Рабочее дерево не чистое. Сначала закоммить или убери локальные изменения."
fi

if [[ -n "$(git ls-files --others --exclude-standard)" ]]; then
  fail "Есть неотслеживаемые файлы. Сначала убери их или добавь в .gitignore."
fi

info "Обновляю репозиторий"
git pull --ff-only

info "Собираю приложение"
export MAVEN_OPTS="$MAVEN_OPTS_VALUE"
mvn -q -DskipTests package

info "Перезапускаю сервис $SERVICE_NAME"
sudo systemctl restart "$SERVICE_NAME"

info "Жду запуска сервиса"
for _ in {1..24}; do
  SERVICE_STATE="$(systemctl is-active "$SERVICE_NAME" || true)"
  if [[ "$SERVICE_STATE" == "active" ]]; then
    break
  fi
  sleep 5
done

SERVICE_STATE="$(systemctl is-active "$SERVICE_NAME" || true)"
if [[ "$SERVICE_STATE" != "active" ]]; then
  sudo systemctl --no-pager --full status "$SERVICE_NAME"
  fail "Сервис $SERVICE_NAME не перешел в состояние active"
fi

info "Проверяю HTTP endpoint"
HTTP_CODE="000"
for _ in {1..12}; do
  HTTP_CODE="$(curl -sk -o /tmp/liza_alert_backend_deploy_health.out -w '%{http_code}' "$APP_URL" || true)"
  if [[ "$HTTP_CODE" == "200" ]]; then
    break
  fi
  sleep 5
done

if [[ "$HTTP_CODE" != "200" ]]; then
  cat /tmp/liza_alert_backend_deploy_health.out >&2 || true
  fail "Health-check РІРµСЂРЅСѓР» HTTP $HTTP_CODE"
fi

info "Деплой завершен успешно"
info "Текущий commit: $(git rev-parse --short HEAD)"
info "Ответ API: $(cat /tmp/liza_alert_backend_deploy_health.out)"
