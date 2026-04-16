#!/usr/bin/env bash

set -euo pipefail

SECRETS_FILE="/etc/liza-alert-backend/liza-alert-backend.env"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

usage() {
  cat <<'EOF'
Использование:
  bash scripts/vps-secrets.sh show    - показать текущие секреты через sudo
  bash scripts/vps-secrets.sh edit    - отредактировать секреты через sudoeditor/nano
  bash scripts/vps-secrets.sh sync    - пересобрать .env в каталоге проекта из root-only env-файла
  bash scripts/vps-secrets.sh status  - показать права и наличие файлов
EOF
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || {
    printf '[ERROR] Команда %s не найдена\n' "$1" >&2
    exit 1
  }
}

require_command sudo

ACTION="${1:-}"

case "$ACTION" in
  show)
    sudo cat "$SECRETS_FILE"
    ;;
  edit)
    if command -v sudoedit >/dev/null 2>&1; then
      sudoedit "$SECRETS_FILE"
    else
      sudo nano "$SECRETS_FILE"
    fi
    ;;
  sync)
    sudo install -m 600 -o "$(id -un)" -g "$(id -gn)" "$SECRETS_FILE" "$PROJECT_DIR/.env"
    printf '[INFO] Файл %s обновлен из %s\n' "$PROJECT_DIR/.env" "$SECRETS_FILE"
    ;;
  status)
    sudo ls -l "$SECRETS_FILE"
    ls -l "$PROJECT_DIR/.env" 2>/dev/null || true
    ;;
  *)
    usage
    exit 1
    ;;
esac
